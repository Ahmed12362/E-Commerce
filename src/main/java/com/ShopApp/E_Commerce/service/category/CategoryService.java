package com.ShopApp.E_Commerce.service.category;

import com.ShopApp.E_Commerce.exceptions.AlreadyExistException;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.Category;
import com.ShopApp.E_Commerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistException(category.getName() + " Already Exist"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
    Category exitingCategory = getCategoryById(id);
    exitingCategory.setName(category.getName());
    return categoryRepository.save(exitingCategory);

//        return Optional.ofNullable(getCategoryById(id))
//                .map(existingCategory -> {
//                    existingCategory.setName(category.getName());
//                    return categoryRepository.save(existingCategory);
//                }).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
            throw new ResourceNotFoundException("Category not found");
        });
    }
}
