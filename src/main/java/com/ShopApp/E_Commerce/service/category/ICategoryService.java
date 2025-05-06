package com.ShopApp.E_Commerce.service.category;

import com.ShopApp.E_Commerce.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategory();
    Category addCategory(Category category);
    Category updateCategory(Category category , Long id);
    void deleteCategoryById(Long id);
}
