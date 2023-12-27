package com.example.repositories;

import com.example.entity.Project;
import com.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByTitleAndProjectStorage(String title, Project project);

    List<Task> findAllByProjectStorage(Project project);
}
