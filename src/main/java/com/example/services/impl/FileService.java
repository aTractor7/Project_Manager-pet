package com.example.services.impl;

import com.example.entity.File;
import com.example.entity.Project;
import com.example.entity.Task;
import com.example.repositories.FileRepository;
import com.example.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FileService implements CrudService<File> {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> findAll(){
        return fileRepository.findAll();
    }

    public List<File> findAll(Task task){
        return fileRepository.findByTaskStorage(task);
    }

    public File findOne(int id){
        return fileRepository.findById(id).orElseThrow();
    }

    public Optional<File> findByUniquePrams(File file){
        return fileRepository.findByNameAndTaskStorage(file.getName(), file.getTaskStorage());
    }

    @Transactional
    public void save(File file){
        //TODO: delete in future
        file.setPath("some path");

        fileRepository.save(file);
    }

    @Transactional
    public void update(int id, File updatedFile){
        updatedFile.setId(id);
        fileRepository.save(updatedFile);
    }

    @Transactional
    public void delete(int id){
        fileRepository.deleteById(id);
    }
}
