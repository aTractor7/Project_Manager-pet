package com.example.util.validation.factory;

import com.example.entity.Project;
import com.example.util.validation.validator.AbstractValidationStep;
import com.example.util.validation.validator.ChainElement;
import com.example.util.validation.ProjectValidationChain;
import com.example.util.validation.ValidationChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectValidationFactory implements ValidationFactory<Project> {

    private final List<AbstractValidationStep<Project>> validationSteps;

    @Autowired
    public ProjectValidationFactory(List<AbstractValidationStep<Project>> validationSteps) {
        this.validationSteps = validationSteps;
    }

    @Override
    public ValidationChain<Project> createValidationChain() {
        AbstractValidationStep<Project> chainHead = ChainElement.buildChain(validationSteps);
        return new ProjectValidationChain(chainHead);
    }
}
