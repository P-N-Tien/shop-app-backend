package com.shop_app.role.validator;

import com.shop_app.role.RoleRepository;
import com.shop_app.role.entity.Role;
import com.shop_app.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleValidator {
    private final RoleRepository repository;

    public Role validateAndGet(long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Role not found with id: " + id));
    }
}
