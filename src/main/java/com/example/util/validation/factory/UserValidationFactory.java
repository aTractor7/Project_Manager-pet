package com.example.util.validation.factory;

import com.example.entity.User;
import com.example.util.validation.*;
import com.example.util.validation.validator.AbstractValidationStep;
import com.example.util.validation.validator.ChainElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserValidationFactory implements ValidationFactory<User>{

    private final List<AbstractValidationStep<User>> validationSteps;

    @Autowired
    public UserValidationFactory(List<AbstractValidationStep<User>> validationSteps) {
        this.validationSteps = validationSteps;
    }

    @Override
    public ValidationChain<User> createValidationChain() {
        AbstractValidationStep<User> chainHead = ChainElement.buildChain(validationSteps);
        return new UserValidationChain(chainHead);
    }
}
