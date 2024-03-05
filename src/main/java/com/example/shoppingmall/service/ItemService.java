package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Enum.Role;
import com.example.shoppingmall.entity.Enum.Status;
import com.example.shoppingmall.entity.UserEntity;
import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    // 물품 등록
    public ItemDto create(ItemDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(currentUsername);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setAuthorities(Role.ROLE_SELLER.name());
            userRepository.save(user);

            if (user.getAuthorities().equals(Role.ROLE_SELLER.name())) {
                Item item = Item.builder()
                        .seller(currentUsername)
                        .min(dto.getMinPrice())
                        .status(Status.SELLING.name())
                        .title(dto.getTitle())
                        .description(dto.getDescription())
                        .user(user)
                        .build();

                return ItemDto.fromEntity(itemRepository.save(item));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // 물품 전체 보기
    public List<ItemDto> readAll() {
        List<Item> items = itemRepository.findAll();
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(ItemDto.fromEntity(item));
        }
        return itemDtos;
    }

    // 물품 하나 보기
    public ItemDto readOne(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            return ItemDto.fromEntity(optionalItem.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    // 물품 수정
    public ItemDto update(Long itemId, ItemDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (item.getUser().getAuthorities().equals(Role.ROLE_SELLER.name())) {
            if (item.getUser().getUsername().equals(currentUsername)) {
                item.setTitle(dto.getTitle());
                item.setDescription(dto.getDescription());
                item.setImage(dto.getImage());
                item.setMin(dto.getMinPrice());

                return ItemDto.fromEntity(itemRepository.save(item));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    // 물품 삭제
    public void delete(Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (item.getUser().getAuthorities().equals(Role.ROLE_SELLER.name())) {
            if (item.getUser().getUsername().equals(currentUsername)) {
                itemRepository.deleteById(itemId);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}