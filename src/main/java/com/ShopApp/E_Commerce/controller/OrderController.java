package com.ShopApp.E_Commerce.controller;

import com.ShopApp.E_Commerce.dto.OrderDto;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Order;
import com.ShopApp.E_Commerce.response.ApiResponse;
import com.ShopApp.E_Commerce.response.StripeResponse;
import com.ShopApp.E_Commerce.service.order.IOrderService;
import com.ShopApp.E_Commerce.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> createOrder() {
        try {
            StripeResponse stripeResponse = orderService.placeOrder();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse("Checkout Success" , stripeResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error Occur", e.getMessage()));
        }
    }
@GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Loaded!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Error Occur", e.getMessage()));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("my-orders")
    public ResponseEntity<ApiResponse> getUserOrders() {
        try {
            List<OrderDto> order = orderService.getUserOrders();
            return ResponseEntity.ok(new ApiResponse("Loaded!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Error Occur", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/download")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<byte[]> downloadOrderPdf(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            Long currentUserId = orderService.getAuthenticatedUserId();

            if (!order.getUser().getUserId().equals(currentUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null);
            }

            byte[] pdf = orderService.generateOrderPdf(order);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .inline()
                    .filename("order_" + orderId + ".pdf")
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}
