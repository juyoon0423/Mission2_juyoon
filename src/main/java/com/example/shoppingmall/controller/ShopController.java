package com.example.shoppingmall.controller;

import lombok.RequiredArgsConstructor;
import com.example.shoppingmall.dto.ShopDto;
import com.example.shoppingmall.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService service;

    @PostMapping("/createShop")
    public ShopDto createShop(
            @RequestBody
            ShopDto dto
    ) {
        return service.createShop(dto);
    }

    @GetMapping("/readList")
    public List<ShopDto> readList() {
        return service.readList();
    }

    @PutMapping("/{shopId}/update")
    public ShopDto updateShop(
            @PathVariable("shopId")
            Long id,
            @RequestBody
            ShopDto dto
    ) {
        return service.updateShop(id, dto);
    }

    @DeleteMapping("/{shopId}/delete")
    public void deleteShop(
            @PathVariable("shopId")
            Long id
    ) {
        service.deleteShop(id);
    }
}
