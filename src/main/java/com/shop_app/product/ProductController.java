package com.shop_app.product;

import com.github.javafaker.Faker;
import com.shop_app.images.ProductImageService;
import com.shop_app.images.ProductImagesRequest;
import com.shop_app.product.request.ProductCreateRequest;
import com.shop_app.product.request.ProductFilterRequest;
import com.shop_app.product.request.ProductPatchRequest;
import com.shop_app.product.response.ProductResponse;
import com.shop_app.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Products", description = "API for managing products")
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    private final ProductImageService productImageService;

    @Operation(summary = "Create product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Invalid")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createProduct(
            @ModelAttribute @Valid ProductCreateRequest req
    ) {
        productService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update product")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@ModelAttribute ProductPatchRequest req,
                                              @PathVariable long id) {
        productService.partialUpdate(id, req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search & Paging")
    public ResponseEntity<PageResponse<ProductResponse>> search(
            ProductFilterRequest filter,
            @PageableDefault(
                    page = 0, size = 20,
                    sort = "createdAt", direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.search(filter, pageable));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(productService.getProducts(categoryId, page, limit));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Product by Id")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/details")
    @Operation(summary = "Get Product by Name")
    public ResponseEntity<ProductResponse> getProductByName(
            @RequestParam String name) {
        return ResponseEntity.ok(productService.getByName(name));
    }

    @PostMapping("/generate-dummy-products")
    private ResponseEntity<Void> generateDummyProducts(
    ) {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            String productName = faker.commerce().productName();
            if (productService.existByName(productName)) {
                continue;
            }
            ProductCreateRequest req = ProductCreateRequest.builder()
                    .name(productName)
                    .price(BigDecimal.valueOf(faker.number().numberBetween(10L, 90_000_000L)))
                    .description(faker.lorem().sentence())
                    .categoryId(faker.number().numberBetween(1L, 3L))
                    .build();
            productService.create(req);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ProductImagesRequest>> uploadProductImages(
            @RequestParam(value = "productId") Long productId,
            @RequestParam(value = "files") MultipartFile[] files,
            @RequestParam(value = "primaryIndex") int primaryIndex) {
        return ResponseEntity.ok(
                productImageService.uploadProductImages(productId, files, primaryIndex));
    }
}
