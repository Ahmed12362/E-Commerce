package com.ShopApp.E_Commerce.controller;

import com.ShopApp.E_Commerce.dto.CartDto;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Cart;
import com.ShopApp.E_Commerce.response.ApiResponse;
import com.ShopApp.E_Commerce.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{cartId}/my-cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            CartDto cartDto = cartService.convertCartToDto(cart);
            return ResponseEntity.ok(new ApiResponse("Success", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cartId}/clear")
    @PreAuthorize("hasRole('ROLE_USER')")

    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @GetMapping("/my-cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> getUserCart(){
            Cart cart = cartService.getCartUser();
            if(cart == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("User didn't have cart yet" , "Can't get cart"));
            }
            CartDto cartDto = cartService.convertCartToDto(cart);
            return ResponseEntity.ok(new ApiResponse("Found!" , cartDto));
    }

    @GetMapping("/{cartId}/cart/total-price")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            BigDecimal totalPrice = cartService.getTotalAmount(cartId);
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
