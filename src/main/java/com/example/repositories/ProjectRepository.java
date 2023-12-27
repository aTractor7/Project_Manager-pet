package com.example.repositories;


import com.example.entity.Project;
import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findByTitleAndUserStorage(String title, User user);

    List<Project> findAllByUserStorage(User user);
}