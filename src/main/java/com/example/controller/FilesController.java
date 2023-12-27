package com.example.controller;

import com.example.dto.FileDTO;
import com.example.entity.File;
import com.example.entity.Task;
import com.example.services.impl.FileService;
import com.example.services.impl.TaskService;
import com.example.util.ErrorResponse;
import com.example.util.UserIdFileCorrespondence;
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
@RequestMapping("/file")
public class FilesController {
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final TaskService taskService;
    private final UserIdFileCorrespondence userIdFileCorrespondence;
    private final UserIdTaskCorrespondence userIdTaskCorrespondence;
    private final ValidationChain<File> validation;

    @Autowired
    public FilesController(ModelMapper modelMapper, FileService fileService, TaskService taskService,
                           UserIdFileCorrespondence userIdFileCorrespondence, UserIdTaskCorrespondence userIdTaskCorrespondence,
                           ValidationFactory<File> validationFactory) {
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.taskService = taskService;
        this.userIdFileCorrespondence = userIdFileCorrespondence;
        this.userIdTaskCorrespondence = userIdTaskCorrespondence;
        this.validation = validationFactory.createValidationChain();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid FileDTO fileDTO, BindingResult bindingResult){
        File file = convertToFile(fileDTO);
        validation.validate(file, bindingResult);

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        fileService.save(file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> edit(@PathVariable int id, @RequestBody @Valid FileDTO fileDTO,
                                            BindingResult bindingResult){
        userIdFileCorrespondence.matchId(id);

        File file = convertToFile(fileDTO);
        validation.validate(file, bindingResult);

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        fileService.update(id, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id){
        userIdFileCorrespondence.matchId(id);
        fileService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<FileDTO> getAll(@RequestParam(name = "taskId") int taskId){
        userIdTaskCorrespondence.matchId(taskId);
        Task task = taskService.findOne(taskId);
        return fileService.findAll(task).stream().map(this::convertToFileDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FileDTO getOne(@PathVariable int id){
        userIdFileCorrespondence.matchId(id);
        return convertToFileDTO(fileService.findOne(id));
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

    private File convertToFile(FileDTO fileDTO){
        File file = modelMapper.map(fileDTO, File.class);
        file.setId(0);
        file.setTaskStorage(new Task(fileDTO.getTaskId()));
        return file;
    }

    private FileDTO convertToFileDTO(File file){
        return modelMapper.map(file, FileDTO.class);
    }
}