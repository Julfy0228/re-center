package com.recenter.config;

import com.recenter.entity.User;
import com.recenter.entity.Service;
import com.recenter.entity.News;
import com.recenter.repository.UserJpaRepository;
import com.recenter.repository.ServiceJpaRepository;
import com.recenter.repository.NewsJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Инициализация БД при запуске приложения
 * Создаёт таблицы и загружает тестовые данные через JPA Repository
 */
@Component
public class DatabaseInitializer {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ServiceJpaRepository serviceJpaRepository;

    @Autowired
    private NewsJpaRepository newsJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        try {
            createTables();
            seedUsers();
            seedServices();
            seedNews();
            System.out.println("✓ БД инициализирована успешно");
        } catch (Exception e) {
            System.err.println("✗ Ошибка инициализации БД: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Создание таблиц в БД
     * Hibernate может создать их автоматически, но используем явное создание для совместимости
     */
    private void createTables() {
        try {
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "phone TEXT, " +
                    "role TEXT NOT NULL, " +
                    "created_at TIMESTAMP)");

            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS services (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "description TEXT, " +
                    "base_price REAL, " +
                    "service_type TEXT, " +
                    "min_capacity INTEGER, " +
                    "max_capacity INTEGER)");

            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS news (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "content TEXT, " +
                    "publication_date TEXT)");

            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "service_id INTEGER, " +
                    "start_date TEXT, " +
                    "end_date TEXT, " +
                    "total_price REAL, " +
                    "status TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
        } catch (Exception e) {
            // Таблицы могут уже существовать, это нормально
            System.out.println("Таблицы уже созданы или есть ошибка: " + e.getMessage());
        }
    }

    /**
     * Заполнение тестовых пользователей через JPA Repository
     */
    private void seedUsers() {
        if (!userJpaRepository.existsByEmail("admin")) {
            User admin = new User(
                "admin",
                passwordEncoder.encode("admin"),
                "Администратор",
                null,
                "ADMIN"
            );
            admin.setCreatedAt(LocalDateTime.now());
            userJpaRepository.save(admin);
            System.out.println("✓ Создан пользователь: admin");
        }

        if (!userJpaRepository.existsByEmail("ivan@mail.ru")) {
            User client = new User(
                "ivan@mail.ru",
                passwordEncoder.encode("123"),
                "Иван",
                "Петров",
                "CLIENT"
            );
            client.setPhone("+79991234567");
            client.setCreatedAt(LocalDateTime.now());
            userJpaRepository.save(client);
            System.out.println("✓ Создан пользователь: ivan@mail.ru");
        }
    }

    private void seedServices() {
        if (serviceJpaRepository.count() == 0) {
            Service service1 = new Service(
                "Коттедж Премиум",
                "Просторный двухэтажный дом с сауной и камином.",
                5000.0,
                "DAILY",
                1, 6
            );
            serviceJpaRepository.save(service1);

            Service service2 = new Service(
                "Теннисный корт",
                "Профессиональное покрытие Hard.",
                1000.0,
                "HOURLY",
                2, 4
            );
            serviceJpaRepository.save(service2);
            System.out.println("✓ Созданы услуги (2 шт.)");
        }
    }

    private void seedNews() {
        if (newsJpaRepository.count() == 0) {
            LocalDateTime now = LocalDateTime.now();

            News news1 = new News(
                "Открытие летнего сезона!",
                "Мы рады сообщить, что с 1 мая база отдыха переходит на летний режим работы.",
                now
            );
            newsJpaRepository.save(news1);

            News news2 = new News(
                "Скидки для больших групп",
                "При бронировании 3-х и более коттеджей скидка 15%.",
                now
            );
            newsJpaRepository.save(news2);
            System.out.println("✓ Созданы новости (2 шт.)");
        }
    }
}