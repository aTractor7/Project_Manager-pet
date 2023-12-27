package com.example.util.validation;

import com.example.entity.Task;
import com.example.util.validation.validator.AbstractValidationStep;
import org.springframework.validation.Errors;

public class TaskValidationChain implements ValidationChain<Task>{
    private final AbstractValidationStep<Task> chainHead;

    public TaskValidationChain(AbstractValidationStep<Task> chainHead) {
        this.chainHead = chainHead;
    }

    @Override
    public void validate(Task toValidate, Errors errors) {
        chainHead.validate(toValidate, errors);
    }
}
