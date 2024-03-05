package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ShopDto;
import com.example.shoppingmall.entity.Shop;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.repository.ShopRepository;
import com.example.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    // 개설
    public ShopDto createShop(ShopDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            Shop shop = Shop.builder()
                    .name(dto.getName())
                    .introduction(dto.getIntroduction())
                    .category(dto.getCategory())
                    .build();

            user.getShops().add(shop);
            userRepository.save(user);

            return ShopDto.fromEntity(shopRepository.save(shop));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 신청 목록 보기
    public List<ShopDto> readList() {
        return shopRepository.findAll().stream()
                .map(ShopDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 수정
    public ShopDto updateShop(Long shopId, ShopDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            Shop shop = shopRepository.findById(shopId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            if (!user.getShops().contains(shop)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            shop.setIntroduction(dto.getIntroduction());
            shop.setName(dto.getName());
            shop.setCategory(dto.getCategory());

            return ShopDto.fromEntity(shopRepository.save(shop));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 삭제
    public void deleteShop(Long shopId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);
        if (optionalUser.isPresent()) {
            shopRepository.deleteById(shopId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
