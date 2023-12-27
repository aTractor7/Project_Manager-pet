package com.example.dto;

import com.example.entity.enums.Methodology;
import com.example.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectDTO {

    @NotBlank
    @Size(min = 5, max = 50, message = "From 5 to 50")
    private  String title;

    @NotBlank
    @Size(min = 20, max = 200, message = "From 20 to 200")
    private String description;

    @NotNull
    private Status status;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Methodology methodology;
}
