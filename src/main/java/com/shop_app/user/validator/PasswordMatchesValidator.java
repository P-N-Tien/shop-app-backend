package com.shop_app.user.validator;

import com.shop_app.user.request.UserRegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements
        ConstraintValidator<PasswordMatches, UserRegisterRequest> {

    public boolean isValid(UserRegisterRequest user, ConstraintValidatorContext context) {

        return user != null
                && user.password() != null
                && user.password().equals(user.confirmPassword());
    }
}
