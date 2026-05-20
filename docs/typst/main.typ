#import "template.typ": template

#show: template.with(
  title: [ Пояснительная записка ],
  discipline: [
    к курсовому проекту по дисциплине \
    "Распределённые программные системы" \
    на тему
  ],
  theme: [ Проектирование и разработка информационной системы \ "База отдыха Re-Center" ],
  students: (
    (
      name: "Б. С. Турков",
      group: "ИСТ-123",
      gender: "male"
    ),
    (
      name: "Г. С. Мошталёв",
      group: "ИСТ-123",
      gender: "male"
    )
  ),
  teacher: (
    name: "Г. В. Проскурина",
    position: "доц. каф. ИСПИ",
    gender: "female"
  )
)

= Описание предметной области

База отдыха "Re-Center" представляет собой комплекс услуг для отдыха и досуга клиентов, включающий проживание в коттеджах, спортивные мероприятия, экскурсии, аренду оборудования и развлекательные программы. В современном формате работы базы отдыха сталкиваются с необходимостью автоматизации ключевых процессов: бронирования услуг, учета клиентов, управления оплатами и публикации актуальной информации.

== Основные проблемы, решаемые системой

- Разрозненность информации об услугах и ценах
- Отсутствие удобной системы онлайн-бронирования
- Ручное распределение участников по возрастным группам
- Отсутствие механизма предоплаты и онлайн-оплаты
- Неэффективная система отмены бронирований
- Отсутствие системы мотивации клиентов
- Недостаток актуальных новостей и объявлений

== Категории пользователей

Система предназначена для четырех основных категорий пользователей:

- Администраторы — управление услугами, бронированиями, новостями и статистикой
- Менеджеры — управление бронированиями и клиентами
- Зарегистрированные клиенты — просмотр услуг, бронирование, управление своими заказами
- Гости (незарегистрированные пользователи) — просмотр услуг и новостей

= Анализ бизнес-процесса

== Описание прецедентов

=== Регистрироваться в системе

*Предусловие:* Пользователь (Гость) находится на главной странице веб-приложения базы отдыха.

*Действующее лицо:* Гость

*Основной поток:*

+ Гость нажимает кнопку «Регистрация» на главной странице
+ Система отображает форму регистрации с полями: имя, электронная почта, пароль
+ Гость заполняет обязательные поля и нажимает кнопку «Согласиться с условиями»
+ Гость нажимает кнопку «Зарегистрироваться»
+ Система проверяет корректность данных и уникальность электронной почты
+ Система создаёт новую учётную запись Пользователя, присваивает ему статус «Клиент» и автоматически выполняет вход в систему
+ Система перенаправляет Клиента в его личный кабинет

*Альтернативный поток:* Электронная почта уже зарегистрирована

- На шаге 5 система обнаруживает, что введённая электронная почта уже существует в базе данных
- Система отображает сообщение об ошибке «Пользователь с таким email уже зарегистрирован» и предлагает восстановить пароль
- Поток прерывается

*Постусловие:* В системе создана новая учётная запись, пользователь аутентифицирован и получил роль «Клиент».

=== Бронировать услугу

*Предусловие:* Пользователь авторизован в системе как Клиент.

*Действующее лицо:* Клиент

*Основной поток:*

+ Клиент просматривает каталог услуг и нажимает кнопку «Подробнее» у выбранной услуги
+ Система отображает детальную информацию об услуге (описание, фотографии, цена)
+ Клиент выбирает дату заезда и дату выезда в соответствующих полях формы
+ Система проверяет доступность услуги на выбранные даты
+ Система рассчитывает итоговую стоимость, применяя доступные скидки
+ Клиент нажимает кнопку «Забронировать»
+ Система создаёт новое бронирование со статусом «Ожидание оплаты»
+ Система перенаправляет Клиента на страницу оплаты

*Альтернативный поток:* Услуга недоступна на выбранные даты

- На шаге 4 система определяет, что услуга занята на запрашиваемый период
- Система отображает сообщение «Выбранные даты недоступны» и предлагает выбрать другие даты
- Поток возвращается к шагу 3

