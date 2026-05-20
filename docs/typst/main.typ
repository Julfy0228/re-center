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

#heading([ Аннотация ], outlined: false)

Пояснительная записка содержит описание проектирования и разработки информационной системы "База отдыха Re-Center" — распределённой веб-системы для автоматизации бизнес-процессов туристического комплекса.

В документе представлены: анализ предметной области, описание пяти основных прецедентов использования, функциональные и нефункциональные требования, архитектура системы, модель данных с 14 сущностями, описание API с DTO классами и контроллерами, результаты тестирования (85 юнит-тестов), три сценария нагрузочного тестирования JMeter, 18 макетов интерфейса и 18 скриншотов реализованного приложения.

Система реализована на Java с использованием Spring Framework, H2 Database для хранения данных и React для фронтенда. Все компоненты протестированы и готовы к развёртыванию.

Пояснительная записка содержит #context(counter(page).final().first()) страниц, #context(counter(image).final().first()) рисунков, #context(counter(table).final().first()) таблиц.

#heading([ Abstract ], outlined: false)

The explanatory note contains a description of the design and development of the "Re-Center Resort" information system — a distributed web system for automating business processes of a tourist complex.

The document presents: analysis of the subject area, description of five main use cases, functional and non-functional requirements, system architecture, data model with 14 entities, API description with DTO classes and controllers, testing results (85 unit tests), three JMeter load testing scenarios, 18 interface mockups and 18 screenshots of the implemented application.

The system is implemented in Java using Spring Framework, H2 Database for data storage and React for the frontend. All components are tested and ready for deployment.

The explanatory note contains #context(counter(page).final().first()) pages, #context(counter(image).final().first()) images, #context(counter(table).final().first()) tables.

#pagebreak()

#outline(
  title: [Содержание],
  depth: 2
)
#pagebreak()

= Введение

Развитие информационных технологий и интернета привело к необходимости создания современных распределённых систем, которые обеспечивают масштабируемость, надёжность и удобство использования. Туристический и рекреационный бизнес не является исключением — базы отдыха и курортные комплексы нуждаются в автоматизации своих бизнес-процессов для повышения эффективности и улучшения качества обслуживания клиентов.

Данный курсовой проект посвящен проектированию и разработке информационной системы "База отдыха Re-Center", которая предназначена для автоматизации ключевых процессов: бронирования услуг, управления клиентами, обработки платежей и публикации актуальной информации. Система разработана с использованием современных технологий и архитектурных подходов, обеспечивающих высокую производительность, безопасность и удобство использования.

Целью проекта является создание полнофункциональной веб-системы, которая позволит базе отдыха эффективно управлять своей деятельностью, предоставляя клиентам удобный интерфейс для бронирования услуг и управления своими заказами. Система должна поддерживать различные роли пользователей: администраторов, менеджеров, зарегистрированных клиентов и гостей.

В пояснительной записке описаны все этапы разработки системы: анализ предметной области, проектирование архитектуры, реализация компонентов, тестирование и развёртывание. Документ содержит диаграммы, макеты интерфейса, описание API, результаты тестирования и скриншоты реализованного приложения.

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

=== Макет 1: Страница входа

#figure(
  image("../diagrams/drawio-export/ui/out-1.png", width: 90%),
  caption: [Макет страницы входа в систему]
) <fig:mockup-1>

=== Макет 2: Страница регистрации

#figure(
  image("../diagrams/drawio-export/ui/out-2.png", width: 90%),
  caption: [Макет страницы регистрации новых пользователей]
) <fig:mockup-2>

=== Макет 3: Каталог услуг

Главная страница предназначена для быстрого ознакомления с возможностями базы и текущими акциями. Содержит блок новостей и популярные услуги.

#figure(
  image("../diagrams/drawio-export/ui/out-3.png", width: 90%),
  caption: [Макет каталога услуг с фильтрацией]
) <fig:mockup-3>

=== Макет 4: Детали услуги

Детальная карточка конкретного предложения сфокусирована на конверсии посетителя в клиента через упрощенный процесс выбора дат.

#figure(
  image("../diagrams/drawio-export/ui/out-4.png", width: 90%),
  caption: [Макет страницы услуги с формой бронирования]
) <fig:mockup-4>

=== Макет 5: Акции и скидки

Раздел с актуальными акциями, скидками и специальными предложениями.

#figure(
  image("../diagrams/drawio-export/ui/out-5.png", width: 90%),
  caption: [Макет раздела акций и скидок]
) <fig:mockup-5>

