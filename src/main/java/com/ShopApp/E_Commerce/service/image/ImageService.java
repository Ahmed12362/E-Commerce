package com.ShopApp.E_Commerce.service.image;

import com.ShopApp.E_Commerce.dto.ImageDto;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Image;
import com.ShopApp.E_Commerce.model.Product;
import com.ShopApp.E_Commerce.repository.ImageRepository;
import com.ShopApp.E_Commerce.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    final private ImageRepository imageRepository;
    final private IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Image is not found with id #" + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Image is not found with id #" + id);
                });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long product_id) {
        Product product = productService.getProductById(product_id);
        List<ImageDto> imageDtos = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImaged = imageRepository.save(image);
                savedImaged.setDownloadUrl(buildDownloadUrl + savedImaged.getId());
                imageRepository.save(savedImaged);
                ImageDto imageDto= new ImageDto();
                imageDto.setImageName(savedImaged.getFileName());
                imageDto.setImageId(savedImaged.getId());
                imageDto.setDownloadUrl(savedImaged.getDownloadUrl());

                imageDtos.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return imageDtos;
    }

    @Override
    public Image updateImage(MultipartFile file, Long image_id) {
        Image image = getImageById(image_id);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
            return image;
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
