package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Enum.Category;
import com.example.shoppingmall.entity.Shop;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String introduction;
    private String shopStatus;
    private String shopResponse;
    private String reason;
    private String deleteReason;
    private Category category;

    public boolean condition =
            name != null && introduction != null;
    public static ShopDto fromEntity(Shop entity) {
        return ShopDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .introduction(entity.getIntroduction())
                .shopStatus(entity.getShopStatus())
                .shopResponse(entity.getShopResponse())
                .reason(entity.getReason())
                .build();
    }
}