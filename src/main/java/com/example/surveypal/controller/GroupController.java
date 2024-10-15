package com.example.surveypal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.surveypal.dto.UserGroupDTO;
import com.example.surveypal.service.UserService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final UserService userService;

    @Autowired
    public GroupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGroupDTO> getGroupById(@PathVariable Long id) {
        return userService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserGroupDTO> createGroup(@RequestBody UserGroupDTO groupDTO) {
        return ResponseEntity.ok(userService.createGroup(groupDTO));
    }

    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<UserGroupDTO> createUser(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.addUserToGroup(userId, groupId));
    }

    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<UserGroupDTO> deleteUser(@PathVariable Long groupId, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.removeUserFromGroup(userId, groupId));
    }
}
