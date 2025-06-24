package com.ShopApp.E_Commerce.service.cart;

import com.ShopApp.E_Commerce.dto.CartDto;
import com.ShopApp.E_Commerce.dto.CartItemDto;
import com.ShopApp.E_Commerce.dto.ProductDto;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Cart;
import com.ShopApp.E_Commerce.model.User;
import com.ShopApp.E_Commerce.repository.CartItemRepository;
import com.ShopApp.E_Commerce.repository.CartRepository;
import com.ShopApp.E_Commerce.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
//    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

    }

    @Override
    @Transactional
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalAmount(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
//        return cart.getCartItems().stream()
//                .map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Override
    public Cart initializeNewCart(User user){
       return Optional.ofNullable(getCartByUserId(user.getUserId()))
               .orElseGet(()-> {
                   Cart cart = new Cart();
                   cart.setUser(user);
                   user.setCart(cart);
                   return cartRepository.save(cart);
               });
    }
    @Override
    public Cart getCartUser(){
        User user = userService.getAuthenticatedUser();
        return cartRepository.findByUserUserIdWithItems(user.getUserId());
    }

    @Override
    public Cart getCartByUserId(Long userId) {
       return cartRepository.findByUserUserId(userId);
    }
    @Override
    public CartDto convertCartToDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setTotalAmount(cart.getTotalAmount());

        dto.setCartItems(cart.getCartItems().stream().map(item -> {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setId(item.getId());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setQuantity(item.getQuantity());

            ProductDto productDto = new ProductDto();
            productDto.setId(item.getProduct().getId());
            productDto.setName(item.getProduct().getName());
            productDto.setBrand(item.getProduct().getBrand());
            productDto.setPrice(item.getProduct().getPrice());
            productDto.setCategory(item.getProduct().getCategory());

            itemDto.setProduct(productDto);
            return itemDto;
        }).collect(Collectors.toSet()));

        return dto;
    }

}
