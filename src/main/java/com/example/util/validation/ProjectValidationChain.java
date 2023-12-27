package com.example.util.validation;

import com.example.entity.Project;
import com.example.util.validation.validator.AbstractValidationStep;
import org.springframework.validation.Errors;

public class ProjectValidationChain implements ValidationChain<Project>{
    private final AbstractValidationStep<Project> chainHead;

    public ProjectValidationChain(AbstractValidationStep<Project> chainHead) {
        this.chainHead = chainHead;
    }

    @Override
    public void validate(Project toValidate, Errors errors) {
        chainHead.validate(toValidate, errors);
    }
}

