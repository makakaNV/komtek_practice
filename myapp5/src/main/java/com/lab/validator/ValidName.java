package com.lab.validator;

import com.lab.validator.impl.NameValidatorImpl;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidatorImpl.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    String message() default "Поле не может содержать цифры";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

