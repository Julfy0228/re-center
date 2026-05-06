package com.recenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;

/**
 * REST API для инициализации тестовых данных
 * Доступен только в профиле test-data
 * 
 * Endpoints:
 * POST /api/test-data/execute - выполнить SQL запрос
 * DELETE /api/test-data/cleanup - очистить все данные
 */
@Controller
@RequestMapping("/api/test-data")
public class TestDataController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isTestDataMode() {
        return Arrays.asList(environment.getActiveProfiles()).contains("test-data");
    }

    /**
     * Выполнить SQL запрос
     * Принимает SQL в теле запроса
     */
    @PostMapping("/execute")
    @ResponseBody
    public ResponseEntity<?> executeSql(@RequestBody SqlRequest request) {
        if (!isTestDataMode()) {
            return ResponseEntity.status(403).body(new ExecutionResult(
                "error",
                "Test Data Controller is not available in this mode",
                0
            ));
        }

        try {
            if (request.getSql() == null || request.getSql().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("SQL query is empty");
            }

            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                String[] queries = request.getSql().split(";");
                int executedCount = 0;
                
                for (String query : queries) {
                    String trimmedQuery = query.trim();
                    if (!trimmedQuery.isEmpty()) {
                        trimmedQuery = hashPasswordsInQuery(trimmedQuery);
                        
                        try {
                            if (trimmedQuery.toUpperCase().startsWith("SELECT")) {
                                stmt.executeQuery(trimmedQuery);
                            } else {
                                stmt.executeUpdate(trimmedQuery);
                            }
                            executedCount++;
                        } catch (Exception e) {
                            System.err.println("Error executing query: " + trimmedQuery);
                            System.err.println("Error: " + e.getMessage());
                            throw e;
                        }
                    }
                }
                
                return ResponseEntity.ok(new ExecutionResult(
                    "success",
                    "Executed " + executedCount + " SQL statements",
                    executedCount
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ExecutionResult(
                "error",
                "SQL execution failed: " + e.getMessage(),
                0
            ));
        }
    }

    /**
     * Хеширует пароли в INSERT запросах
     * Ищет паттерн 'computeHash(password)' и заменяет на хешированный пароль
     */
    private String hashPasswordsInQuery(String query) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("'computeHash\\(([^)]+)\\)'");
        java.util.regex.Matcher matcher = pattern.matcher(query);
        
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String password = matcher.group(1);
            String hashedPassword = passwordEncoder.encode(password);
            String escapedHash = java.util.regex.Matcher.quoteReplacement("'" + hashedPassword + "'");
            matcher.appendReplacement(sb, escapedHash);
        }
        matcher.appendTail(sb);
        
        return sb.toString();
    }

    /**
     * Очистить все данные (удалить все записи из таблиц)
     */
    @DeleteMapping("/cleanup")
    @ResponseBody
    public ResponseEntity<?> cleanup() {
        if (!isTestDataMode()) {
            return ResponseEntity.status(403).body(new ExecutionResult(
                "error",
                "Test Data Controller is not available in this mode",
                0
            ));
        }

        try {
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
                
                stmt.execute("DELETE FROM Bookings");
                stmt.execute("DELETE FROM Reviews");
                stmt.execute("DELETE FROM Services");
                stmt.execute("DELETE FROM Users");
                
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
                
                return ResponseEntity.ok(new ExecutionResult(
                    "success",
                    "All test data cleaned up",
                    0
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ExecutionResult(
                "error",
                "Cleanup failed: " + e.getMessage(),
                0
            ));
        }
    }

    /**
     * Получить статус (проверка, что контроллер доступен)
     */
    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<?> status() {
        if (!isTestDataMode()) {
            return ResponseEntity.status(403).body(new StatusResponse(
                "Test Data Controller is not available",
                "test-data mode disabled"
            ));
        }

        return ResponseEntity.ok(new StatusResponse(
            "Test Data Controller is active",
            "test-data mode enabled"
        ));
    }

    public static class SqlRequest {
        private String sql;

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }

    public static class ExecutionResult {
        private String status;
        private String message;
        private int count;

        public ExecutionResult(String status, String message, int count) {
            this.status = status;
            this.message = message;
            this.count = count;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public int getCount() {
            return count;
        }
    }

    public static class StatusResponse {
        private String message;
        private String mode;

        public StatusResponse(String message, String mode) {
            this.message = message;
            this.mode = mode;
        }

        public String getMessage() {
            return message;
        }

        public String getMode() {
            return mode;
        }
    }
}
