package com.ShopApp.E_Commerce.service.image;

import com.ShopApp.E_Commerce.dto.ImageDto;
import com.ShopApp.E_Commerce.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> file , Long product_id);
    Image updateImage(MultipartFile file , Long image_id);
}
