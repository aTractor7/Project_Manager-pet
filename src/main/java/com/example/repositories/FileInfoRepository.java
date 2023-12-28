package com.example.repositories;

import com.example.entity.FileInfo;
import com.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Integer> {
    Optional<FileInfo> findByNameAndTaskStorage(String name, Task task);

    List<FileInfo> findByTaskStorage(Task task);
}