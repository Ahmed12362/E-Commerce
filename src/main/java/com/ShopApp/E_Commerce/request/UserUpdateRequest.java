package com.ShopApp.E_Commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "Firstname mustn't be empty")
    private String firstName;
    @NotBlank(message = "Lastname mustn't be empty")
    private String lastName;
}
