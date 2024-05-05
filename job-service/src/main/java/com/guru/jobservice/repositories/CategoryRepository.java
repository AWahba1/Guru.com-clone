package com.guru.jobservice.repositories;

import com.guru.jobservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query(value = "select * from get_categories_with_subcategories_and_skills(?1)", nativeQuery = true)
    List<Category> getCategories(UUID categoryId);
}
