package com.example.services.impl;

import com.example.entity.Project;
import com.example.entity.Task;
import com.example.entity.User;
import com.example.repositories.TaskRepository;
import com.example.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService implements CrudService<Task> {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public List<Task> findAll(Project project){
        return taskRepository.findAllByProjectStorage(project);
    }

    public Task findOne(int id){
        return taskRepository.findById(id).orElseThrow();
    }

    public Optional<Task> findByUniquePrams(Task task){
        return taskRepository.findByTitleAndProjectStorage(task.getTitle(), task.getProjectStorage());
    }

    @Transactional
    public void save(Task task){
        taskRepository.save(task);
    }

    @Transactional
    public void update(int id, Task updatedTask){
        updatedTask.setId(id);
        taskRepository.save(updatedTask);
    }

    @Transactional
    public void delete(int id){
        taskRepository.deleteById(id);
    }
}