=== Макет 6: Новости

Блок новостей и объявлений базы отдыха с информацией о событиях и обновлениях.

#figure(
  image("../diagrams/drawio-export/ui/out-6.png", width: 90%),
  caption: [Макет раздела новостей]
) <fig:mockup-6>

=== Макет 7: Мои бронирования

Интерфейс ориентирован на управление существующими бронированиями и мотивацию к повторным заказам.

#figure(
  image("../diagrams/drawio-export/ui/out-7.png", width: 90%),
  caption: [Макет личного кабинета с активными бронированиями]
) <fig:mockup-7>

=== Макет 8: Уведомления

Центр уведомлений с информацией о статусе бронирований, платежах и системных сообщениях.

#figure(
  image("../diagrams/drawio-export/ui/out-8.png", width: 90%),
  caption: [Макет центра уведомлений]
) <fig:mockup-8>

=== Макет 9: Профиль пользователя

Страница профиля с личной информацией, контактными данными и историей активности.

#figure(
  image("../diagrams/drawio-export/ui/out-9.png", width: 90%),
  caption: [Макет страницы профиля пользователя]
) <fig:mockup-9>

=== Макет 10: Админ-панель — Общий экран

Дашборд администратора с ключевыми метриками: количество новых бронирований, выручка, загрузка услуг.

#figure(
  image("../diagrams/drawio-export/ui/out-10.png", width: 90%),
  caption: [Макет дашборда администратора]
) <fig:mockup-10>

=== Макет 11: Управление бронированиями

Интерфейс для просмотра, редактирования и управления всеми бронированиями в системе.

#figure(
  image("../diagrams/drawio-export/ui/out-11.png", width: 90%),
  caption: [Макет управления бронированиями]
) <fig:mockup-11>

=== Макет 12: Управление услугами

Интерфейс для добавления, редактирования и удаления услуг с управлением категориями и ценами.

#figure(
  image("../diagrams/drawio-export/ui/out-12.png", width: 90%),
  caption: [Макет управления услугами]
) <fig:mockup-12>

=== Макет 13: Управление новостями

Интерфейс для создания, редактирования и публикации новостей с поддержкой форматирования.

#figure(
  image("../diagrams/drawio-export/ui/out-13.png", width: 90%),
  caption: [Макет управления новостями]
) <fig:mockup-13>

=== Макет 14: Управление акциями

Интерфейс для создания и управления акциями и скидками с указанием сроков действия.

#figure(
  image("../diagrams/drawio-export/ui/out-14.png", width: 90%),
  caption: [Макет управления акциями]
) <fig:mockup-14>

=== Макет 15: Управление скидками

Интерфейс для управления персональными скидками пользователей по различным критериям.

#figure(
  image("../diagrams/drawio-export/ui/out-15.png", width: 90%),
  caption: [Макет управления скидками]
) <fig:mockup-15>

=== Макет 16: Модерация отзывов

Интерфейс для просмотра и модерации отзывов пользователей с возможностью одобрения или отклонения.

#figure(
  image("../diagrams/drawio-export/ui/out-16.png", width: 90%),
  caption: [Макет модерации отзывов]
) <fig:mockup-16>

=== Макет 17: Управление платежами

Интерфейс для отслеживания платежей, проверки статусов и управления возвратами.

#figure(
  image("../diagrams/drawio-export/ui/out-17.png", width: 90%),
  caption: [Макет управления платежами]
) <fig:mockup-17>

=== Макет 18: Страница ошибки 404

Страница, отображаемая при попытке доступа к несуществующему ресурсу.

#figure(
  image("../diagrams/drawio-export/ui/out-18.png", width: 90%),
  caption: [Макет страницы ошибки 404]
) <fig:mockup-18>

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

= Компоненты бизнес-логики

== Контекстная диаграмма

Контекстная диаграмма показывает взаимодействие системы Re-Center с внешними сущностями и системами. Диаграмма отображает основные потоки данных между системой и пользователями, платежными системами, системой уведомлений и хранилищем данных.

#figure(
  image("../diagrams/context.png", width: 95%),
  caption: [Контекстная диаграмма системы Re-Center]
) <fig:context-diagram>

== Процесс бронирования услуги

Диаграмма описывает полный процесс бронирования услуги от момента выбора клиентом услуги до подтверждения оплаты. Включает проверку доступности, расчет стоимости с применением скидок, создание бронирования и инициирование платежа.

#figure(
  image("../diagrams/booking.png", width: 95%),
  caption: [Диаграмма процесса бронирования услуги]
) <fig:booking-process>

