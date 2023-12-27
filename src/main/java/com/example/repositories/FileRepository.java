package com.example.repositories;

import com.example.entity.File;
import com.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    Optional<File> findByNameAndTaskStorage(String name, Task task);

    List<File> findByTaskStorage(Task task);
}