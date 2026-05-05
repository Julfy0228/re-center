# JMeter Tests для Re-Center

Тесты нагрузки для приложения Re-Center.

## Структура

- `booking.jmx` — Тест сценария бронирования (login → browse services → create/view/delete bookings)
- `admin.jmx` — Тест сценария администратора (login → manage services)
- `profile.jmx` — Тест сценария профиля пользователя (login → update profile)
- `users.csv` — Тестовые данные обычных пользователей (5 пользователей)
- `admin-users.csv` — Тестовые данные администраторов (5 администраторов)

## Подготовка к тестированию

### 1. Запустить backend с инициализацией тестовых данных

```bash
# Из корневой папки проекта
.\run-backend.bat --jmeter-test
```

Флаг `--jmeter-test` автоматически:
- Активирует Spring профиль `jmeter-test`
- Использует in-memory H2 БД (данные хранятся в оперативке, не на диске)
- Очищает БД и создаёт 5 обычных пользователей (CLIENT)
- Создаёт 5 администраторов (ADMIN) с правильной ролью

### 2. Запустить JMeter в GUI режиме

```bash
# Из папки jmeter-tests
jmeter
```

### 3. Открыть и запустить тесты

1. File → Open → выбрать `booking.jmx` (или `admin.jmx`, `profile.jmx`)
2. Нажать зелёную кнопку "Start" (или Ctrl+Enter)

## Тестовые данные

### Обычные пользователи (CLIENT)
```
lab4_user1@example.com / lab41234
lab4_user2@example.com / lab41234
lab4_user3@example.com / lab41234
lab4_user4@example.com / lab41234
lab4_user5@example.com / lab41234
```

### Администраторы (ADMIN)
```
lab4_admin1@example.com / lab41234
lab4_admin2@example.com / lab41234
lab4_admin3@example.com / lab41234
lab4_admin4@example.com / lab41234
lab4_admin5@example.com / lab41234
```

## Конфигурация тестов

Все тесты используют:
- **Base URL**: `http://localhost:8080/re-center`
- **Thread Group**: 5 потоков, 1 итерация
- **Ramp-up**: 1 секунда
- **Global HTTP Cookie Manager** — для сохранения сессий
- **Global Authorization Header Manager** — для передачи JWT токена

## Отладка

Каждый тест содержит Debug Sampler, который выводит все переменные в Results Tree.

Для просмотра ответов сервера:
1. View Results Tree → выбрать нужный запрос
2. Response Data → посмотреть JSON ответ
