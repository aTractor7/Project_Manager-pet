package com.example.controller;

import com.example.dto.FileInfoDTO;
import com.example.entity.Task;
import com.example.services.FileService;
import com.example.services.impl.FileInfoService;
import com.example.services.impl.TaskService;
import com.example.util.ErrorResponse;
import com.example.entity.FileInfo;
import com.example.util.UserIdFileCorrespondence;
import com.example.util.UserIdTaskCorrespondence;
import com.example.util.validation.ValidationChain;
import com.example.util.validation.factory.ValidationFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.util.ErrorUtils.generateFieldErrorMessage;

@RestController
@RequestMapping("/file")
public class FilesController {
    private final ModelMapper modelMapper;
    private final FileInfoService fileInfoService;
    private final FileService fileService;
    private final TaskService taskService;
    private final UserIdFileCorrespondence userIdFileCorrespondence;
    private final UserIdTaskCorrespondence userIdTaskCorrespondence;
    private final ValidationChain<FileInfo> validation;

    @Autowired
    public FilesController(ModelMapper modelMapper, FileInfoService fileInfoService, FileService fileService, TaskService taskService,
                           UserIdFileCorrespondence userIdFileCorrespondence, UserIdTaskCorrespondence userIdTaskCorrespondence,
                           ValidationFactory<FileInfo> validationFactory) {
        this.modelMapper = modelMapper;
        this.fileInfoService = fileInfoService;
        this.fileService = fileService;
        this.taskService = taskService;
        this.userIdFileCorrespondence = userIdFileCorrespondence;
        this.userIdTaskCorrespondence = userIdTaskCorrespondence;
        this.validation = validationFactory.createValidationChain();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestParam("file") MultipartFile multipartFile,
                                             @RequestParam("taskId") int taskId,
                                             @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        FileInfo fileInfo = new FileInfo(multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                userDetails.getUsername() + taskId, new Task(taskId));

        BindingResult bindingResult =  new BeanPropertyBindingResult(fileInfo, "FileInfo");

        validation.validate(fileInfo, bindingResult);

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        fileInfoService.save(fileInfo);
        fileService.saveFile(fileInfo, multipartFile);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping ("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) throws IOException {
        userIdFileCorrespondence.matchId(id);
        fileService.delete(fileInfoService.findOne(id));
        fileInfoService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public List<FileInfoDTO> getAll(@RequestParam(name = "taskId") int taskId){
        userIdTaskCorrespondence.matchId(taskId);
        Task task = taskService.findOne(taskId);
        return fileInfoService.findAll(task).stream().map(this::convertToFileInfoDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getOne(@PathVariable int id){
        userIdFileCorrespondence.matchId(id);

        FileInfo fileInfo = fileInfoService.findOne(id);
        File file = fileService.getFile(fileInfo);

        try{
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.valueOf(fileInfo.getType()))
                    .body(resource);

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Something went wrong. Try later.");
        }
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

    private FileInfo convertToFileInfo(FileInfoDTO fileDTO){
        FileInfo fileInfo = modelMapper.map(fileDTO, FileInfo.class);
        fileInfo.setTaskStorage(new Task(fileDTO.getTaskId()));
        return fileInfo;
    }

    private FileInfoDTO convertToFileInfoDTO(FileInfo fileInfo){
        return modelMapper.map(fileInfo, FileInfoDTO.class);
    }
}