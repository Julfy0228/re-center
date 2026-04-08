# Re-Center

Веб-приложение для базы отдыха с backend на Spring MVC и frontend на React.

Сейчас в проекте есть:

- JWT-аутентификация и роли пользователей
- каталог услуг и подробная страница услуги
- бронирования, оплаты и отзывы
- новости, уведомления, акции и скидки
- админ-панель для управления контентом
- загрузка изображений для услуг и новостей

## Стек

- Java 17
- Maven
- Spring MVC
- Spring Security
- Hibernate / JPA
- React
- Axios

## Структура проекта

- [`backend/`](backend) — серверная часть
- [`frontend/`](frontend) — клиентская часть
- [`pom.xml`](pom.xml) — корневой Maven-проект

## Backend

Backend запускается на:

- `http://localhost:8080/re-center`

API находится по адресу:

- `http://localhost:8080/re-center/api`

Загрузка картинок отдаётся через:

- `http://localhost:8080/re-center/uploads/...`

### Запуск backend

Из корня проекта:

```powershell
mvn -pl backend cargo:run -DskipTests
```

Если нужен полный прогон тестов:

```powershell
mvn -pl backend test
```

## Frontend

Frontend разработческий сервер обычно работает на:

- `http://localhost:3000`

### Важно про запуск frontend

Фронт использует `react-scripts`. Если команда `npm start` пишет, что `react-scripts` не найден, значит зависимости не установлены.

Тогда в папке `frontend` нужно выполнить:

```powershell
npm install
```

После этого запуск:

```powershell
npm start
```

Если вы запускаете локальный Node из папки `frontend/node`, команды будут такие:

```powershell
.\node\npm.cmd install
.\node\npm.cmd start
```

## Работа с другого устройства по локальной сети

Проект уже настроен так, чтобы frontend мог обращаться к backend не только через `localhost`, но и через IP машины в локальной сети.

Что нужно:

1. Запустить backend на основном компьютере.
2. Запустить frontend на основном компьютере.
3. Узнать IP основного компьютера в локальной сети.
4. Открывать сайт с другого устройства по адресу вида:

```text
http://192.168.1.50:3000
```

Frontend сам формирует API-адрес на основе текущего хоста:

- если сайт открыт на `192.168.1.50:3000`, запросы пойдут на `192.168.1.50:8080/re-center/api`

Если с другого устройства сайт открывается, но backend недоступен, проверьте:

- что backend реально запущен
- что порт `8080` не блокируется фаерволом
- что frontend открыт именно по IP, а не по `localhost`

## Загрузка картинок

Загрузка изображений работает через endpoint:

- `POST /api/uploads/images`

Картинки можно прикреплять в админ-панели при создании и редактировании:

- услуг
- новостей

Файлы сохраняются в папку `backend/uploads`.

Поддерживаются форматы:

- `jpg`
- `jpeg`
- `png`
- `webp`
- `gif`

## Основные пользовательские разделы

- каталог услуг
- страница услуги
- мои бронирования
- новости
- уведомления
- акции и скидки
- профиль

## Основные админские разделы

- управление услугами
- управление новостями
- управление бронированиями
- управление отзывами
- управление платежами
- управление промоакциями
- управление скидками

## Полезные файлы

- [`frontend/src/App.jsx`](frontend/src/App.jsx) — роутинг и shell приложения
- [`frontend/src/App.css`](frontend/src/App.css) — основные стили
- [`frontend/src/api/axios.js`](frontend/src/api/axios.js) — конфигурация API
- [`backend/src/main/java/com/recenter/config/SecurityConfig.java`](backend/src/main/java/com/recenter/config/SecurityConfig.java) — security и CORS
- [`backend/src/main/java/com/recenter/config/WebConfig.java`](backend/src/main/java/com/recenter/config/WebConfig.java) — MVC и resource handlers
- [`backend/src/main/java/com/recenter/controller/`](backend/src/main/java/com/recenter/controller) — контроллеры backend

## Замечания

- Старые `run.bat`-скрипты могут быть неактуальны.
- Если frontend не стартует, сначала проверьте наличие `react-scripts`.
- После изменений в backend, связанных с multipart, security или resource handlers, backend нужно перезапускать полностью.
