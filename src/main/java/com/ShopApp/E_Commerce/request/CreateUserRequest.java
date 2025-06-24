package com.ShopApp.E_Commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "First name mustn't be empty")
    private String firstName;
    @NotBlank(message = "Last name mustn't be empty")
    private String lastName;

    @NotBlank(message = "Email mustn't be empty")
    private String email;

    @NotBlank(message = "Password mustn't be empty")
    private String password;
}
