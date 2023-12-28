package com.example.entity;

import com.example.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Size(max = 100, message = "Less then 100")
    @Column(name = "title")
    private String title;

    @Size(max = 200, min = 10, message = "From 10 to 200 chars")
    @Column(name="description")
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "priority")
    private int priority;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project projectStorage;

    @OneToMany(mappedBy = "taskStorage", cascade = CascadeType.REMOVE)
    private List<FileInfo> fileList;

    public Task(int id) {
        this.id = id;
    }
}