*Постусловие:* В системе создана новая заявка на бронирование, которая ожидает подтверждения оплаты.

=== Отменять бронирование

*Предусловие:* Клиент авторизован в системе и имеет активные бронирования.

*Действующее лицо:* Клиент

*Основной поток:*

+ Клиент открывает раздел «Мои бронирования» в личном кабинете
+ Система отображает список активных бронирований Клиента
+ Клиент нажимает кнопку «Отменить» у выбранного бронирования
+ Система запрашивает подтверждение действия
+ Клиент подтверждает отмену
+ Система проверяет временные условия отмены (срок до начала услуги)
+ Система рассчитывает сумму возврата средств согласно правилам базы отдыха
+ Система изменяет статус бронирования на «Отменено» и, если применимо, инициирует процесс возврата денежных средств
+ Система уведомляет Клиента об успешной отмене бронирования

*Альтернативный поток:* Нарушены правила отмены

- На шаге 6 система определяет, что срок для бесплатной отмены истёк
- Система отображает предупреждение о том, что будет удержан штраф, и запрашивает подтверждение отмены
- Поток возвращается к шагу 5

*Постусловие:* Статус бронирования изменён на «Отменено», клиенту возвращена часть средств согласно политике отмены.

=== Добавлять новую услугу

*Предусловие:* Пользователь авторизован в системе как Администратор.

*Действующее лицо:* Администратор

*Основной поток:*

+ Администратор переходит в «Панель администратора» и выбирает раздел «Управление услугами»
+ Система отображает интерфейс управления услугами со списком существующих
+ Администратор нажимает кнопку «Добавить услугу»
+ Система отображает форму для ввода данных о новой услуге
+ Администратор заполняет обязательные поля: название, описание, категория, базовая цена, вместимость
+ Администратор нажимает кнопку «Сохранить»
+ Система проверяет корректность данных и сохраняет новую услугу в каталоге
+ Система уведомляет Администратора об успешном добавлении услуги

*Альтернативный поток:* Обнаружены ошибки в данных

- На шаге 7 система проверяет введённые данные и обнаруживает ошибки (например, не заполнено обязательное поле)
- Система отображает сообщение с указанием допущенных ошибок
- Поток возвращается к шагу 5 для исправления данных

*Постусловие:* В каталоге услуг системы создана новая позиция, которая стала доступна для бронирования Клиентами.

=== Публиковать новость

*Предусловие:* Пользователь авторизован в системе как Администратор.

*Действующее лицо:* Администратор

*Основной поток:*

+ Администратор переходит в «Панель администратора» и выбирает раздел «Управление новостями»
+ Система отображает интерфейс управления новостями со списком существующих
+ Администратор нажимает кнопку «Добавить новость»
+ Система отображает форму для создания новости
+ Администратор заполняет поля: заголовок, содержание, дата публикации
+ Администратор нажимает кнопку «Опубликовать»
+ Система сохраняет новость и делает её видимой для всех посетителей сайта в разделе «Новости»
+ Система уведомляет Администратора об успешной публикации

*Альтернативный поток:* Сохранение черновика

- На шаге 6 Администратор нажимает кнопку «Сохранить черновик»
- Система сохраняет новость со статусом «Черновик», не публикуя её на сайте
- Поток завершается

*Постусловие:* Новая запись добавлена в блок новостей на главной странице и в разделе «Новости», где её могут видеть все посетители сайта.

== Диаграмма прецедентов

#figure(
  image("../diagrams/drawio-export/use-case.png", width: 90%),
  caption: [Диаграмма прецедентов системы]
) <fig:usecase>

= Требования к системе

== Функциональные требования

=== Для клиентов

- Просмотр услуг и цен
- Онлайн-бронирование с выбором дат
- Личный кабинет с моими бронированиями
- Отмена бронирований

=== Для администраторов

- Добавление и редактирование услуг
- Управление бронированиями
- Публикация новостей и акций
- Просмотр статистики

