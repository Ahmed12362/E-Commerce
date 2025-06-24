package com.ShopApp.E_Commerce.request;

import com.ShopApp.E_Commerce.model.Category;
import com.ShopApp.E_Commerce.model.Image;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddProductRequest {

    @NotBlank(message = "Product name must not be empty")
    private String name;

    @NotBlank(message = "Brand must not be empty")
    private String brand;

    @NotNull(message = "Price is required")
//    @PositiveOrZero
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @Min(value = 0, message = "Inventory must be zero or more")
    private int inventory;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Category is required")
    private Category category;

//    @NotNull(message = "Images list must not be null")
//    @Size(min = 1, message = "At least one image is required")
    private List<Image> images;
}