== Регистрация и вход в систему

Диаграмма показывает процессы аутентификации и авторизации пользователей. Включает регистрацию новых пользователей с валидацией данных, вход в систему с проверкой учетных данных и генерацией JWT-токена для защиты ресурсов.

#figure(
  image("../diagrams/auth.png", width: 95%),
  caption: [Диаграмма процесса регистрации и входа в систему]
) <fig:auth-process>

== Управление контентом

Диаграмма описывает процессы управления контентом администраторами: создание и редактирование услуг, публикация новостей, управление акциями и скидками. Показывает взаимодействие между различными компонентами управления и хранилищем данных.

#figure(
  image("../diagrams/manage-content.png", width: 95%),
  caption: [Диаграмма процесса управления контентом]
) <fig:content-management>

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

= Тестирование

== Юнит-тесты

Система содержит полный набор юнит-тестов для всех контроллеров и сервисов. Тесты проверяют корректность бизнес-логики, валидацию данных и обработку ошибок.

#figure(
  table(
    columns: (1fr, 1fr, 1fr),
    align: (left, center, center),
    [*Тестовый класс*], [*Количество тестов*], [*Статус*],
    [ActivityControllerTest], [6], [Пройден],
    [AuthControllerTest], [5], [Пройден],
    [BookingControllerTest], [8], [Пройден],
    [CategoryControllerTest], [7], [Пройден],
    [DiscountControllerTest], [8], [Пройден],
    [NewsControllerTest], [8], [Пройден],
    [NotificationControllerTest], [9], [Пройден],
    [PaymentControllerTest], [7], [Пройден],
    [PromotionControllerTest], [5], [Пройден],
    [ReviewControllerTest], [8], [Пройден],
    [ServiceControllerTest], [7], [Пройден],
    [UserControllerTest], [7], [Пройден],
  ),
  caption: [Результаты юнит-тестирования]
) <tbl:unittests>

*Итоговый результат:* Всего запущено 85 тестов, все пройдены успешно (0 ошибок, 0 пропусков). Время выполнения: 9.790 сек.

== Сценарии нагрузочного тестирования (JMeter)

=== Сценарий 1: Тестирование потока бронирования

*Название:* Re-Center Booking Flow Load Test

*Описание:* Нагрузочное тестирование аутентифицированного потока бронирования. Сценарий включает: вход в систему, просмотр услуг, создание бронирования, просмотр бронирования и удаление бронирования.

*Параметры:*
- Количество потоков (пользователей): 5
- Время разогрева: 30 сек
- Количество итераций: 10
- Время ожидания между запросами: 2000 мс
- Таймаут соединения: 10000 мс

*Основные шаги:*
+ Аутентификация пользователя (POST /api/auth/login)
+ Получение информации текущего пользователя (GET /api/auth/me)
+ Получение списка услуг (GET /api/services)
+ Создание бронирования (POST /api/bookings)
+ Получение моих бронирований (GET /api/bookings/my)
+ Удаление бронирования (DELETE /api/bookings/{id})

*Результаты тестирования:*

#figure(
  image("../../jmeter-tests/results/booking.png", width: 95%),
  caption: [Результаты нагрузочного тестирования потока бронирования]
) <fig:jmeter-booking>

=== Сценарий 2: Тестирование административного потока управления услугами

*Название:* Re-Center Service Admin Flow Load Test

*Описание:* Нагрузочное тестирование административного потока управления услугами. Сценарий проверяет: вход администратора, создание категории, создание услуги, обновление услуги и удаление сущностей каталога.

*Параметры:*
- Количество потоков (администраторов): 5
- Время разогрева: 30 сек
- Количество итераций: 10
- Время ожидания между запросами: 2000 мс
- Таймаут соединения: 10000 мс

*Основные шаги:*
+ Аутентификация администратора (POST /api/auth/login)
+ Проверка роли ADMIN в ответе
+ Получение информации текущего пользователя (GET /api/auth/me)
+ Создание категории услуг (POST /api/categories)
+ Создание новой услуги (POST /api/services)
+ Обновление услуги (PUT /api/services/{id})
+ Удаление услуги (DELETE /api/services/{id})

*Результаты тестирования:*

#figure(
  image("../../jmeter-tests/results/admin.png", width: 95%),
  caption: [Результаты нагрузочного тестирования административного потока]
) <fig:jmeter-admin>

=== Сценарий 3: Тестирование потока профиля пользователя

*Название:* Re-Center Profile Flow Load Test

