package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Size(min = 2, max = 100, message = "From 2 to 100")
    @Column(name = "username")
    private String username;

    @Column(name = "role")
    private String role;

    @NotBlank
    @Size(min = 8, message = "From 8")
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "userStorage", cascade = CascadeType.REMOVE)
    private List<Project> projectList;
}