package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "file")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Size(max = 50, message = "File name should be less then 50")
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name="dir")
    private String dir;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task taskStorage;

    public FileInfo(String name, String type, String dir, Task taskStorage) {
        this.name = name;
        this.type = type;
        this.dir = dir;
        this.taskStorage = taskStorage;
    }
}
