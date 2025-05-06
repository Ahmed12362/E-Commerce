package com.ShopApp.E_Commerce.request;

import com.ShopApp.E_Commerce.model.Category;
import com.ShopApp.E_Commerce.model.Image;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
    private List<Image> images;
}

