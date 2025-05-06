package com.ShopApp.E_Commerce.service.product;

import com.ShopApp.E_Commerce.dto.ProductDto;
import com.ShopApp.E_Commerce.model.Product;
import com.ShopApp.E_Commerce.request.AddProductRequest;
import com.ShopApp.E_Commerce.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProductById(UpdateProductRequest product , Long product_id);
    List<Product> getAllProduct();
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String category , String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String brand,String name);
    Long countProductByBrandAndName(String brand ,String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToProductDto(Product product);
}
