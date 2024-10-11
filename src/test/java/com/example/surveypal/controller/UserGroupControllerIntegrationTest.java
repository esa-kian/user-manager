
package com.example.surveypal.controller;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.surveypal.model.UserGroup;
import com.example.surveypal.repository.UserGroupRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserGroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @BeforeEach
    public void setup() {
        userGroupRepository.deleteAll();
    }

    @Test
    public void testCreateUserGroup() throws Exception {
        String groupJson = "{\"name\": \"Developers\"}";

        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(groupJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Developers")));
    }

    @Test
    public void testGetUserGroupById() throws Exception {
        UserGroup group = new UserGroup();
        group.setName("Admins");
        userGroupRepository.save(group);

        mockMvc.perform(get("/api/groups/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Admins")));
    }
}
