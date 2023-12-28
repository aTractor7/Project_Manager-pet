package com.example.util.validation.validator;

import com.example.entity.User;
import com.example.services.impl.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UniqueUserValidator extends AbstractValidationStep<User> {

    private final UserService userService;

    @Autowired
    public UniqueUserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if(userService.findByUniqueParam(user.getUsername()).isPresent()) {
            errors.rejectValue("username", "400", "Sorry :(  That username is already used");
        }
        nextStep(target, errors);
    }
}
