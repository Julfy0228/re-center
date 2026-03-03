package com.recenter.repository;

import com.recenter.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA Repository для News Entity
 * Spring Data автоматически генерирует реализацию для всех методов
 */
@Repository
public interface NewsJpaRepository extends JpaRepository<News, Long> {

    /**
     * Найти новости, опубликованные после заданной даты
     * @param date дата
     * @return список новостей, отсортированный по дате публикации
     */
    List<News> findByPublicationDateAfterOrderByPublicationDateDesc(LocalDateTime date);

    /**
     * Найти все новости отсортированные по дате в обратном порядке (новые первыми)
     * @return список новостей
     */
    List<News> findAllByOrderByPublicationDateDesc();
}
