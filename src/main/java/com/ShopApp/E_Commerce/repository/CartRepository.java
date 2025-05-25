package com.ShopApp.E_Commerce.repository;

import com.ShopApp.E_Commerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserUserId(Long userId);
}