*Описание:* Нагрузочное тестирование аутентифицированного потока профиля. Сценарий включает: вход в систему, получение информации профиля, обновление профиля и просмотр журнала активности.

*Параметры:*
- Количество потоков (пользователей): 5
- Время разогрева: 30 сек
- Количество итераций: 10
- Время ожидания между запросами: 2000 мс
- Таймаут соединения: 10000 мс

*Основные шаги:*
+ Аутентификация пользователя (POST /api/auth/login)
+ Получение информации текущего пользователя (GET /api/auth/me)
+ Получение профиля пользователя (GET /api/users/{id})
+ Обновление профиля (PUT /api/users/{id})
+ Получение журнала активности (GET /api/activities)

*Результаты тестирования:*

#figure(
  image("../../jmeter-tests/results/profile.png", width: 95%),
  caption: [Результаты нагрузочного тестирования потока профиля]
) <fig:jmeter-profile>

= Реализация интерфейса

Фронтенд-приложение разработано на React с использованием TypeScript и современных инструментов разработки. Интерфейс обеспечивает удобное взаимодействие для всех категорий пользователей: гостей, клиентов и администраторов.

== Страницы аутентификации

=== Страница входа

Позволяет пользователям войти в систему с использованием email и пароля. При успешной аутентификации выдается JWT-токен.

#figure(
  image("../screenshots/01-Вход.png", width: 90%),
  caption: [Страница входа в систему]
) <fig:login-page>

=== Страница регистрации

Форма регистрации новых пользователей с валидацией email, пароля и контактной информации. После регистрации пользователь автоматически получает роль CLIENT.

#figure(
  image("../screenshots/02-Регистрация.png", width: 90%),
  caption: [Страница регистрации новых пользователей]
) <fig:register-page>

== Основные страницы клиента

=== Каталог услуг

Отображает полный список доступных услуг с фильтрацией по категориям, ценой и другим параметрам. Каждая услуга представлена карточкой с фотографией, описанием и кнопкой "Подробнее".

#figure(
  image("../screenshots/03-Каталог-услуг.png", width: 90%),
  caption: [Каталог услуг с фильтрацией и поиском]
) <fig:catalog>

=== Детали услуги

Детальная страница услуги с полной информацией, фотографиями, характеристиками и блоком бронирования. Позволяет выбрать даты и количество участников.

#figure(
  image("../screenshots/04-Детали-услуги.png", width: 90%),
  caption: [Страница с деталями услуги и формой бронирования]
) <fig:service-details>

=== Акции и скидки

Раздел с актуальными акциями, скидками и специальными предложениями. Отображает условия применения скидок и сроки действия.

#figure(
  image("../screenshots/05-Акции-и-скидки.png", width: 90%),
  caption: [Раздел акций и скидок]
) <fig:promotions>

=== Новости

Блок новостей и объявлений базы отдыха. Содержит информацию о событиях, обновлениях и важных сообщениях.

#figure(
  image("../screenshots/06-Новости.png", width: 90%),
  caption: [Раздел новостей и объявлений]
) <fig:news>

=== Мои бронирования

Личный кабинет клиента с таблицей активных и завершенных бронирований. Позволяет просмотреть детали, отменить бронирование или оставить отзыв.

#figure(
  image("../screenshots/07-Мои-бронирования.png", width: 90%),
  caption: [Раздел личного кабинета с активными бронированиями]
) <fig:my-bookings>

=== Уведомления

Центр уведомлений с информацией о статусе бронирований, платежах, новых акциях и системных сообщениях.

#figure(
  image("../screenshots/08-Уведомления.png", width: 90%),
  caption: [Центр уведомлений пользователя]
) <fig:notifications>

=== Профиль пользователя

Страница профиля с личной информацией, контактными данными и историей активности. Позволяет редактировать данные и просматривать статистику.

#figure(
  image("../screenshots/09-Профиль.png", width: 90%),
  caption: [Страница профиля пользователя]
) <fig:profile>

== Административная панель

=== Общий экран управления

Дашборд администратора с ключевыми метриками: количество новых бронирований, выручка, загрузка услуг и статистика пользователей.

#figure(
  image("../screenshots/10-Управление-общий-экран.png", width: 90%),
  caption: [Дашборд администратора с метриками]
) <fig:admin-dashboard>

=== Управление бронированиями

Интерфейс для просмотра, редактирования и управления всеми бронированиями в системе. Позволяет изменять статусы и отслеживать платежи.

