package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BusinessStatusDto {
    private Long businessId;
    private String name;
    private String authorities;

    public static BusinessStatusDto fromEntity(UserEntity entity) {
        return BusinessStatusDto.builder()
                .businessId(entity.getId())
                .name(entity.getUsername())
                .authorities(entity.getAuthorities())
                .build();
    }

}