=== Системные функции

- Автоматические скидки (групповые, именинникам)
- Проверка доступности номеров
- Учет оплаты и возвратов

== Нефункциональные требования

=== Производительность

- Быстрая загрузка страниц
- Стабильная работа при наплыве посетителей

=== Безопасность

- Защита данных клиентов
- Надежное хранение паролей

=== Удобство

- Простой и понятный интерфейс
- Работа на компьютерах
- Легкий процесс бронирования

= Макет интерфейса

== Общая структура интерфейса

Верхняя навигационная панель содержит логотип "Re-Center", основные разделы сайта (Главная, Услуги, Новости, Контакты) и кнопки авторизации для пользователей. Подвал дублирует основную навигацию с добавлением копирайта "Владимир 2025" и обеспечивает постоянный доступ к ключевым разделам с любой страницы.

Модальное окно входа/регистрации позволяет пользователю войти или зарегистрироваться в веб-приложении.

#figure(
  image("../screenshots/01-Вход.png", width: 85%),
  caption: [Макет основного интерфейса — страница входа]
) <fig:login>

== Главная страница

Главная страница предназначена для быстрого ознакомления с возможностями базы и текущими акциями. Содержит:

- Блок новостей: отображает актуальные события базы отдыха карточками с датами публикации
- Популярные услуги: показывает востребованные предложения с ценами и кнопками "подробнее"

#figure(
  image("../screenshots/06-Новости.png", width: 85%),
  caption: [Макет главной страницы с новостями]
) <fig:home>

== Личный кабинет

Интерфейс ориентирован на управление существующими бронированиями и мотивацию к повторным заказам. Включает:

- Вкладка активных бронирований: таблица с текущими заказами, статусами оплаты и кнопками управления ("Отменить/Детали")
- Система скидок: отображение персональных бонусов и специальных предложений

#figure(
  image("../screenshots/07-Мои-бронирования.png", width: 85%),
  caption: [Макет личного кабинета с активными бронированиями]
) <fig:cabinet>

== Страница услуги

Детальная карточка конкретного предложения сфокусирована на конверсии посетителя в клиента через упрощенный процесс выбора дат. Включает:

- Визуальная зона с фотографией объекта
- Текстовое описание с характеристиками и вместимостью
- Блок бронирования с выбором дат, отображением цены и кнопкой "Забронировать"

#figure(
  image("../screenshots/04-Детали-услуги.png", width: 85%),
  caption: [Макет страницы услуги с формой бронирования]
) <fig:service>

== Админ-панель

Инструмент управления для персонала базы отдыха. Состоит из:

- Дашборд со статистикой: ключевые метрики в реальном времени (новые брони, выручка, загрузка)
- Таблица последних бронирований с возможностью редактирования
- Быстрые действия: кнопки добавления новых услуг и создания новостей

#figure(
  image("../screenshots/10-Управление-общий-экран.png", width: 85%),
  caption: [Макет админ-панели с дашбордом]
) <fig:admin>

= Модель и структура БД

== ER-диаграмма

#figure(
  image("../diagrams/db.png", width: 95%),
  caption: [Физическая ER-диаграмма программной системы]
) <fig:erdiagram>

== Основные сущности системы

#figure(
  table(
    columns: (1fr, 2fr),
    align: (left, left),
    [*Сущность*], [*Назначение*],
    [Пользователь (User)], [Хранит информацию о клиентах, администраторах и менеджерах],
    [Категория услуг (Category)], [Группирует услуги по видам (проживание, спорт, питание и т.п.)],
    [Услуга (Service)], [Конкретное предложение (домик, площадка, бассейн и т.д.)],
    [Скидка (Discount)], [Общие скидки (процентные/фиксированные)],
    [Пользовательская скидка (UserDiscount)], [Назначенные пользователям скидки],
    [Бронирование (Booking)], [Информация о заказе/бронировании услуги пользователем],
    [Оплата (Payment)], [Данные об оплате брони],
    [Отзыв (Review)], [Отзывы клиентов на услуги],
    [Новости (News)], [Актуальные новости базы отдыха],
    [Акции (Promotion)], [Действующие акции на услуги],
    [Связь Акции–Услуга (PromotionService)], [К каким определенным услугам применяется акция],
    [Связь Акции–Категория (PromotionCategory)], [К каким целым категориям услуг применяется акция],
    [Журнал действий (Activity)], [Хранит действия всех пользователей],
    [Уведомления (Notification)], [Рассылка уведомлений для всех пользователей],
  ),
  caption: [Основные сущности системы]
) <tbl:entities>

