package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;
    @Setter
    private String introduction;
    @Setter
    private String reason;
    @Setter
    private String delReason;
    @Setter
    private String shopStatus;
    @Setter
    private String shopResponse;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}