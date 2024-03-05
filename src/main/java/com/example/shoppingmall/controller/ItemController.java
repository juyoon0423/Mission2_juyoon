package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping("/create")
    public ItemDto create(
            @RequestBody
            ItemDto dto
    ) {
        return service.create(dto);
    }

    @GetMapping("/{itemId}")
    public ItemDto readOne(
            @PathVariable("itemId")
            Long itemId
    ) {
        return service.readOne(itemId);
    }

    @GetMapping
    public List<ItemDto> readAll() {
        return service.readAll();
    }

    @PutMapping("/{itemId}/update")
    public ItemDto update(
            @PathVariable("itemId")
            Long itemId,
            @RequestBody
            ItemDto dto
    ) {
        return service.update(itemId, dto);
    }

    @DeleteMapping("/{itemId}/delete")
    public void delete(
            @PathVariable("itemId")
            Long itemId
    ) {
        service.delete(itemId);
    }
}
