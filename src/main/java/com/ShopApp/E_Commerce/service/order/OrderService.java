package com.ShopApp.E_Commerce.service.order;

import com.ShopApp.E_Commerce.dto.OrderDto;
import com.ShopApp.E_Commerce.enums.OrderStatus;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.*;
import com.ShopApp.E_Commerce.repository.OrderRepository;
import com.ShopApp.E_Commerce.repository.ProductRepository;
import com.ShopApp.E_Commerce.service.cart.ICartService;
import com.ShopApp.E_Commerce.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public Order placeOrder() {
        User user = userService.getAuthenticatedUser();
        Cart cart = cartService.getCartByUserId(user.getUserId());
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException("Order Not Found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserUserId(userId);
        return orders
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order , OrderDto.class);
    }
}
