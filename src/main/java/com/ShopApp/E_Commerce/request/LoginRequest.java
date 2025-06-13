package com.ShopApp.E_Commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
