package com.ShopApp.E_Commerce.service.user;

import com.ShopApp.E_Commerce.dto.OrderDto;
import com.ShopApp.E_Commerce.dto.OrderItemDto;
import com.ShopApp.E_Commerce.dto.UserDto;
import com.ShopApp.E_Commerce.exceptions.AlreadyExistException;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Role;
import com.ShopApp.E_Commerce.model.User;
import com.ShopApp.E_Commerce.repository.RoleRepository;
import com.ShopApp.E_Commerce.repository.UserRepository;
import com.ShopApp.E_Commerce.request.CreateUserRequest;
import com.ShopApp.E_Commerce.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public User creatAdmin(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN");
                    user.setRoles(Set.of(adminRole));
                    return userRepository.save(user);
                })
                .orElseThrow(()->new AlreadyExistException(request.getEmail()+" already exists!"));
    }
    @Override
    public User creatUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));

                    Role userRole = roleRepository.findByName("ROLE_USER");
                    user.setRoles(Set.of(userRole));
                    return userRepository.save(user);
                })
                .orElseThrow(()->new AlreadyExistException(request.getEmail()+" already exists!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                            existingUser.setFirstName(request.getFirstName());
                            existingUser.setLastName(request.getLastName());
                            return userRepository.save(existingUser);
                        }
                ).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, () ->
                {
                    throw new ResourceNotFoundException("User Not Found");
                });
    }

    //Convert Manually From User To User Dto
    @Override
    public UserDto convertUserToDto(User user){
        UserDto userDto = modelMapper.map(user , UserDto.class);

        // Manual mapping for orders
        List<OrderDto> orderDtos = user.getOrder().stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getOrderId());
            orderDto.setUserId(user.getUserId());
            orderDto.setOrderDate(order.getOrderDate());
            orderDto.setOrderStatus(order.getOrderStatus().toString());
            orderDto.setTotalAmount(order.getTotalAmount());

            // Map order items
            List<OrderItemDto> items = order.getOrderItems().stream().map(orderItem -> {
                OrderItemDto dto = new OrderItemDto();
                dto.setProductId(orderItem.getProduct().getId());
                dto.setProductName(orderItem.getProduct().getName());
                dto.setProductBrand(orderItem.getProduct().getBrand());
                dto.setPrice(orderItem.getPrice());
                dto.setQuantity(orderItem.getQuantity());
                return dto;
            }).toList();

            orderDto.setItems(items);
            return orderDto;
        }).toList();

        userDto.setOrders(orderDtos);

        return userDto;
    }

//    public UserDto convertUserToDto(User user){
//       return  modelMapper.map(user , UserDto.class);
//    }


    @Override
    public User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication();
    }

}
