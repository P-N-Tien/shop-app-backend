package com.shop_app.user;

import com.shop_app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByPhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phone);
}
