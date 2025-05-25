package com.ShopApp.E_Commerce.service.order;

import com.ShopApp.E_Commerce.dto.OrderDto;
import com.ShopApp.E_Commerce.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
