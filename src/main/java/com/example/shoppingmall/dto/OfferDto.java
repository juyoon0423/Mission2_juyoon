package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.Offer;
import com.example.shoppingmall.entity.UserEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {
    private Long id;
    private String itemName;
    private String offer;
    private String offerStatus;

    private UserEntity user;

    public static OfferDto fromEntity(Offer entity) {
        return OfferDto.builder()
                .id(entity.getId())
                .itemName(entity.getItemName())
                .offer(entity.getOffer())
                .offerStatus(entity.getOfferStatus())
                .build();
    }
}