package com.example.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    private int id;

    @NotBlank
    @Size(min = 2, max = 100, message = "From 2 to 100")
    private String username;

    @NotBlank
    @Size(min = 8, max = 20, message = "From 8 to 20")
    private String password;
}
