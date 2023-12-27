package com.example.services.impl;

import com.example.entity.Project;
import com.example.entity.User;
import com.example.entity.enums.Status;
import com.example.repositories.ProjectRepository;
import com.example.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProjectService implements CrudService<Project> {

    private final ProjectRepository projectRepository;


    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public List<Project> findAll(){
        return projectRepository.findAll();
    }

    public List<Project> findAll(User user){
        return projectRepository.findAllByUserStorage(user);
    }

    public Project findOne(int id){
        return projectRepository.findById(id).orElseThrow();
    }

    public Optional<Project> findByUniquePrams(Project project){
        return projectRepository.findByTitleAndUserStorage(project.getTitle(), project.getUserStorage());
    }

    @Transactional
    public void save(Project project){
        project.setStatus(Status.IN_PROCESS);
        projectRepository.save(project);
    }

    @Transactional
    public void update(int id, Project updatedProject){
        updatedProject.setId(id);
        projectRepository.save(updatedProject);
    }

    @Transactional
    public void delete(int id){
        projectRepository.deleteById(id);
    }
}