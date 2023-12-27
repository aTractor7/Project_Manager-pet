package com.example.util.validation.validator;

import com.example.entity.Task;
import com.example.services.impl.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@Order(0)
public class UniqueTaskValidator extends AbstractValidationStep<Task> {

    private final TaskService taskService;

    @Autowired
    public UniqueTaskValidator(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Task task = (Task) target;

        if(taskService.findByUniquePrams(task).isPresent()) {
            errors.rejectValue("title", "400", "This project already have task with this name");
        }
        nextStep(target, errors);
    }
}