#figure(
  image("../screenshots/11-Управление-бронированиями.png", width: 90%),
  caption: [Раздел управления бронированиями]
) <fig:admin-bookings>

=== Управление услугами

Интерфейс для добавления, редактирования и удаления услуг. Позволяет управлять категориями, ценами, описаниями и фотографиями.

#figure(
  image("../screenshots/12-Управление-услугами.png", width: 90%),
  caption: [Раздел управления услугами]
) <fig:admin-services>

=== Управление новостями

Интерфейс для создания, редактирования и публикации новостей. Поддерживает форматирование текста и загрузку изображений.

#figure(
  image("../screenshots/13-Управление-новостями.png", width: 90%),
  caption: [Раздел управления новостями]
) <fig:admin-news>

=== Управление акциями

Интерфейс для создания и управления акциями и скидками. Позволяет применять скидки к услугам или категориям с указанием сроков действия.

#figure(
  image("../screenshots/14-Управление-акциями.png", width: 90%),
  caption: [Раздел управления акциями]
) <fig:admin-promotions>

=== Управление скидками

Интерфейс для управления персональными скидками пользователей. Позволяет назначать скидки по различным критериям.

#figure(
  image("../screenshots/15-Управление-скидками.png", width: 90%),
  caption: [Раздел управления скидками]
) <fig:admin-discounts>

=== Модерация отзывов

Интерфейс для просмотра и модерации отзывов пользователей. Позволяет одобрять, отклонять или удалять отзывы.

#figure(
  image("../screenshots/16-Модерация-отзывов.png", width: 90%),
  caption: [Раздел модерации отзывов]
) <fig:admin-reviews>

=== Управление платежами

Интерфейс для отслеживания платежей, проверки статусов и управления возвратами. Показывает историю всех транзакций.

#figure(
  image("../screenshots/17-Управление-платежами.png", width: 90%),
  caption: [Раздел управления платежами]
) <fig:admin-payments>

=== Страница ошибки 404

Страница, отображаемая при попытке доступа к несуществующему ресурсу. Содержит информацию об ошибке и ссылку на главную страницу.

#figure(
  image("../screenshots/18-Страница-404.png", width: 90%),
  caption: [Страница ошибки 404]
) <fig:error-404>

= Заключение

В ходе выполнения курсового проекта была разработана полнофункциональная информационная система "База отдыха Re-Center", которая решает основные проблемы автоматизации бизнес-процессов туристического комплекса.

Основные достижения проекта:

- Спроектирована архитектура системы с использованием паттернов REST API и многоуровневой архитектуры
- Разработана реляционная база данных с 14 основными сущностями и правильно организованными связями
- Реализован полный набор API endpoints для управления услугами, бронированиями, платежами и контентом
- Создан удобный веб-интерфейс для всех категорий пользователей: гостей, клиентов и администраторов
- Реализована система аутентификации и авторизации с использованием JWT-токенов
- Разработана система управления скидками и акциями с автоматическим расчётом стоимости
- Внедрена система уведомлений для информирования пользователей о статусе их заказов

Качество разработанной системы подтверждается результатами тестирования:

- Все 85 юнит-тестов пройдены успешно
- Нагрузочное тестирование показало стабильную работу системы при одновременном подключении 5 пользователей с 10 итерациями операций
- Система обеспечивает быструю загрузку страниц и отзывчивый интерфейс

Система готова к развёртыванию и использованию в реальной среде. Архитектура позволяет легко масштабировать приложение при увеличении нагрузки и добавлять новые функции без значительных изменений в существующем коде.

= Список использованных источников

+ H2 Database Engine: официальная документация. — URL: https://www.h2database.com/html/main.html (дата обращения: 15.10.2025).

+ Project Lombok: официальная документация. — URL: https://projectlombok.org/ (дата обращения: 20.11.2025).

+ MapStruct: официальная документация. — 2026. — URL: https://mapstruct.org/ (дата обращения: 10.12.2025).

+ Mockito: framework documentation. — 2026. — URL: https://site.mockito.org/ (дата обращения: 15.01.2026).

+ Как упростить себе жизнь: удобные юнит-тесты с JUnit и AssertJ. — URL: https://habr.com/ru/companies/skillbox/articles/767804/ (дата обращения: 20.02.2026).

+ Первые шаги в Spring Security с JWT. — 2025. — URL: https://habr.com/ru/companies/spring_aio/articles/909448/ (дата обращения: 15.03.2026).

+ Первое приложение на Spring Boot + ReactJS. — URL: https://habr.com/ru/users/kadmser/ (дата обращения: 15.05.2026).
