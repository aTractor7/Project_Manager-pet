package com.example.util.validation.validator;

import com.example.entity.File;
import com.example.services.impl.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UniqueFileValidator extends AbstractValidationStep<File> {

    private final FileService fileService;

    @Autowired
    public UniqueFileValidator(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return File.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        File file = (File) target;

        if(fileService.findByUniquePrams(file).isPresent()){
            errors.rejectValue("name", "400", "This task is already have file with such name");
        }
        nextStep(target, errors);
    }
}
