package com.shop_app.user.validator;

import com.shop_app.shared.exceptions.DuplicateException;
import com.shop_app.shared.exceptions.NotFoundException;
import com.shop_app.user.UserRepository;
import com.shop_app.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {
    private final UserRepository repository;

    public User validateAndGet(long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "User not found with id: " + id));
    }

    public void validateDuplicate(String phone) {
        if (repository.existsByPhoneNumber(phone))
            throw new DuplicateException(
                    "Duplicate user phone number: " + phone);

    }

    public User getByPhoneNumber(String phone) {
        return repository.findUserByPhoneNumber(phone)
                .orElseThrow(() ->
                        new NotFoundException(
                                "User not found with phone number: " + phone));
    }

    public boolean existsByPhone(String phone) {
        return repository.existsByPhoneNumber(phone);
    }
}
