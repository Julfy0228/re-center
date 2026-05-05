package com.recenter.config;

import com.recenter.model.entity.User;
import com.recenter.model.enums.UserRole;
import com.recenter.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * Инициализирует тестовые данные при запуске с профилем jmeter-test
 * Использование: mvn package cargo:run -Pjmeter-test
 */
@Configuration
@Profile("jmeter-test")
public class TestDataInitializer implements InitializingBean {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("🧪 Инициализация тестовых данных для JMeter (in-memory БД)...");

        try {
            // Очистка существующих пользователей
            userRepository.deleteAll();

            // Создание обычных пользователей (CLIENT)
            List<User> regularUsers = Arrays.asList(
                    createUser("lab4_user1@example.com", "lab41234", "User", "One", UserRole.CLIENT),
                    createUser("lab4_user2@example.com", "lab41234", "User", "Two", UserRole.CLIENT),
                    createUser("lab4_user3@example.com", "lab41234", "User", "Three", UserRole.CLIENT),
                    createUser("lab4_user4@example.com", "lab41234", "User", "Four", UserRole.CLIENT),
                    createUser("lab4_user5@example.com", "lab41234", "User", "Five", UserRole.CLIENT)
            );

            // Создание администраторов (ADMIN)
            List<User> adminUsers = Arrays.asList(
                    createUser("lab4_admin1@example.com", "lab41234", "Admin", "One", UserRole.ADMIN),
                    createUser("lab4_admin2@example.com", "lab41234", "Admin", "Two", UserRole.ADMIN),
                    createUser("lab4_admin3@example.com", "lab41234", "Admin", "Three", UserRole.ADMIN),
                    createUser("lab4_admin4@example.com", "lab41234", "Admin", "Four", UserRole.ADMIN),
                    createUser("lab4_admin5@example.com", "lab41234", "Admin", "Five", UserRole.ADMIN)
            );

            userRepository.saveAll(regularUsers);
            userRepository.saveAll(adminUsers);

            System.out.println("✅ Тестовые данные инициализированы:");
            System.out.println("   - 5 обычных пользователей (CLIENT)");
            System.out.println("   - 5 администраторов (ADMIN)");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при инициализации тестовых данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private User createUser(String email, String password, String firstName, String lastName, UserRole role) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMiddleName("");
        user.setPhoneNumber("+7" + System.nanoTime() % 10000000000L);
        user.setRole(role);
        return user;
    }
}
