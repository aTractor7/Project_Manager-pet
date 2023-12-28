package com.example.util.validation;

import com.example.entity.FileInfo;
import com.example.util.validation.validator.AbstractValidationStep;
import org.springframework.validation.Errors;

public class FileValidationChain implements ValidationChain<FileInfo>{
    private final AbstractValidationStep<FileInfo> chainHead;

    public FileValidationChain(AbstractValidationStep<FileInfo> chainHead) {
        this.chainHead = chainHead;
    }

    @Override
    public void validate(FileInfo toValidate, Errors errors) {
        chainHead.validate(toValidate, errors);
    }
}
