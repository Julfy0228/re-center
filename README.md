# Re-Center — система бронирования

Этот репозиторий — учебный веб‑проект (WAR), реализованный на Spring MVC. Проект использует Java 17 и собирается через Maven. В нём есть API (REST‑контроллеры, использующие DTO) и классический JSP‑фронтенд.


## Требования

- Java JDK 17+
- Apache Maven 3.6+
- (опционально) редактор/IDE (VS Code / IntelliJ)

## Сборка и запуск локально

1. Откройте терминал в корне проекта:

```powershell
cd "c:\Users\user\Desktop\Учёба\Лабораторные Работы\5 Семестр\РПС\re-center"
```

2. Соберите и запустите контейнер Tomcat с помощью Maven/Cargo (включён в pom.xml):

```powershell
mvn clean package cargo:run -DskipTests
```

3. Откройте приложение в браузере:

```
http://localhost:8080/re-center/
```


## Где что находится (важные пути)

- Конфигурация Spring MVC: `src/main/java/com/recenter/config/WebConfig.java`, `WebAppInitializer.java`.
- DTO: `src/main/java/com/recenter/dto/`.
- Контроллеры (REST): `src/main/java/com/recenter/controllers/`.
- Модели: `src/main/java/com/recenter/model/`.
- JSP/статические файлы: `src/main/webapp/`.

## REST API — краткий обзор

Контроллеры расположены под `/api/*`. Основные эндпоинты (пример):

- `POST /api/users/register` — регистрация (RegistrationRequestDto)
- `POST /api/users/login` — логин (LoginRequestDto)
- `GET /api/users/{id}` — информация о пользователе
- `GET /api/services`, `POST /api/services`, `GET /api/services/{id}` — управление услугами
- `POST /api/bookings`, `GET /api/bookings/{id}` — бронирования
- `GET/POST /api/promotions`, `GET /api/promotions/{id}` — акции
- `GET/POST /api/discounts`, `POST /api/discounts/assign` — скидки
- `POST /api/payments`, `GET /api/payments/{id}` — платежи
- `GET/POST /api/news` — новости

## Дальнейшие шаги (планы)

- Подключить JdbcTemplate/DAO к SQLite (в `WebConfig` есть бин DataSource/JdbcTemplate).
- Добавить полноценные мапперы model <-> dto и тесты.
- Перевести контроллеры с in‑memory на реальное хранение в БД.

---
Автор(ы):

- Мошталев Г.С.
- Турков Б.С.

*ВлГУ, 2025*