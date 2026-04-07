package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recenter.model.dto.ActivityRequest;
import com.recenter.model.entity.Activity;
import com.recenter.model.entity.User;
import com.recenter.model.enums.ActivityType;
import com.recenter.service.ActivityService;
import com.recenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ActivityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ActivityService activityService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ActivityController activityController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Activity activity;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();

        User user = new User();
        user.setId(10L);
        user.setEmail("test@example.com");

        activity = Activity.builder()
                .id(1L)
                .user(user)
                .type(ActivityType.LOGIN)
                .details("User logged in")
                .build();
    }

    @Test
    void create_ReturnsCreatedActivity() throws Exception {
        ActivityRequest request = new ActivityRequest();
        request.setUserId(10L);
        request.setType(ActivityType.LOGIN);
        request.setDetails("User logged in");

        when(userService.getById(10L)).thenReturn(Optional.of(activity.getUser()));
        when(activityService.create(any(Activity.class))).thenReturn(activity);

        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("LOGIN"))
                .andExpect(jsonPath("$.details").value("User logged in"));

        verify(activityService).create(any(Activity.class));
    }

    @Test
    void getById_Found_ReturnsActivity() throws Exception {
        when(activityService.getById(1L)).thenReturn(Optional.of(activity));

        mockMvc.perform(get("/api/activities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("LOGIN"))
                .andExpect(jsonPath("$.details").value("User logged in"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(activityService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/activities/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_ReturnsList() throws Exception {
        when(activityService.getAll()).thenReturn(List.of(activity));

        mockMvc.perform(get("/api/activities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("LOGIN"));
    }

    @Test
    void getMyActivities_ReturnsUserActivities() throws Exception {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("pass")
                .roles("CLIENT")
                .build();

        TestingAuthenticationToken auth = new TestingAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = activity.getUser();

        when(userService.getByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(activityService.getByUser(user)).thenReturn(List.of(activity));

        mockMvc.perform(get("/api/activities/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("LOGIN"));
    }

    @Test
    void delete_ReturnsSuccessMessage() throws Exception {
        doNothing().when(activityService).delete(1L);

        mockMvc.perform(delete("/api/activities/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Activity deleted successfully"));

        verify(activityService).delete(1L);
    }
}
