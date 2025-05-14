package com.ShopApp.E_Commerce.repository;

import com.ShopApp.E_Commerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {
    boolean existsByEmail(String email);
}
