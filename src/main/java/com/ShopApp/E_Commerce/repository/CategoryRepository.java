package com.ShopApp.E_Commerce.repository;

import com.ShopApp.E_Commerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category , Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
