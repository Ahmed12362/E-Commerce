package com.ShopApp.E_Commerce.service.order;

import com.ShopApp.E_Commerce.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);

    List<Order> getUserOrders(Long userId);
}
