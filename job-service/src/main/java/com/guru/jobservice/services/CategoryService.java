package com.guru.jobservice.services;

import com.guru.jobservice.model.Category;
import com.guru.jobservice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Cacheable(value = "categories", key = "#categoryId != null ? #categoryId : 'all_categories'")
    public List<Category> getCategories(UUID categoryId){
        return categoryRepository.getCategories(categoryId);
    }
}
