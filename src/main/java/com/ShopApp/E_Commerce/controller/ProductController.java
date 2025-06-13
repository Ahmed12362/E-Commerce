package com.ShopApp.E_Commerce.controller;

import com.ShopApp.E_Commerce.dto.ProductDto;
import com.ShopApp.E_Commerce.exceptions.AlreadyExistException;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Product;
import com.ShopApp.E_Commerce.request.AddProductRequest;
import com.ShopApp.E_Commerce.request.UpdateProductRequest;
import com.ShopApp.E_Commerce.response.ApiResponse;
import com.ShopApp.E_Commerce.service.product.IProductService;
import com.ShopApp.E_Commerce.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.PrimitiveIterator;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProduct();
        List<ProductDto> productDtos = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Success", productDtos));
    }

    @GetMapping("/product/{id}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            var product = productService.getProductById(id);
            var productDto = productService.convertToProductDto(product);
            return ResponseEntity.ok(new ApiResponse("Found!", productDto));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request) {
        try {
            Product theProduct = productService.addProduct(request);
            return ResponseEntity.ok(new ApiResponse("Added Success!", theProduct));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")

    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable long id,@RequestBody UpdateProductRequest request) {
        try {
            Product theProduct = productService.updateProductById(request , id);
            return ResponseEntity.ok(new ApiResponse("Added Success!", theProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted Success!", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }
    @GetMapping("/product/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand ,@RequestParam String name)  {
        try {
            List<Product> theProduct = productService.getProductByBrandAndName(brand , name);
            List<ProductDto> productDtos = productService.getConvertedProducts(theProduct);

            if(theProduct.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No product found with name: "+name , null));
            }
            return ResponseEntity.ok(new ApiResponse("Found!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }
    @GetMapping("/product/by/category-and-name")
    public ResponseEntity<ApiResponse> getProductByCategoryAndName(@RequestParam String category ,@RequestParam String name)  {
        try {
            List<Product> theProduct = productService.getProductByCategoryAndBrand(category , name);
            List<ProductDto> productDtos = productService.getConvertedProducts(theProduct);
            if(theProduct.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No product found with name: "+name , null));
            }
            return ResponseEntity.ok(new ApiResponse("Found!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }
    @GetMapping("/product/by-name")
    public ResponseEntity<ApiResponse> getProductByName(@RequestParam String name)  {
        try {
            List<Product> theProduct = productService.getProductByName(name);
            List<ProductDto> productDtos = productService.getConvertedProducts(theProduct);
            if(theProduct.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No product found with name: "+name , null));
            }
            return ResponseEntity.ok(new ApiResponse("Found!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }
    @GetMapping("/product/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand)  {
        try {
            List<Product> theProduct = productService.getProductByBrand(brand);
            if(theProduct.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No product found with brand: "+brand , null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(theProduct);
            return ResponseEntity.ok(new ApiResponse("Found!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }
    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category)  {
        try {
            List<Product> theProduct = productService.getProductByCategory(category);
            if(theProduct.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("No product found with category: "+category , null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(theProduct);


            return ResponseEntity.ok(new ApiResponse("Found!", productDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage() , null));
        }
    }
    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand , @RequestParam String name){
        try{
            Long counter = productService.countProductByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product Count!" , counter));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(e.getMessage() , null));
        }
    }
}
