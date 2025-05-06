package com.ShopApp.E_Commerce.repository;

import com.ShopApp.E_Commerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem , Long> {
    void deleteAllByCartId(Long id);
}
