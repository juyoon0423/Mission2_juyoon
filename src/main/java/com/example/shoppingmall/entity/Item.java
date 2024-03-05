package com.example.shoppingmall.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item_table")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seller;
    @Column(nullable = false)
    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private String image;
    @Setter
    private String status;
    @Setter
    private Integer min;
    @Setter
    private String response;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<Offer> orderOffers = new ArrayList<>();
}
