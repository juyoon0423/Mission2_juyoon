package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Offer;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String seller;
    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private String image;
    @Setter
    private Integer minPrice;
    private String status;
    @Setter
    private String response;
    private Offer offer;

    // 사용자 정보는 제공 x
    public static ItemDto fromEntity(Item entity) {
        return ItemDto.builder()
                .id(entity.getId())
                .seller(entity.getSeller())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .image(entity.getImage())
                .minPrice(entity.getMin())
                .status(entity.getStatus())
                .response(entity.getResponse())
                .build();
    }
}
