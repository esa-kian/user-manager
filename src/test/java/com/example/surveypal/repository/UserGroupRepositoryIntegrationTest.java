package com.example.surveypal.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.surveypal.model.UserGroup;

@DataJpaTest
public class UserGroupRepositoryIntegrationTest {

    @Autowired
    private UserGroupRepository userGroupRepository;

    @BeforeEach
    public void setup() {
        userGroupRepository.deleteAll();
    }

    @Test
    public void testSaveUserGroup() {
        UserGroup group = new UserGroup();
        group.setName("Admin Group");
        UserGroup savedGroup = userGroupRepository.save(group);

        assertNotNull(savedGroup.getId());
        assertEquals("Admin Group", savedGroup.getName());
    }

    @Test
    public void testFindUserGroupById() {
        UserGroup group = new UserGroup();
        group.setName("Users Group");
        UserGroup savedGroup = userGroupRepository.save(group);

        Optional<UserGroup> foundGroup = userGroupRepository.findById(savedGroup.getId());
        assertTrue(foundGroup.isPresent());
        assertEquals("Users Group", foundGroup.get().getName());
    }

    @Test
    public void testDeleteUserGroup() {
        UserGroup group = new UserGroup();
        group.setName("Test Group");
        UserGroup savedGroup = userGroupRepository.save(group);

        userGroupRepository.deleteById(savedGroup.getId());
        Optional<UserGroup> deletedGroup = userGroupRepository.findById(savedGroup.getId());

        assertFalse(deletedGroup.isPresent());
    }
}
