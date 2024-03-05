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
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Setter
    private String password;
    @Setter
    private String email;
    @Setter
    private String phone;
    @Setter
    private String avatar;
    @Setter
    private String nickname;
    @Setter
    private String name;
    @Setter
    private String age;
    @Setter
    private String businessStatus;
    @Setter
    private String businessNum;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Shop> shops = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Offer> offers = new ArrayList<>();

    @Setter
    private String authorities;

}