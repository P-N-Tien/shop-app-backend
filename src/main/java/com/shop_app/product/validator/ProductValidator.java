package com.shop_app.product.validator;

import com.shop_app.product.entity.Product;
import com.shop_app.product.ProductRepository;
import com.shop_app.shared.exceptions.DuplicateException;
import com.shop_app.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final ProductRepository repository;

    public Product validateAndGet(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    public Product getByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Product not found with name: " + name));
    }

    public void validateDuplicateName(String name) {
        if (repository.existsByName(name)) {
            throw new DuplicateException("Product already exists with name: " + name);
        }
    }
}
