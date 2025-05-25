package com.ShopApp.E_Commerce.service.cart;

import com.ShopApp.E_Commerce.model.Cart;
import com.ShopApp.E_Commerce.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalAmount(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
