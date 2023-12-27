package com.example.util;

import com.example.entity.User;
import com.example.entity.UserDetailsImpl;
import com.example.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdCorrespondence {

    private final UserService userService;

    @Autowired
    public UserIdCorrespondence(UserService userService) {
        this.userService = userService;
    }

    public void matchId(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {

            if(userDetails.getAuthorities().stream().noneMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                User user = userService.findByUniqueParam(userDetails.getUsername())
                        .orElseThrow(() -> new RuntimeException("Problem with authorization. Try logging in again"));

                if(user.getId() != id){
                    throw new IllegalArgumentException("Operation under another user object");
                }
            }
        } else {
            throw new RuntimeException("Problem with authorization. Try logging in again");
        }
    }
}
