package com.example.util.validation.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class AbstractValidationStep<T> implements ChainElement<AbstractValidationStep<T>>, Validator {

    private AbstractValidationStep<T> next;

    @Override
    public final void setNext(AbstractValidationStep<T> step) {
        this.next = step;
    }

    protected void nextStep(Object target, Errors errors) {
        if(next == null) return;

        next.validate(target, errors);
    }
}
