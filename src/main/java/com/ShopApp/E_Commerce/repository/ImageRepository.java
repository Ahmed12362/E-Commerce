package com.ShopApp.E_Commerce.repository;

import com.ShopApp.E_Commerce.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image , Long> {
    List<Image> findByProductId(Long id);
}
