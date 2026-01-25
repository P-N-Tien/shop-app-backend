package com.shop_app.product;

import com.shop_app.category.validator.CategoryValidator;
import com.shop_app.product.enums.ProductStatus;
import com.shop_app.product.images.IProductImageService;
import com.shop_app.product.images.ProductImageServiceImpl;
import com.shop_app.product.mapper.ProductMapper;
import com.shop_app.product.request.ProductCreateRequest;
import com.shop_app.product.request.ProductFilterRequest;
import com.shop_app.product.request.ProductPatchRequest;
import com.shop_app.product.response.ProductResponse;
import com.shop_app.product.specification.ProductSpecifications;
import com.shop_app.product.validator.ProductValidator;
import com.shop_app.category.entity.Category;
import com.shop_app.product.entity.Product;
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

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryValidator categoryValidator;
    private final ProductValidator productValidator;
    private final ProductMapper productMapper;
    private final IProductImageService productImageService;

    /**
     * Creates a product and its related metadata atomically.
     * <p>
     * Transaction scope:
     * - Product and image metadata are persisted together.
     * - File upload is executed asynchronously AFTER commit.
     * </p>
     */
    @Override
    @Transactional
    public void create(ProductCreateRequest req) {
        Validate.requiredNonNull(req, "Product request must be not null");

        productValidator.validateDuplicateName(req.getName());

        Category category = categoryValidator.validateAndGet(req.getCategoryId());

        // MapStruct
        Product product = productMapper.createFrom(req);
        product.setCategory(category);

        Product saved = productRepository.saveAndFlush(product);

        // Persist image metadata inside transaction.
        // Actual file upload is deferred and triggered AFTER commit
        // to avoid orphan files in case of rollback.
        productImageService.save(req.getFiles(), saved);

        // handle upload images after transaction commit
        productImageService.handleUploadImages(req.getFiles(), saved);

        log.info("[PRODUCT][CREATE] id={} name={}", saved.getId(), saved.getName());
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

    /**
     * Search products using dynamic specification filters.
     */
    @Override
    public Page<ProductResponse> search(ProductFilterRequest filter,
                                        Pageable pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Specification<Product> spec = ProductSpecifications.filter(filter);

        return productRepository.findAll(spec, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
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

    public List<Product> findAllByIdInList(List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
}

