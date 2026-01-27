package com.shop_app.role;

import com.shop_app.role.entity.Role;
import com.shop_app.role.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

    Optional<Role> findByName(UserRole name);
}
