package com.example.services.impl;

import com.example.entity.FileInfo;
import com.example.entity.Task;
import com.example.repositories.FileInfoRepository;
import com.example.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class FileInfoService implements CrudService<FileInfo> {

    @Value("${user.file.location}")
    private String taskFileDir;

    private final FileInfoRepository fileInfoRepository;

    @Autowired
    public FileInfoService(FileInfoRepository fileRepository) {
        this.fileInfoRepository = fileRepository;
    }

    public List<FileInfo> findAll(){
        return fileInfoRepository.findAll();
    }

    public List<FileInfo> findAll(Task task){
        return fileInfoRepository.findByTaskStorage(task);
    }

    public FileInfo findOne(int id){
        return fileInfoRepository.findById(id).orElseThrow();
    }

    public Optional<FileInfo> findByUniquePrams(FileInfo file){
        return fileInfoRepository.findByNameAndTaskStorage(file.getName(), file.getTaskStorage());
    }

    @Transactional
    public void save(FileInfo fileInfo){
        fileInfo.setDir(String.format(taskFileDir, fileInfo.getDir()));
        fileInfoRepository.save(fileInfo);
    }

    @Override
    public void update(int id, FileInfo toUpdate) {
        toUpdate.setId(id);
        fileInfoRepository.save(toUpdate);
    }

    @Transactional
    public void delete(int id){
        fileInfoRepository.deleteById(id);
    }
}
