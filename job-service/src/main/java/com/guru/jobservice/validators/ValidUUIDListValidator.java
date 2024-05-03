package com.guru.jobservice.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidUUIDListValidator implements ConstraintValidator<ValidUUIDList, String[]> {

    private static final Pattern UUID_PATTERN = Pattern.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");

    @Override
    public boolean isValid(String[] values, ConstraintValidatorContext context) {
        if (values == null) {
            return true;  // null values are considered valid
        }
        for (String value : values) {
            if (value == null || !UUID_PATTERN.matcher(value).matches()) {
                return false;  // Invalid UUID format
            }
        }
        return true;
    }
}

