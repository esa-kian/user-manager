package com.example.surveypal.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.surveypal.dto.UserDTO;
import com.example.surveypal.dto.UserGroupDTO;
import com.example.surveypal.exception.ResourceNotFoundException;
import com.example.surveypal.model.User;
import com.example.surveypal.model.UserGroup;
import com.example.surveypal.repository.UserGroupRepository;
import com.example.surveypal.repository.UserRepository;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers_ShouldReturnUserList() {
        // Arrange
        List<User> users = Arrays.asList(
                new User(1L, "John Doe", new HashSet<>()),
                new User(2L, "Jane Doe", new HashSet<>())
        );
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDTO> userDTOList = userService.getAllUsers();

        // Assert
        assertEquals(2, userDTOList.size());
        assertEquals("John Doe", userDTOList.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_UserExists_ShouldReturnUser() {
        // Arrange
        User user = new User(1L, "John Doe", new HashSet<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<UserDTO> userDTO = userService.getUserById(1L);

        // Assert
        assertTrue(userDTO.isPresent());
        assertEquals("John Doe", userDTO.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_UserDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<UserDTO> userDTO = userService.getUserById(1L);

        // Assert
        assertFalse(userDTO.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateUser_ShouldReturnSavedUser() {
        // Arrange
        UserDTO userDTO = UserDTO.builder().name("John Doe").build();
        User user = User.builder().name("John Doe").build();
        User savedUser = new User(1L, "John Doe", new HashSet<>());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDTO result = userService.createUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_UserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserDoesNotExist_ShouldThrowException() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });
        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userRepository, never()).deleteById(1L);
    }

    @Test
    void testCreateGroup_ShouldReturnSavedGroup() {
        // Arrange
        UserGroupDTO groupDTO = UserGroupDTO.builder().name("Group A").build();
        UserGroup group = UserGroup.builder().name("Group A").build();
        UserGroup savedGroup = new UserGroup(1L, "Group A", new HashSet<>());

        when(userGroupRepository.save(any(UserGroup.class))).thenReturn(savedGroup);

        // Act
        UserGroupDTO result = userService.createGroup(groupDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Group A", result.getName());
        verify(userGroupRepository, times(1)).save(any(UserGroup.class));
    }

    @Test
    void testAddUserToGroup_ValidUserAndGroup_ShouldAddUserToGroup() {
        // Arrange
        User user = new User(1L, "John Doe", new HashSet<>());
        UserGroup group = new UserGroup(1L, "Group A", new HashSet<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userGroupRepository.findById(1L)).thenReturn(Optional.of(group));

        // Act
        userService.addUserToGroup(1L, 1L);

        // Assert
        assertTrue(group.getUsers().contains(user));
        verify(userGroupRepository, times(1)).save(group);
    }

    @Test
    void testAddUserToGroup_UserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.addUserToGroup(1L, 1L);
        });
        assertEquals("User with id 1 not found", exception.getMessage());
        verify(userGroupRepository, never()).save(any(UserGroup.class));
    }

    @Test
    void testAddUserToGroup_GroupNotFound_ShouldThrowException() {
        // Arrange
        User user = new User(1L, "John Doe", new HashSet<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userGroupRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.addUserToGroup(1L, 1L);
        });
        assertEquals("Group with id 1 not found", exception.getMessage());
        verify(userGroupRepository, never()).save(any(UserGroup.class));
    }
}
