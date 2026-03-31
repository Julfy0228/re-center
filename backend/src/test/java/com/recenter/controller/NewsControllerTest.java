package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recenter.model.dto.NewsRequest;
import com.recenter.model.entity.News;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NewsStatus;
import com.recenter.model.enums.UserRole;
import com.recenter.repository.UserRepository;
import com.recenter.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NewsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NewsService newsService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NewsController newsController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User admin;
    private News news;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(newsController)
                .build();

        objectMapper.registerModule(new JavaTimeModule());

        admin = User.builder()
                .id(1L)
                .email("admin@example.com")
                .role(UserRole.ADMIN)
                .build();

        news = News.builder()
                .id(1L)
                .title("Открытие нового домика")
                .content("Мы построили новый домик у озера!")
                .author(admin)
                .status(NewsStatus.PUBLISHED)
                .build();
    }

    // -----------------------------
    // POST /api/news (ADMIN/MANAGER)
    // -----------------------------
    @Test
    void create_AsAdmin_ReturnsCreatedNews() throws Exception {
        NewsRequest request = new NewsRequest();
        request.setTitle("Открытие нового домика");
        request.setContent("Мы построили новый домик у озера!");
        request.setStatus(NewsStatus.PUBLISHED);

        // principal = UserDetails
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password("pass")
                .roles("ADMIN")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));
        when(newsService.create(any(News.class))).thenReturn(news);

        mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Открытие нового домика"));
    }

    // -----------------------------
    // GET /api/news/{id}
    // -----------------------------
    @Test
    void getById_Found_ReturnsNews() throws Exception {
        when(newsService.getById(1L)).thenReturn(Optional.of(news));

        mockMvc.perform(get("/api/news/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Открытие нового домика"));
    }

    @Test
    void getById_NotFound_Returns404() throws Exception {
        when(newsService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/news/1"))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // GET /api/news
    // -----------------------------
    @Test
    void getAll_ReturnsList() throws Exception {
        when(newsService.getAll()).thenReturn(List.of(news));

        mockMvc.perform(get("/api/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Открытие нового домика"));
    }

    // -----------------------------
    // GET /api/news/published
    // -----------------------------
    @Test
    void getPublished_ReturnsPublishedNews() throws Exception {
        when(newsService.getPublished()).thenReturn(List.of(news));

        mockMvc.perform(get("/api/news/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PUBLISHED"));
    }

    // -----------------------------
    // PUT /api/news/{id} (ADMIN/MANAGER)
    // -----------------------------
    @Test
    void update_AsAdmin_ReturnsUpdatedNews() throws Exception {
        NewsRequest request = new NewsRequest();
        request.setTitle("Обновлённая новость");
        request.setContent("Контент обновлён");
        request.setStatus(NewsStatus.DRAFT);

        News updated = News.builder()
                .id(1L)
                .title("Обновлённая новость")
                .content("Контент обновлён")
                .status(NewsStatus.DRAFT)
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password("pass")
                .roles("ADMIN")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        when(newsService.update(eq(1L), any(News.class))).thenReturn(updated);

        mockMvc.perform(put("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Обновлённая новость"));
    }

    @Test
    void update_NotFound_Returns404() throws Exception {
        NewsRequest request = new NewsRequest();
        request.setTitle("Обновлённая новость");
        request.setContent("Контент обновлён");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password("pass")
                .roles("ADMIN")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        when(newsService.update(eq(1L), any(News.class))).thenReturn(null);

        mockMvc.perform(put("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // -----------------------------
    // DELETE /api/news/{id} (ADMIN)
    // -----------------------------
    @Test
    void delete_AsAdmin_ReturnsSuccessMessage() throws Exception {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password("pass")
                .roles("ADMIN")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        doNothing().when(newsService).delete(1L);

        mockMvc.perform(delete("/api/news/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("News deleted successfully"));

        verify(newsService).delete(1L);
    }
}
