package com.ShopApp.E_Commerce.repository;

import com.ShopApp.E_Commerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserUserId(Long userId);
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.user.userId = :userId")
    Cart findByUserUserIdWithItems(@Param("userId") Long userId);

}
