package com.shop_app.category;

import com.shop_app.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    @Query("SELECT c.name FROM Category c WHERE c.name IN :names")
    List<String> findNamesInList(@Param("names") List<String> names);
}
