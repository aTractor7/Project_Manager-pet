package com.example.util.validation.factory;

import com.example.entity.FileInfo;
import com.example.util.validation.validator.AbstractValidationStep;
import com.example.util.validation.validator.ChainElement;
import com.example.util.validation.FileValidationChain;
import com.example.util.validation.ValidationChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileValidationFactory implements ValidationFactory<FileInfo>{

    private final List<AbstractValidationStep<FileInfo>> validationSteps;

    @Autowired
    public FileValidationFactory(List<AbstractValidationStep<FileInfo>> validationSteps) {
        this.validationSteps = validationSteps;
    }

    @Override
    public ValidationChain<FileInfo> createValidationChain() {
        AbstractValidationStep<FileInfo> chainHead = ChainElement.buildChain(validationSteps);
        return new FileValidationChain(chainHead);
    }
}
