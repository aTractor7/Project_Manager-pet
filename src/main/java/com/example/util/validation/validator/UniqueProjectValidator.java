package com.example.util.validation.validator;

import com.example.entity.Project;
import com.example.services.impl.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UniqueProjectValidator extends AbstractValidationStep<Project> {

    private final ProjectService projectService;


    @Autowired
    public UniqueProjectValidator(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Project.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Project project = (Project) target;

        if(projectService.findByUniquePrams(project).isPresent()) {
            errors.rejectValue("title", "400", "You already have project with this name");
        }
        nextStep(target, errors);
    }
}
