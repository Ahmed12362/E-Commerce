package com.ShopApp.E_Commerce.repository;

import com.ShopApp.E_Commerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role , Long> {
    Role findByName(String name);
}
