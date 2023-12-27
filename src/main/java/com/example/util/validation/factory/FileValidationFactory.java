package com.example.util.validation.factory;

import com.example.entity.File;
import com.example.util.validation.validator.AbstractValidationStep;
import com.example.util.validation.validator.ChainElement;
import com.example.util.validation.FileValidationChain;
import com.example.util.validation.ValidationChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileValidationFactory implements ValidationFactory<File>{

    private final List<AbstractValidationStep<File>> validationSteps;

    @Autowired
    public FileValidationFactory(List<AbstractValidationStep<File>> validationSteps) {
        this.validationSteps = validationSteps;
    }

    @Override
    public ValidationChain<File> createValidationChain() {
        AbstractValidationStep<File> chainHead = ChainElement.buildChain(validationSteps);
        return new FileValidationChain(chainHead);
    }
}
