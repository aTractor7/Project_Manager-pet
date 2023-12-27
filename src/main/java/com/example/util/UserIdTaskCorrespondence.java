package com.example.util;

import com.example.services.impl.ProjectService;
import com.example.services.impl.TaskService;
import com.example.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIdTaskCorrespondence extends UserIdProjectCorrespondence{

    private final TaskService taskService;

    @Autowired
    public UserIdTaskCorrespondence(UserService userService, ProjectService projectService, TaskService taskService) {
        super(userService, projectService);
        this.taskService = taskService;
    }

    public void matchId(int id) {
        super.matchId(taskService.findOne(id).getProjectStorage().getId());
    }
}
