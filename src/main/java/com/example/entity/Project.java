package com.example.entity;

import com.example.entity.enums.Methodology;
import com.example.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Size(min = 5, max = 50, message = "From 5 to 50")
    @Column(name = "title")
    private  String title;

    @NotNull
    @Size(min = 20, max = 200, message = "From 20 to 200")
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "methodology")
    private Methodology methodology;

    @OneToMany(mappedBy = "projectStorage", cascade = CascadeType.REMOVE)
    private List<Task> taskList;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userStorage;

    public Project(int id) {
        this.id = id;
    }
}