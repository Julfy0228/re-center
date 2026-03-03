package com.recenter.repository;

import com.recenter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository для User Entity
 * Spring Data автоматически генерирует реализацию для всех методов
 */
@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по email
     * @param email адрес электронной почты
     * @return Optional с пользователем, если найден
     */
    Optional<User> findByEmail(String email);

    /**
     * Проверить существование пользователя с заданным email
     * @param email адрес электронной почты
     * @return true если пользователь существует
     */
    boolean existsByEmail(String email);
}
