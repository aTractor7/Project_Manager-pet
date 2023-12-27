package com.example.controller;

import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.services.impl.UserService;


import com.example.util.ErrorResponse;
import com.example.util.UserIdCorrespondence;
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
@RequestMapping("/user")
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserIdCorrespondence userIdCorrespondence;
    private final ValidationChain<User> validation;

    @Autowired
    public UserController(ModelMapper modelMapper, UserService userService, UserIdCorrespondence userIdCorrespondence,
                          ValidationFactory<User> validationFactory) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userIdCorrespondence = userIdCorrespondence;
        this.validation = validationFactory.createValidationChain();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable int id, @RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {
        userIdCorrespondence.matchId(id);

        User user = convertToUser(userDTO);
        if(!userService.findOne(id).getUsername().equals(user.getUsername())) {
            validation.validate(user, bindingResult);
        }

        if(bindingResult.hasErrors()) {
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        userService.update(id, user);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id){
        userIdCorrespondence.matchId(id);
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping()
    public List<UserDTO> getAll(){
        return userService.findAll().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getOne(@PathVariable int id){
        userIdCorrespondence.matchId(id);
        return convertToUserDTO(userService.findOne(id));
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

    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
