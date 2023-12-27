package com.example.util;

import com.example.services.impl.FileService;
import com.example.services.impl.ProjectService;
import com.example.services.impl.TaskService;
import com.example.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIdFileCorrespondence extends UserIdTaskCorrespondence{

    private final FileService fileService;

    @Autowired
    public UserIdFileCorrespondence(UserService userService, ProjectService projectService, TaskService taskService, FileService fileService) {
        super(userService, projectService, taskService);
        this.fileService = fileService;
    }

    public void matchId(int id) {
        super.matchId(fileService.findOne(id).getTaskStorage().getId());
    }
}
