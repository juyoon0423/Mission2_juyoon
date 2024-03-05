package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByOffer(String name);
}