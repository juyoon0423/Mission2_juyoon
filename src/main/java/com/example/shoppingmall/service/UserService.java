package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.entity.Enum.Role;
import com.example.shoppingmall.entity.Shop;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.jwt.JwtRequestDto;
import com.example.shoppingmall.jwt.JwtResponseDto;
import com.example.shoppingmall.jwt.JwtTokenUtils;
import com.example.shoppingmall.repository.ShopRepository;
import com.example.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsManager detailsManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final ShopRepository shopRepository;

    // 회원 가입
    public void register(UserDetails user) {
        if (detailsManager.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;
            log.info("기존 유저 Encoded Password: {}", passwordEncoder.encode(userDetails.getPassword()));
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .password(passwordEncoder.encode(userDetails.getPassword()))
                    .authorities(Role.ROLE_INACTIVE.name())
                    .build();

            userRepository.save(newUser);


        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인
    public JwtResponseDto login(JwtRequestDto dto) {
        if (!detailsManager.userExists(dto.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        UserDetails userDetails = detailsManager.loadUserByUsername(dto.getUsername());
        log.info("User 비밀번호: {}", dto.getPassword());
        log.info("userDetails 비밀번호: {}", userDetails.getPassword());

        if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 토큰 발급
        String jwt = jwtTokenUtils.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        log.info("Token generated: {}", jwt);

        return response;
    }

    // 비활성 -> 일반
    public void updateUser(String username, UserDetails user){
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();
            CustomUserDetails customUserDetails = (CustomUserDetails) user;

            if (customUserDetails.inactiveToUser()) {
                existingUser.setNickname(customUserDetails.getNickname());
                existingUser.setEmail(customUserDetails.getEmail());
                existingUser.setPhone(customUserDetails.getPhone());
                existingUser.setName(customUserDetails.getName());
                existingUser.setAge(customUserDetails.getAge());
                existingUser.setAuthorities(String.valueOf(Role.ROLE_USER));

                userRepository.save(existingUser);
            }
        }
    }

    // 일반 -> 비즈니스
    public void updateBusinessUser(String username, UserDetails user){
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            UserEntity existingUser = optionalUser.get();
            CustomUserDetails customUserDetails = (CustomUserDetails) user;

            if (customUserDetails.userToBusiness()) {
                existingUser.setBusinessNum(customUserDetails.getBusinessNum());
                existingUser.setAuthorities(Role.ROLE_BUSINESS.name());

                Shop shop = Shop.builder()
                        .user(existingUser)
                        .build();

                existingUser.getShops().add(shop);
                shopRepository.save(shop);

                userRepository.save(existingUser);
            }
        }
    }
}
