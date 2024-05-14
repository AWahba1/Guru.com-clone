package com.guru.jobservice.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUUIDListValidator.class)
public @interface ValidUUIDList {
    String message() default "Invalid UUID format in the list";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

