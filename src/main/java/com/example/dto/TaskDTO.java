package com.example.dto;

import com.example.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDateTime;

@Data
@PropertySource("classpath:application-dev.properties")
public class TaskDTO {

    @NotBlank
    @Size(max = 100, message = "Less then 100")
    private String title;

    @NotBlank
    @Size(max = 200, min = 10, message = "From 10 to 200 chars")
    private String description;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime deadline;

    private int priority;

    private int projectId;
}
