package com.recenter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.recenter.entity.News;
import com.recenter.entity.Service;
import com.recenter.entity.User;
import com.recenter.repository.NewsRepository;
import com.recenter.repository.ServiceRepository;
import com.recenter.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        try {
            seedUsers();
            seedServices();
            seedNews();
            System.out.println("БД инициализирована успешно");
        } catch (Exception e) {
            System.err.println("Ошибка инициализации БД: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin"), "Администратор", null, "ADMIN");
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
        }

        if (!userRepository.existsByEmail("manager")) {
            User manager = new User("manager", passwordEncoder.encode("manager"), "Менеджер", null, "MANAGER");
            manager.setCreatedAt(LocalDateTime.now());
            userRepository.save(manager);
        }

        if (!userRepository.existsByEmail("ivan@mail.ru")) {
            User client = new User("ivan@mail.ru", passwordEncoder.encode("123"), "Иван", "Петров", "CLIENT");
            client.setPhone("+79991234567");
            client.setCreatedAt(LocalDateTime.now());
            userRepository.save(client);
        }
    }

    private void seedServices() {
        if (serviceRepository.count() == 0) {
            Service service1 = new Service("Коттедж Премиум", "Просторный двухэтажный дом с сауной и камином.", 5000.0, "DAILY", 1, 6);
            serviceRepository.save(service1);

            Service service2 = new Service("Теннисный корт", "Профессиональное покрытие Hard.", 1000.0, "HOURLY", 2, 4);
            serviceRepository.save(service2);
        }
    }

    private void seedNews() {
        if (newsRepository.count() == 0) {
            LocalDateTime now = LocalDateTime.now();
            News news1 = new News();
            news1.setTitle("Открытие летнего сезона!");
            news1.setContent("Мы рады сообщить, что с 1 мая база отдыха переходит на летний режим работы.");
            news1.setPublicationDate(now);
            newsRepository.save(news1);

            News news2 = new News();
            news2.setTitle("Скидки для больших групп");
            news2.setContent("При бронировании 3-х и более коттеджей скидка 15%.");
            news2.setPublicationDate(now);
            newsRepository.save(news2);
        }
    }
}