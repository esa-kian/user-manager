package com.example.surveypal.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.surveypal.dto.UserDTO;
import com.example.surveypal.dto.UserGroupDTO;
import com.example.surveypal.exception.ResourceNotFoundException;
import com.example.surveypal.model.User;
import com.example.surveypal.model.UserGroup;
import com.example.surveypal.repository.UserGroupRepository;
import com.example.surveypal.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserGroupRepository userGroupRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder().id(user.getId()).name(user.getName()).build())
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> UserDTO.builder().id(user.getId()).name(user.getName()).build());
    }

    public Optional<UserGroupDTO> getGroupById(Long id) {
        return userGroupRepository.findById(id)
                .map(group -> {
                    Set<UserDTO> userDTOs = group.getUsers().stream()
                            .map(user -> UserDTO.builder().id(user.getId()).name(user.getName()).build())
                            .collect(Collectors.toSet());
                    return UserGroupDTO.builder()
                            .id(group.getId())
                            .name(group.getName())
                            .users(userDTOs) // Add users to the DTO
                            .build();
                });
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = User.builder().name(userDTO.getName()).build();
        User savedUser = userRepository.save(user);
        return UserDTO.builder().id(savedUser.getId()).name(savedUser.getName()).build();
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserGroupDTO createGroup(UserGroupDTO groupDTO) {
        UserGroup group = UserGroup.builder().name(groupDTO.getName()).build();
        UserGroup savedGroup = userGroupRepository.save(group);
        return UserGroupDTO.builder().id(savedGroup.getId()).name(savedGroup.getName()).build();
    }

    @Transactional
    public void addUserToGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group with id " + groupId + " not found"));
        group.getUsers().add(user);
        userGroupRepository.save(group);
    }
}
