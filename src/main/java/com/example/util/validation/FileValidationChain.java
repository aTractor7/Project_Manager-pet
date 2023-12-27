package com.example.util.validation;

import com.example.entity.File;
import com.example.util.validation.validator.AbstractValidationStep;
import org.springframework.validation.Errors;

public class FileValidationChain implements ValidationChain<File>{
    private final AbstractValidationStep<File> chainHead;

    public FileValidationChain(AbstractValidationStep<File> chainHead) {
        this.chainHead = chainHead;
    }

    @Override
    public void validate(File toValidate, Errors errors) {
        chainHead.validate(toValidate, errors);
    }
}
