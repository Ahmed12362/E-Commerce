package com.ShopApp.E_Commerce.service.cart;

import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Cart;
import com.ShopApp.E_Commerce.model.User;
import com.ShopApp.E_Commerce.repository.CartItemRepository;
import com.ShopApp.E_Commerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
//    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
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
                   return cartRepository.save(cart);
               });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
       return cartRepository.findByUserUserId(userId);
    }
}
