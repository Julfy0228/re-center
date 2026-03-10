package com.recenter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.recenter.entity.*;
import com.recenter.repository.*;

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
    private CategoryRepository categoryRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        try {
            seedUsers();
            seedCategories();
            seedServices();
            seedDiscounts();
            seedNews();
            System.out.println("БД инициализирована успешно!");
        } catch (Exception e) {
            System.err.println("Ошибка инициализации БД: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = new User("admin@test.com", passwordEncoder.encode("admin"), "Иван", "Иванов", "ADMIN");
            userRepository.save(admin);
        }

        if (!userRepository.existsByEmail("manager@test.com")) {
            User manager = new User("manager@test.com", passwordEncoder.encode("manager"), "Петр", "Петров", "MANAGER");
            userRepository.save(manager);
        }

        if (!userRepository.existsByEmail("ivan@mail.ru")) {
            User client = new User("ivan@mail.ru", passwordEncoder.encode("123"), "Клиент", "Тестовый", "CLIENT");
            client.setPhone("+79991234567");
            userRepository.save(client);
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            Category cat1 = new Category("Аренда помещений", "Различные варианты аренды залов и комнат");
            categoryRepository.save(cat1);

            Category cat2 = new Category("Транспорт", "Услуги перевозки и аренды автомобилей");
            categoryRepository.save(cat2);
        }
    }

    private void seedServices() {
        if (serviceRepository.count() == 0) {
            Category cat1 = categoryRepository.findAll().get(0);

            Service service1 = new Service("Аренда конференц-зала", "Зал на 50 человек с оборудованием", "daily", "1d", 5000.0, 10, 50);
            service1.setCategory(cat1);
            serviceRepository.save(service1);

            Service service2 = new Service("Аренда автомобиля", "Легковой автомобиль на сутки", "daily", "1d", 3000.0, 1, 5);
            serviceRepository.save(service2);
        }
    }

    private void seedDiscounts() {
        if (discountRepository.count() == 0) {
            Discount d1 = new Discount("Скидка 10%", "percent", 10.0, LocalDateTime.now().plusDays(30));
            discountRepository.save(d1);

            Discount d2 = new Discount("Скидка 500 руб.", "fixed", 500.0, LocalDateTime.now().plusDays(15));
            discountRepository.save(d2);
        }
    }

    private void seedNews() {
        if (newsRepository.count() == 0) {
            User admin = userRepository.findByEmail("admin@test.com").orElse(null);
            if (admin != null) {
                News news1 = new News("Открытие летнего сезона!", 
                    "Мы рады сообщить, что с 1 мая база отдыха переходит на летний режим работы.", 
                    admin, "published");
                newsRepository.save(news1);

                News news2 = new News("Скидки для больших групп", 
                    "При бронировании 3-х и более услуг скидка 15%.", 
                    admin, "published");
                newsRepository.save(news2);
            }
        }
    }
}