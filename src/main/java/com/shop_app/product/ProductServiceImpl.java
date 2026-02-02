package com.shop_app.product;

import com.shop_app.category.validator.CategoryValidator;
import com.shop_app.product.enums.ProductStatus;
import com.shop_app.product.mapper.ProductMapper;
import com.shop_app.product.request.ProductCreateRequest;
import com.shop_app.product.request.ProductFilterRequest;
import com.shop_app.product.request.ProductPatchRequest;
import com.shop_app.product.response.ProductResponse;
import com.shop_app.product.specification.ProductSpecifications;
import com.shop_app.product.validator.ProductValidator;
import com.shop_app.category.entity.Category;
import com.shop_app.product.entity.Product;
import com.shop_app.shared.dto.PageResponse;
import com.shop_app.shared.exceptions.SystemException;
import com.shop_app.shared.validate.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository repository;
    private final CategoryValidator categoryValidator;
    private final ProductValidator productValidator;
    private final ProductMapper productMapper;

    /**
     * Creates a product and its related metadata atomically.
     */
    @Override
    @Transactional
    public void create(ProductCreateRequest req) {
        Validate.requiredNonNull(req, "Product request must be not null");
        try {
            // 1. Validate req
            productValidator.validateDuplicateName(req.getName());
            Category getCategory = categoryValidator.validateAndGet(req.getCategoryId());

            // 2. Map to entity
            Product product = productMapper.createFrom(req);
            product.setCategory(getCategory);

            // 3. Save db
            Product saved = repository.save(product);

            log.info("[PRODUCT][CREATE] Success: id={} name={}", saved.getId(), saved.getName());

        } catch (Exception e) {
            log.error("[PRODUCT][FAILED] Error when creating Product : {}", req.getName(), e);
            throw new SystemException("Error when creating Product: " + e.getMessage());
        }
    }

    /**
     * Partial Update
     * <p>
     * Using the feature Hibernate of dirty-checking
     */
    @Override
    @Transactional
    public void partialUpdate(long id, @Nullable ProductPatchRequest req) {

        // Get entity from database
        Product product = productValidator.validateAndGet(id);

        // Check duplicate name ( only the name change and not null)
        if (!Objects.isNull(req.getName()) && !req.getName().equals(product.getName())) {
            productValidator.validateDuplicateName(req.getName());
        }

        // Update Category if its change
        if (!Objects.isNull(req.getCategoryId()) &&
                !product.getCategory().getId().equals(req.getCategoryId())) {
            product.setCategory(categoryValidator.validateAndGet(req.getCategoryId()));
        }

        // MapStruct
        productMapper.updateFrom(req, product);

        log.info("[PRODUCT][UPDATE] id={} updatedBy={}", id, "Someone (TODO)");
    }

    /**
     * Soft Delete
     */
    @Override
    @Transactional
    public void delete(long id) {
        Product product = productValidator.validateAndGet(id);

        // Change status
        product.setStatus(ProductStatus.IN_ACTIVE);

        log.info("[PRODUCT][SOFT_DELETE] id={}", id);
    }

    @Override
    public ProductResponse getById(long id) {
        return productMapper.toResponse(productValidator.validateAndGet(id));
    }

    @Override
    public ProductResponse getByName(String name) {
        return productMapper.toResponse(productValidator.getByName(name));
    }

    @Override
    public PageResponse<ProductResponse> getProducts(
            Long categoryId,
            int page,
            int limit
    ) {
        Pageable pageable = PageRequest.of(
                page,
                limit,
                Sort.by("createdAt").descending()
        );

        Page<Product> products;

        if (categoryId == null) {
            products = repository.findAll(pageable);
        } else {
            products = repository.findByCategory(categoryId, pageable);
        }

        Page<ProductResponse> responsePage = products.map(productMapper::toResponse);

        return PageResponse.from(responsePage);
    }

    /**
     * Search products using dynamic specification filters.
     */
    @Override
    public PageResponse<ProductResponse> search(ProductFilterRequest filter,
                                                Pageable pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Specification<Product> spec = ProductSpecifications.filter(filter);

        Page<ProductResponse> responsePage = repository
                .findAll(spec, pageable).map(productMapper::toResponse);

        return PageResponse.from(responsePage);
    }

    @Override
    public void deleteProductImage(long productId, long imageId) {

    }

    @Override
    public boolean existByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public void updateProductThumbnail(long productId, String imageUrl) {
        Product product = productValidator.validateAndGet(productId);
        product.setThumbnailUrl(imageUrl);
        repository.save(product);
    }

    // ============ HELPER METHODS ============== //

    private Pageable createPageable(Pageable page) {
        if (page.getSort() == null || page.getSort().isEmpty()) {
            return PageRequest.of(
                    page.getPageNumber(), page.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }
        return PageRequest.of(
                page.getPageNumber(), page.getPageSize(), page.getSort()
        );
    }
}

