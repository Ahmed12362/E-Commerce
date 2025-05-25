package com.ShopApp.E_Commerce.dto;

import com.ShopApp.E_Commerce.model.Cart;
import com.ShopApp.E_Commerce.model.Order;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;
@Data
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
