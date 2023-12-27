package com.example.util.validation.factory;

import com.example.entity.Task;
import com.example.util.validation.validator.AbstractValidationStep;
import com.example.util.validation.validator.ChainElement;
import com.example.util.validation.TaskValidationChain;
import com.example.util.validation.ValidationChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskValidationFactory implements ValidationFactory<Task> {

    private final List<AbstractValidationStep<Task>> validationSteps;

    @Autowired
    public TaskValidationFactory(List<AbstractValidationStep<Task>> validationSteps) {
        this.validationSteps = validationSteps;
    }

    @Override
    public ValidationChain<Task> createValidationChain() {
        AbstractValidationStep<Task> chainHead = ChainElement.buildChain(validationSteps);
        return new TaskValidationChain(chainHead);
    }
}
