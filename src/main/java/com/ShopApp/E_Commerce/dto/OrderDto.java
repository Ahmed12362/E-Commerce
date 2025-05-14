package com.ShopApp.E_Commerce.dto;

import com.ShopApp.E_Commerce.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal TotalAmount;
    private String status;
    private List<OrderItemDto> items;
}
