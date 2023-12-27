package com.example.util.validation;

import com.example.entity.User;
import com.example.util.validation.validator.AbstractValidationStep;
import org.springframework.validation.Errors;


public class UserValidationChain implements ValidationChain<User>{
    private final AbstractValidationStep<User> chainHead;

    public UserValidationChain(AbstractValidationStep<User> chainHead) {
        this.chainHead = chainHead;
    }

    @Override
    public void validate(User toValidate, Errors errors) {
        chainHead.validate(toValidate, errors);
    }
}

