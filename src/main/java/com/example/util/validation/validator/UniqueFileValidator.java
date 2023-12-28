package com.example.util.validation.validator;

import com.example.entity.FileInfo;
import com.example.services.impl.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UniqueFileValidator extends AbstractValidationStep<FileInfo> {

    private final FileInfoService fileService;

    @Autowired
    public UniqueFileValidator(FileInfoService fileService) {
        this.fileService = fileService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FileInfo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FileInfo fileInfo = (FileInfo) target;

        if(fileService.findByUniquePrams(fileInfo).isPresent()){
            errors.rejectValue("name", "400", "This task is already have file with such name");
        }
        nextStep(target, errors);
    }
}
