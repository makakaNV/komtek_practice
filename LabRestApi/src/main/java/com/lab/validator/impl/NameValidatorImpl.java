package com.lab.validator.impl;

import com.lab.validator.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidatorImpl implements ConstraintValidator<ValidName, String> {

    private static final String NAME_REGEX = "^[А-Яа-яA-Za-z\\-]+$";

    @Override
    public void initialize(ValidName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches(NAME_REGEX);
    }
}
