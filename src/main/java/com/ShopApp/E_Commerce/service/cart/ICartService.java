package com.ShopApp.E_Commerce.service.cart;

import com.ShopApp.E_Commerce.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalAmount(Long id);

    Long initializeNewCart();
}
