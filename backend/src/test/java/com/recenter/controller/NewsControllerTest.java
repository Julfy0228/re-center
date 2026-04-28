package com.recenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recenter.model.dto.NewsRequest;
import com.recenter.model.entity.News;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NewsStatus;
import com.recenter.model.enums.UserRole;
import com.recenter.service.NewsService;
import com.recenter.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NewsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NewsService newsService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NewsController newsController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User admin;
    private News news;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(newsController).build();
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

    @Test
    void create_AsAdmin_ReturnsCreatedNews() throws Exception {
        NewsRequest request = new NewsRequest();
        request.setTitle("Открытие нового домика");
        request.setContent("Мы построили новый домик у озера!");
        request.setStatus(NewsStatus.PUBLISHED);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password("pass")
                .roles("ADMIN")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userService.getByEmail(admin.getEmail())).thenReturn(Optional.of(admin));
        when(newsService.create(any(News.class))).thenReturn(news);

        mockMvc.perform(post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Открытие нового домика"));
    }

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

    @Test
    void getAll_ReturnsList() throws Exception {
        when(newsService.getAll()).thenReturn(List.of(news));

        mockMvc.perform(get("/api/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Открытие нового домика"));
    }

    @Test
    void getPublished_ReturnsPaginatedNews() throws Exception {
        when(newsService.getPublishedPage(0, 6))
                .thenReturn(new PageImpl<>(List.of(news), PageRequest.of(0, 6), 1));

        mockMvc.perform(get("/api/news/published"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].status").value("PUBLISHED"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

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
                new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

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
                new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        when(newsService.update(eq(1L), any(News.class))).thenReturn(null);

        mockMvc.perform(put("/api/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_AsAdmin_ReturnsSuccessMessage() throws Exception {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(admin.getEmail())
                .password("pass")
                .roles("ADMIN")
                .build();

        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        doNothing().when(newsService).delete(1L);

        mockMvc.perform(delete("/api/news/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("News deleted successfully"));

        verify(newsService).delete(1L);
    }
}
