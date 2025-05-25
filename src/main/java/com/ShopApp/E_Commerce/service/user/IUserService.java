package com.ShopApp.E_Commerce.service.user;

import com.ShopApp.E_Commerce.dto.UserDto;
import com.ShopApp.E_Commerce.model.User;
import com.ShopApp.E_Commerce.request.CreateUserRequest;
import com.ShopApp.E_Commerce.request.UserUpdateRequest;
import lombok.Lombok;

public interface IUserService {
    User getUserById(Long userId);
    User creatUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request , Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
