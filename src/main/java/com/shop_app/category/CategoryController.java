package com.shop_app.category;

import com.shop_app.category.request.CategoryListRequest;
import com.shop_app.category.request.CategoryRequest;
import com.shop_app.category.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories", description = "API for managing categories")
@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping("/bulk")
    @Operation(summary = "Create category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Invalid")
    })
    public ResponseEntity<String> createCategories(
            @Valid @RequestBody CategoryListRequest req
    ) {
        categoryService.createCategories(req.getCategories());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable long id,
            @Valid @RequestBody CategoryRequest req) {

        categoryService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {

        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category by Id")
    public ResponseEntity<CategoryResponse> getById(@PathVariable long id) {

        return ResponseEntity.ok(categoryService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Get all Category")
    public ResponseEntity<List<CategoryResponse>> getCategories() {

        return ResponseEntity.ok(categoryService.findAll());
    }
}

