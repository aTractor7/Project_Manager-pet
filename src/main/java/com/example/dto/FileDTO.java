package com.example.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileDTO {
    @NotBlank
    private String name;

    private int taskId;
}
