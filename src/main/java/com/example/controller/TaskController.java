package com.example.controller;

import com.example.dto.TaskDTO;
import com.example.entity.Project;
import com.example.entity.Task;
import com.example.services.impl.ProjectService;
import com.example.services.impl.TaskService;
import com.example.util.ErrorResponse;
import com.example.util.UserIdProjectCorrespondence;
import com.example.util.UserIdTaskCorrespondence;
import com.example.util.validation.ValidationChain;
import com.example.util.validation.factory.ValidationFactory;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.util.ErrorUtils.generateFieldErrorMessage;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final ModelMapper modelMapper;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserIdTaskCorrespondence userIdTaskCorrespondence;
    private final UserIdProjectCorrespondence userIdProjectCorrespondence;
    private final ValidationChain<Task> validation;

    @Autowired
    public TaskController(ModelMapper modelMapper, TaskService taskService, ProjectService projectService, UserIdTaskCorrespondence userIdTaskCorrespondence, UserIdProjectCorrespondence userIdProjectCorrespondence, ValidationFactory<Task> validationFactory) {
        this.modelMapper = modelMapper;
        this.taskService = taskService;
        this.projectService = projectService;
        this.userIdTaskCorrespondence = userIdTaskCorrespondence;
        this.userIdProjectCorrespondence = userIdProjectCorrespondence;
        this.validation = validationFactory.createValidationChain();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult){
        Task task = convertToTask(taskDTO);
        validation.validate(task, bindingResult);

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        taskService.save(task);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> edit(@PathVariable int id, @RequestBody @Valid TaskDTO taskDTO, BindingResult bindingResult){
        userIdTaskCorrespondence.matchId(id);

        Task task = convertToTask(taskDTO);
        validation.validate(task, bindingResult);

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        taskService.update(id, task);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id){
        userIdTaskCorrespondence.matchId(id);
        taskService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<TaskDTO> getAll(@RequestParam(name = "projectId") int projectId){
        userIdProjectCorrespondence.matchId(projectId);
        Project project = projectService.findOne(projectId);
        return taskService.findAll(project).stream().map(this::convertToTaskDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TaskDTO getOne(@PathVariable int id){
        userIdTaskCorrespondence.matchId(id);
        return convertToTaskDTO(taskService.findOne(id));
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

    private Task convertToTask(TaskDTO taskDTO){
        Task task = modelMapper.map(taskDTO, Task.class);
        task.setId(0);
        task.setProjectStorage(new Project(taskDTO.getProjectId()));
        return task;
    }

    private TaskDTO convertToTaskDTO(Task task){
        return modelMapper.map(task, TaskDTO.class);
    }
}