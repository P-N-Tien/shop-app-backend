package com.shop_app.category;

import com.shop_app.category.mapper.CategoryMapper;
import com.shop_app.category.request.CategoryRequest;
import com.shop_app.category.response.CategoryResponse;
import com.shop_app.category.validator.CategoryValidator;
import com.shop_app.product.entity.Product;
import com.shop_app.shared.exceptions.IllegalArgumentException;
import com.shop_app.category.entity.Category;
import com.shop_app.shared.validate.Validate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidator categoryValidator;

    @Override
    @Transactional
    public void createCategories(List<CategoryRequest> req) {

        Validate.notEmpty(req, "Category list request must not be null or empty");

        List<String> names = req.stream()
                .map(dto -> dto.getName())
                .collect(Collectors.toList());

        // Validate that the names list does not contain any duplicates
        categoryValidator.validateInputListHasNotDuplicate(names);
        categoryValidator.validateListIsNotDuplicateDB(names);

        List<Category> categories = req.stream()
                .map(dto -> categoryMapper.createFrom(dto))
                .toList();

        categoryRepository.saveAll(categories);

        log.info("[CATEGORY][CREATE]");
    }

    @Override
    @Transactional
    public void update(long id, CategoryRequest req) {
        Validate.requiredNonNull(req, "Category must be not empty");

        Category category = categoryValidator.validateAndGet(id);
        if (!category.getName().equals(req.getName())) {
            categoryValidator.validateDuplicate(req.getName());
        }

        categoryMapper.updateFrom(req, category);

        log.info("[CATEGORY][UPDATE] id={} name={}", id, req.getName());
    }

    @Override
    @Transactional
    public void delete(long id) {
        Category category = categoryValidator.validateAndGet(id);
        List<Product> products = category.getProducts();

        if (!products.isEmpty())
            throw new IllegalArgumentException(
                    "Cannot delete category with associated products");

        categoryRepository.delete(category);

        log.info("[CATEGORY][DELETE] id={}", id);
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse findById(long id) {
        return categoryMapper.toResponse(categoryValidator.validateAndGet(id));
    }

}
