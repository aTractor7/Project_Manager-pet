package com.example.util;

import com.example.services.impl.ProjectService;
import com.example.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIdProjectCorrespondence extends UserIdCorrespondence{

    private final ProjectService projectService;

    @Autowired
    public UserIdProjectCorrespondence(UserService userService, ProjectService projectService) {
        super(userService);
        this.projectService = projectService;
    }

    public void matchId(int id) {
        super.matchId(projectService.findOne(id).getUserStorage().getId());
    }
}
