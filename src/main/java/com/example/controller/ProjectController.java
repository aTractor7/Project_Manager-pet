package com.example.controller;

import com.example.dto.ProjectDTO;
import com.example.entity.Project;
import com.example.entity.UserDetailsImpl;
import com.example.services.impl.ProjectService;
import com.example.util.ErrorResponse;
import com.example.util.UserIdProjectCorrespondence;
import com.example.util.validation.ValidationChain;
import com.example.util.validation.factory.ValidationFactory;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.util.ErrorUtils.generateFieldErrorMessage;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ModelMapper modelMapper;
    private final ProjectService projectService;
    private final UserIdProjectCorrespondence userIdProjectCorrespondence;
    private final ValidationChain<Project> validation;

    @Autowired
    public ProjectController(ModelMapper modelMapper, ProjectService projectService, UserIdProjectCorrespondence userIdProjectCorrespondence, ValidationFactory<Project> validationFactory) {
        this.modelMapper = modelMapper;
        this.projectService = projectService;
        this.userIdProjectCorrespondence = userIdProjectCorrespondence;
        this.validation = validationFactory.createValidationChain();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> edit(@PathVariable int id, @RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult){
        userIdProjectCorrespondence.matchId(id);

        Project project = convertToProject(projectDTO);
        validation.validate(project, bindingResult);

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        projectService.update(id, project);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id){
        userIdProjectCorrespondence.matchId(id);
        projectService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<ProjectDTO> getAll(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return projectService.findAll(userDetails.user()).stream().map(this::convertToProjectDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProjectDTO getOne(@PathVariable int id){
        userIdProjectCorrespondence.matchId(id);
        return convertToProjectDTO(projectService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ProjectDTO projectDTO, BindingResult bindingResult,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        Project project = convertToProject(projectDTO);
        validation.validate(project, bindingResult);

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        project.setUserStorage(userDetails.user());

        projectService.save(project);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIllegalArgExc(IllegalArgumentException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleRuntimeExc(RuntimeException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIllegalArgExc(NoSuchElementException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private Project convertToProject(ProjectDTO projectDTO){
        return modelMapper.map(projectDTO, Project.class);
    }

    private ProjectDTO convertToProjectDTO(Project project){
        return modelMapper.map(project, ProjectDTO.class);
    }
}