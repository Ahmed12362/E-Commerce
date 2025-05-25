package com.ShopApp.E_Commerce.dto;

import com.ShopApp.E_Commerce.model.CartItem;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
@Data
public class CartDto {
    private Long id;
    private BigDecimal totalAmount;

    private Set<CartItemDto> cartItems;
}
