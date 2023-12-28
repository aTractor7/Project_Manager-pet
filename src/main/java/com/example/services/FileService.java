package com.example.services;

import com.example.entity.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FileService {

    public void saveFile(FileInfo fileInfo, MultipartFile multipartFile) throws IOException {
        File dir = new File(fileInfo.getDir());
        if(!dir.exists()) {
            if(!dir.mkdirs()) {
                throw new RuntimeException("Can't make dir");
            }
        }

        File file = new File(getFilePath(fileInfo));

        FileCopyUtils.copy(multipartFile.getInputStream(), Files.newOutputStream(file.toPath()));
    }

    public File getFile(FileInfo fileInfo){
        File file = new File(getFilePath(fileInfo));
        if(!file.exists())
            throw new RuntimeException("Problem with file path");
        return file;
    }

    public void delete(FileInfo fileInfo) throws IOException {
        File file = new File(getFilePath(fileInfo));
        Files.delete(file.toPath());

    }

    private String getFilePath(FileInfo fileInfo) {
        return fileInfo.getDir() + "/" + fileInfo.getName();
    }
}
