package com.cloudtask.controller;

import com.cloudtask.dto.AuthResponse;
import com.cloudtask.model.User;
import com.cloudtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        return ResponseEntity.ok(new Object() {
            public final Long id = user.getId();
            public final String username = user.getUsername();
            public final String email = user.getEmail();
            public final String fullName = user.getFullName();
            public final String role = user.getRole();
            public final boolean isActive = user.getIsActive();
        });
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestParam(required = false) String fullName,
                                           @RequestParam(required = false) String email,
                                           Authentication authentication) {
        String username = authentication.getName();
        User updatedUser = userService.updateUserProfile(username, fullName, email);
        
        return ResponseEntity.ok(new Object() {
            public final String username = updatedUser.getUsername();
            public final String email = updatedUser.getEmail();
            public final String fullName = updatedUser.getFullName();
            public final String message = "Profile updated successfully";
        });
    }
}
