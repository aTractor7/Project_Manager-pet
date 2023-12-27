package com.example.util.validation.validator;

import com.example.entity.Task;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
@Order(1)
public class FutureDeadLineValidation extends AbstractValidationStep<Task> {

    @Override
    public boolean supports(Class<?> clazz) {
        return Task.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Task task = (Task) target;

        if(task.getDeadline().isBefore(LocalDateTime.now())) {
            errors.rejectValue("deadline", "400", "Deadline can't be in past");
        }
        nextStep(target, errors);
    }
}
