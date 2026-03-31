package com.recenter.controller;

import com.recenter.model.dto.NewsRequest;
import com.recenter.model.entity.News;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NewsStatus;
import com.recenter.repository.UserRepository;
import com.recenter.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
/**
 * REST контроллер для управления новостями и объявлениями.
 * 
 * Предоставляет API endpoints для создания, чтения, обновления и удаления новостей.
 * Требует роль ADMIN или MANAGER для изменения новостей.
 */
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Создает новую новость (требует ADMIN или MANAGER).
     * 
     * @param request DTO с данными новости
     * @return созданная новость
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> create(@Valid @RequestBody NewsRequest request) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        News news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .status(request.getStatus() != null ? request.getStatus() : NewsStatus.DRAFT)
                .build();

        News created = newsService.create(news);
        return ResponseEntity.ok(created);
    }

    /**
     * Получает новость по идентификатору.
     * 
     * @param id идентификатор новости
     * @return новость или 404
     */
    /**
     * Получает все новости.
     * 
     * @return список всех новостей
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<News> news = newsService.getById(id);
        return news.isPresent() ? ResponseEntity.ok(news.get()) : ResponseEntity.notFound().build();
    }

    @GetMapping
    /**
     * Получает опубликованные новости.
     * 
     * @return список опубликованных новостей в обратном хронологическом порядке
     */
    public ResponseEntity<?> getAll() {
        List<News> news = newsService.getAll();
        return ResponseEntity.ok(news);
    }

    @GetMapping("/published")
    /**
     * Обновляет новость (требует ADMIN или MANAGER).
     * 
     * @param id идентификатор новости
     * @param request новые данные новости
     * @return обновленная новость или 404
     */
    public ResponseEntity<?> getPublished() {
        List<News> news = newsService.getPublished();
        return ResponseEntity.ok(news);
    }

    @PutMapping("/{id}")
    /**
     * Удаляет новость (требует ADMIN).
     * 
     * @param id идентификатор новости
     * @return сообщение об успешном удалении
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody NewsRequest request) {
        News newsDetails = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(request.getStatus())
                .build();

        News updated = newsService.update(id, newsDetails);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        newsService.delete(id);
        return ResponseEntity.ok("News deleted successfully");
    }
}
