package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FileInfoDTO {

    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    private int taskId;
}
