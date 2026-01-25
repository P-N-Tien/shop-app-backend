package com.shop_app.product;

import com.github.javafaker.Faker;
import com.shop_app.product.request.ProductCreateRequest;
import com.shop_app.product.request.ProductFilterRequest;
import com.shop_app.product.request.ProductPatchRequest;
import com.shop_app.product.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Products", description = "API for managing products")
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

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

    @Operation(summary = "Search & Paging")
    @GetMapping("/search")
    public ResponseEntity<?> search(
            ProductFilterRequest filter,
            @PageableDefault(
                    page = 0, size = 20,
                    sort = "createdAt", direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.search(filter, pageable));
    }

    @Operation(summary = "Get Product by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping("/generate-dummy-products")
    private ResponseEntity<Void> generateDummyProducts(
    ) {
        Faker faker = new Faker();
        for (int i = 0; i < 10_000_000; i++) {
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
}
