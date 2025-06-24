package com.ShopApp.E_Commerce.controller;

import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Cart;
import com.ShopApp.E_Commerce.model.User;
import com.ShopApp.E_Commerce.response.ApiResponse;
import com.ShopApp.E_Commerce.service.cart.ICartItemService;
import com.ShopApp.E_Commerce.service.cart.ICartService;
import com.ShopApp.E_Commerce.service.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @PostMapping("/item/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            User user = userService.getAuthenticatedUser();
               Cart cart = cartService.initializeNewCart(user);
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item Added Successfully!", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
        catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Deleted Successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/cart/{cartId}/item/{productId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Updated Successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}