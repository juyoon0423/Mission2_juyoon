package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.CustomUserDetails;
import com.example.shoppingmall.entity.Enum.Role;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;

    public JpaUserDetailsManager(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;

        createUser(CustomUserDetails.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .authorities(Role.ROLE_ADMIN.name())
                .build()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);
        UserEntity entity = optionalUser.get();

        return CustomUserDetails.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .age(entity.getAge())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
                .name(entity.getName())
                .businessNum(entity.getBusinessNum())
                .authorities(entity.getAuthorities())
                .build();
    }


    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        try {
            CustomUserDetails userDetails = (CustomUserDetails) user;

            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .authorities(userDetails.getRawAuthorities())
                    .password(userDetails.getPassword())
                    .build();

            userRepository.save(newUser);

        } catch (ClassCastException e) {
            log.error("Failed Cast to: {}", CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }


    @Override
    public void updateUser(UserDetails user) {
        throw new ResponseStatusException((HttpStatus.NOT_IMPLEMENTED));
    }

    @Override
    public void deleteUser(String username) {
        throw new ResponseStatusException((HttpStatus.NOT_IMPLEMENTED));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new ResponseStatusException((HttpStatus.NOT_IMPLEMENTED));
    }


}

