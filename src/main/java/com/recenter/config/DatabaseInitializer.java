package com.recenter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        createTables();
        seedUsers();
        seedServices();
        seedNews();
    }

    private void createTables() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "phone TEXT, " +
                "role TEXT NOT NULL)");

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
    }

    private void seedUsers() {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM users WHERE email = 'admin'", Integer.class);
        if (count != null && count == 0) {
            jdbcTemplate.update("INSERT INTO users (email, password, first_name, role) VALUES (?, ?, ?, ?)",
                    "admin", "admin", "Администратор", "ADMIN");
        }

        Integer clientCount = jdbcTemplate.queryForObject("SELECT count(*) FROM users WHERE email = 'ivan@mail.ru'", Integer.class);
        if (clientCount != null && clientCount == 0) {
            jdbcTemplate.update("INSERT INTO users (email, password, first_name, last_name, phone, role) VALUES (?, ?, ?, ?, ?, ?)",
                    "ivan@mail.ru", "123", "Иван", "Петров", "+79991234567", "CLIENT");
        }
    }

    private void seedServices() {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM services", Integer.class);
        if (count != null && count == 0) {
            jdbcTemplate.update("INSERT INTO services (title, description, base_price, service_type, min_capacity, max_capacity) VALUES (?, ?, ?, ?, ?, ?)",
                    "Коттедж Премиум", "Просторный двухэтажный дом с сауной и камином.", 5000.0, "DAILY", 1, 6);
            jdbcTemplate.update("INSERT INTO services (title, description, base_price, service_type, min_capacity, max_capacity) VALUES (?, ?, ?, ?, ?, ?)",
                    "Теннисный корт", "Профессиональное покрытие Hard.", 1000.0, "HOURLY", 2, 4);
        }
    }

    private void seedNews() {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM news", Integer.class);
        if (count != null && count == 0) {
            String date = LocalDateTime.now().format(formatter);

            jdbcTemplate.update("INSERT INTO news (title, content, publication_date) VALUES (?, ?, ?)",
                    "Открытие летнего сезона!", "Мы рады сообщить, что с 1 мая база отдыха переходит на летний режим работы.", date);

            jdbcTemplate.update("INSERT INTO news (title, content, publication_date) VALUES (?, ?, ?)",
                    "Скидки для больших групп", "При бронировании 3-х и более коттеджей скидка 15%.", date);
        }
    }
}