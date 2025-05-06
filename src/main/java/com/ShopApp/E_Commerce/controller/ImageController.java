package com.ShopApp.E_Commerce.controller;

import com.ShopApp.E_Commerce.dto.ImageDto;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Image;
import com.ShopApp.E_Commerce.response.ApiResponse;
import com.ShopApp.E_Commerce.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long product_id) {
        try {
            List<ImageDto> imageDtos = imageService.saveImage(files, product_id);
            return ResponseEntity.ok(new ApiResponse("Uploaded Success", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Fail to Upload", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{image_id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long image_id) throws SQLException {
        Image image = imageService.getImageById(image_id);
        ByteArrayResource resource =
                new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\""+image.getFileName()+ "\"")
                .body(resource);

    }
    @PutMapping("/image/{image_id}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long image_id ,@RequestBody MultipartFile file) {
        try {
            Image image = imageService.getImageById(image_id);
            if (image != null) {
                imageService.updateImage(file, image_id);
            }
            return ResponseEntity.ok(new ApiResponse("Updated Success", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update Failed!" , null));
        }
    }
    @PutMapping("/image/{image_id}/delete")

    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long image_id) {
        try {
            Image image = imageService.getImageById(image_id);
            if (image != null) {
                imageService.deleteImageById(image_id);
            }
            return ResponseEntity.ok(new ApiResponse("Delete Success", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete Failed!" , null));
        }
    }

}
