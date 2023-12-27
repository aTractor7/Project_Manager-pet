package com.example.util.validation.factory;

import com.example.util.validation.ValidationChain;

public interface ValidationFactory<T> {
    ValidationChain<T> createValidationChain();
}
