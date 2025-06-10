package com.ShopApp.E_Commerce.security.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopConfig{
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
