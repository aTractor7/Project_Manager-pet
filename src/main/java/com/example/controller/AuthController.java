package com.example.controller;

import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.services.impl.UserService;
import com.example.util.ErrorResponse;
import com.example.util.validation.ValidationChain;
import com.example.util.validation.factory.ValidationFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.util.ErrorUtils.generateFieldErrorMessage;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    private final ValidationChain<User> validation;

    @Autowired
    public AuthController(UserService userService, ModelMapper modelMapper, ValidationFactory<User> validationFactory) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.validation = validationFactory.createValidationChain();
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDTO> registration(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        validation.validate(user, bindingResult);

        if(bindingResult.hasErrors()) {
            throw new IllegalArgumentException(generateFieldErrorMessage(bindingResult.getFieldErrors()));
        }

        userService.save(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
