package com.ShopApp.E_Commerce.controller;

import com.ShopApp.E_Commerce.dto.UserDto;
import com.ShopApp.E_Commerce.exceptions.AlreadyExistException;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.User;
import com.ShopApp.E_Commerce.request.CreateUserRequest;
import com.ShopApp.E_Commerce.request.UserUpdateRequest;
import com.ShopApp.E_Commerce.response.ApiResponse;
import com.ShopApp.E_Commerce.service.user.IUserService;
import com.ShopApp.E_Commerce.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Load", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        try {
            User user = userService.creatUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User Created Success!", userDto));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), "Can't create user"));
        }
    }
    @PostMapping("/addAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> createAdmin(@RequestBody @Valid CreateUserRequest request){
        try{
            User admin = userService.creatAdmin(request);
            UserDto adminDto = userService.convertUserToDto(admin);
            return ResponseEntity.ok(new ApiResponse("Admin Created Success!" , adminDto));
        }catch (AlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage() , "Can't create admin"));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
        try {
            User user = userService.updateUser(request, userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Updated Success!", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Deleted Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
