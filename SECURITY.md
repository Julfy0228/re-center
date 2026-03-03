# Spring Security - Документация по аутентификации

## Обзор изменений

Проект был обновлён для использования **Spring Security 7.0** вместо простой аутентификации на базе HttpSession.

## Что поправлено

1. **Устранено предупреждение о deprecated-классе**
   - Удалён использованный `MappingJackson2HttpMessageConverter` из `WebConfig.java`
   - Заменён на более совместимый подход с `ObjectMapper` bean

2. **Добавлена интеграция Spring Security**
   - Добавлены зависимости Spring Security в `pom.xml`
   - Созданы классы: `SecurityConfig`, `CustomUserDetailsService`, `SecurityInitializer`
   - Обновлены `WebAppInitializer` и `AuthController`

## Архитектура безопасности

### 1. CustomUserDetailsService
- Реализует интерфейс `UserDetailsService`
- Загружает пользователей из БД через `UserRepository`
- Преобразует данные пользователя в `UserDetails` для Spring Security

### 2. SecurityConfig
- Конфигурирует `SecurityFilterChain` для фильтрации HTTP-запросов
- Настраивает form-based login (использует поле `email` вместо `username`)
- Определяет правила доступа:
  - `/auth/*`, `/`, статические ресурсы: Public
  - `/admin/**`: Role ADMIN
  - `/cabinet/**`: Authenticated users
  - Остальное: Authenticated

### 3. PasswordEncoder
- Используется `BCryptPasswordEncoder` для хеширования паролей
- Пароли кодируются при регистрации в `UserService.register()`
- При входе пароль сравнивается через `passwordEncoder.matches()`

## Как использовать

### Регистрация нового пользователя
```
POST /auth/register
Параметры формы:
- email
- password
- firstName
- lastName
- phone
```

### Вход в систему
```
POST /auth/login
Параметры формы:
- email (используется как username)
- password
```

### Выход
```
GET /auth/logout
```

### Доступ к защищённым ресурсам
Аутентифицированные пользователи автоматически получают доступ с помощью cookie сессии или Bearer token (в REST API).

## Настройки безопасности

### CSRF Protection
Текущая конфигурация отключает CSRF для простоты разработки:
```java
.csrf(csrf -> csrf.disable())
```
**Внимание:** В production нужно включить и правильно настроить CSRF!

### Роли пользователей
- `ROLE_ADMIN` - для администраторов
- `ROLE_CLIENT` - для обычных пользователей

Роль назначается при регистрации (по умолчанию - CLIENT).

## Интеграция с REST API

Если используется REST API (JSON), пользователи могут:
1. Аутентифицироваться через форму login `/auth/login` (получить session cookie)
2. Использовать session cookie для последующих запросов
3. Или реализовать JWT-аутентификацию (требуется дополнительная конфигурация)

## Примечания

- Существующие пароли в БД нужно переэнкодировать BCrypt при миграции
- Для разработки можно временно использовать `NoOpPasswordEncoder`, но в production всегда используйте `BCryptPasswordEncoder`
- SecurityInitializer автоматически регистрирует Spring Security фильтры при запуске приложения

## Полезные ссылки

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Spring Security 7.0 Migration Guide](https://docs.spring.io/spring-security/reference/migration/index.html)
