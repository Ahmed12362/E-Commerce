package com.ShopApp.E_Commerce.service.order;

import com.ShopApp.E_Commerce.dto.OrderDto;
import com.ShopApp.E_Commerce.model.Order;
import com.lowagie.text.DocumentException;

import java.util.List;

public interface IOrderService {

    Order placeOrder();

    Order getOrderById(Long orderId);

    Long getAuthenticatedUserId();

    OrderDto getOrder(Long orderId);


    List<OrderDto> getUserOrders();

    OrderDto convertToDto(Order order);

    byte[] generateOrderPdf(Order order) throws DocumentException;
}