== Описание связей между сущностями

#figure(
  table(
    columns: (1.2fr, 1fr, 2fr),
    align: (left, left, left),
    [*Сущности*], [*Вид связи*], [*Описание*],
    [Category - Service], [1:N], [Одна категория включает множество услуг. Каждая услуга относится к одной категории],
    [User - Booking], [1:N], [Пользователь может создать множество бронирований. Каждое бронирование принадлежит одному пользователю],
    [Service - Booking], [1:N], [Одна услуга может быть забронирована многократно. Каждое бронирование относится к одной услуге],
    [Booking - Payment], [1:N], [Одно бронирование может оплачиваться несколькими платежами (частичная оплата, предоплата/доплата). Каждый платеж относится к одному бронированию],
    [User - Review], [1:N], [Пользователь может оставить множество отзывов, но каждый отзыв относится к определенному пользователю],
    [Service - Review], [1:N], [Услуга может иметь множество отзывов, но каждый отзыв относится к определенной услуге],
    [User - News], [1:N], [Пользователь (автор) может публиковать различные новости, но новость создана определенным пользователем],
    [User - Notification], [1:N], [Пользователь может иметь множество уведомлений, но каждое уведомление относится к определенному пользователю],
    [User - Activity], [1:N], [Пользователь может иметь множество выполненных действий, но каждое действие относится к определенному пользователю],
    [User - Discount], [M:N], [Одна скидка может быть назначена многим пользователям, и пользователь может иметь много скидок (через UserDiscount)],
    [Promotion - Category], [M:N], [Акция может распространяться на несколько категорий, а категория может участвовать в разных акциях (через PromotionCategory)],
    [Promotion - Service], [M:N], [Акция может применяться к множеству конкретных услуг независимо от категории, а у определенной услуги могут быть различные действующие акции (через PromotionService)],
  ),
  caption: [Описание связей между сущностями]
) <tbl:relations>

= Реализация API

== DTO (Data Transfer Objects)

DTO — классы, необходимые для передачи данных между слоями приложения (например, между сервером и клиентом). Они изолируют структуру БД от внешних API и обеспечивают безопасность (например, не передают пароль). Содержат только необходимые поля и не включают бизнес-логику.

=== Аутентификация: AuthRequest и AuthResponse

Используются для аутентификации пользователя. `AuthRequest` содержит email и пароль, `AuthResponse` возвращает JWT-токен и данные пользователя.

#figure(
```java
@Data
public class AuthRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String role;
}
```,
  caption: [DTO для аутентификации]
)

=== Регистрация: RegisterRequest

Содержит данные для регистрации нового пользователя с валидацией email, пароля и телефона.

#figure(
```java
@Data
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    private String firstName;
    private String lastName;
    private String middleName;

    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Неверный формат телефона")
    private String phoneNumber;
}
```,
  caption: [DTO для регистрации пользователя]
)

=== Бронирование: BookingRequest и BookingResponse

`BookingRequest` используется для создания бронирования с проверкой дат. `BookingResponse` возвращает полную информацию о бронировании, включая статус оплаты.

#figure(
```java
@Data
public class BookingRequest {
    private Long serviceId;

    @Future(message = "Дата заезда должна быть в будущем")
    private LocalDateTime startDate;

    @Future(message = "Дата выезда должна быть в будущем")
    private LocalDateTime endDate;

    private Integer peopleCount;
    private BookingStatus status;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private Long serviceId;
    private String serviceTitle;
    private Long userId;
    private String userEmail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer peopleCount;
    private BigDecimal initialPrice;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private boolean paid;
}
```,
  caption: [DTO для бронирования услуг]
)

