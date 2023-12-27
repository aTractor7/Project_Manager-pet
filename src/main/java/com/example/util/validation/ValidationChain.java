package com.example.util.validation;

import com.example.entity.Task;
import org.springframework.validation.Errors;

public interface ValidationChain<T> {
    void validate(T toValidate, Errors errors);
}
