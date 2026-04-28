package com.recenter.controller;

import com.recenter.mapper.EntityDtoMapper;
import com.recenter.model.dto.NewsRequest;
import com.recenter.model.dto.NewsResponse;
import com.recenter.model.dto.PageResponse;
import com.recenter.model.entity.News;
import com.recenter.model.entity.User;
import com.recenter.model.enums.NewsStatus;
import com.recenter.service.NewsService;
import com.recenter.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<NewsResponse> create(@Valid @RequestBody NewsRequest request) {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User author = userService.getByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        News news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .author(author)
                .status(request.getStatus() != null ? request.getStatus() : NewsStatus.DRAFT)
                .build();

        News created = newsService.create(news);
        return ResponseEntity.ok(EntityDtoMapper.toNewsResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> getById(@PathVariable("id") Long id) {
        return newsService.getById(id)
                .map(news -> ResponseEntity.ok(EntityDtoMapper.toNewsResponse(news)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<NewsResponse>> getAll() {
        List<NewsResponse> responses = newsService.getAll().stream()
                .map(EntityDtoMapper::toNewsResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/published")
    public ResponseEntity<PageResponse<NewsResponse>> getPublished(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size) {
        var newsPage = newsService.getPublishedPage(page, size);

        PageResponse<NewsResponse> response = PageResponse.<NewsResponse>builder()
                .items(newsPage.getContent().stream().map(EntityDtoMapper::toNewsResponse).toList())
                .page(newsPage.getNumber())
                .size(newsPage.getSize())
                .totalItems(newsPage.getTotalElements())
                .totalPages(newsPage.getTotalPages())
                .first(newsPage.isFirst())
                .last(newsPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<NewsResponse> update(@PathVariable("id") Long id, @Valid @RequestBody NewsRequest request) {
        News newsDetails = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .status(request.getStatus())
                .build();

        News updated = newsService.update(id, newsDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityDtoMapper.toNewsResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        newsService.delete(id);
        return ResponseEntity.ok("News deleted successfully");
    }
}