=== Услуги и новости: ServiceResponse и NewsResponse

`ServiceResponse` содержит информацию об услуге для отображения в каталоге. `NewsResponse` используется для передачи новостей и акций.

#figure(
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer duration;
    private BigDecimal price;
    private Integer maxPeople;
    private Long categoryId;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Long authorId;
    private String authorEmail;
    private String status;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
}
```,
  caption: [DTO для услуг и новостей]
)

=== Платежи: PaymentResponse

Содержит информацию об оплате бронирования.

#figure(
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String status;
    private String paymentMethod;
}
```,
  caption: [DTO для платежей]
)

== Контроллеры

=== AuthController — управление аутентификацией

Контроллер аутентификации и авторизации предоставляет endpoints для входа, регистрации и получения информации о текущем пользователе. Использует JWT-токены для защиты ресурсов.

#figure(
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        AuthResponse response = new AuthResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        User user = User.builder()
                .email(registerRequest.getEmail())
                .passwordHash(encoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role(UserRole.CLIENT)
                .build();
        userService.create(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userService.getById(userDetails.getId())
            .map(user -> ResponseEntity.ok(new UserResponse(...)))
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"));
    }
}
```,
  caption: [AuthController — endpoints для входа, регистрации и получения данных пользователя]
)

=== BookingController — управление бронированиями

Контроллер бронирований обрабатывает создание, получение, обновление и удаление бронирований. Проверяет доступность услуги на выбранные даты и статус оплаты.

#figure(
```java
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        // Проверка доступности услуги на выбранные даты
        boolean isBusy = bookingService.getAll().stream()
                .filter(b -> b.getService().getId().equals(request.getServiceId()))
                .anyMatch(b -> !(b.getEndDate().isBefore(request.getStartDate())
                        || b.getStartDate().isAfter(request.getEndDate())));

        if (isBusy) {
            return ResponseEntity.badRequest().body("Selected service is already booked");
        }

        Booking booking = new Booking();
        booking.setService(service);
        booking.setUser(user);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus(BookingStatus.PENDING);

        Booking createdBooking = bookingService.create(booking);
        return ResponseEntity.ok(toResponse(createdBooking));
    }

    @GetMapping("/my")
    public List<BookingResponse> getMyBookings(
            @RequestParam(name = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(name = "dateTo", required = false) LocalDate dateTo,
            @RequestParam(name = "paid", required = false) Boolean paid) {
        // Получение бронирований текущего пользователя с фильтрацией
        List<Booking> bookings = bookingService.getFiltered(user.getId(), dateFrom, dateTo, paid);
        return bookings.stream()
                .map(this::toResponse)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok("Booking deleted successfully");
    }
}
```,
  caption: [BookingController — endpoints для создания, получения и удаления бронирований]
)

=== ServiceController — управление услугами

Контроллер услуг предоставляет endpoints для получения каталога услуг и управления ими (для администраторов). Поддерживает фильтрацию по категориям.

#figure(
```java
@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ServiceResponse> create(@Valid @RequestBody ServiceRequest request) {
        Category category = categoryService.getById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Service service = Service.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .maxPeople(request.getMaxPeople())
                .category(category)
                .build();

        Service created = serviceService.create(service);
        return ResponseEntity.ok(EntityDtoMapper.toServiceResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getAll() {
        List<ServiceResponse> responses = serviceService.getAll().stream()
                .map(EntityDtoMapper::toServiceResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ServiceResponse>> getByCategory(@PathVariable("categoryId") Long categoryId) {
        List<ServiceResponse> responses = serviceService.getByCategory(category).stream()
                .map(EntityDtoMapper::toServiceResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
```,
  caption: [ServiceController — endpoints для управления услугами и фильтрации по категориям]
)

