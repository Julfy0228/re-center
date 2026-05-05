# JMeter Tests - Lab4

Автоматизированное тестирование нагрузки для проекта Re-Center.

## Структура папки

```
jmeter-tests/
├── run-tests.bat              # Батник для запуска (Windows)
├── run-tests.ps1              # PowerShell скрипт
├── lab4-users.csv             # Тестовые пользователи
├── lab4-admin-users.csv       # Тестовые администраторы
├── results/                   # Результаты тестов (создаётся автоматически)
└── README.md                  # Этот файл
```

## Что делает скрипт

1. ✅ Проверяет доступность бэкенда (ждёт до 60 секунд)
2. ✅ Очищает базу данных
3. ✅ Регистрирует тестовых пользователей из CSV файлов
4. ✅ Запускает 3 JMeter теста последовательно:
   - **Booking Flow** — тест бронирования услуг
   - **Admin Service Flow** — тест управления услугами администратором
   - **Profile Flow** — тест работы с профилем пользователя
5. ✅ Собирает результаты в папку `results/`
6. ✅ Выводит итоговый отчёт

## Требования

- **JMeter** установлен и добавлен в PATH
- **Бэкенд** запущен на `http://localhost:8080/re-center`
- **PowerShell** 5.0+ (для Windows)

## Быстрый старт

### 1. Запусти бэкенд (в отдельном терминале)

```powershell
cd ..\..\backend
mvn spring-boot:run
```

Дождись, пока бэкенд запустится на `http://localhost:8080/re-center`

### 2. Запусти тесты

**Вариант A — Двойной клик (самый простой)**
```
Двойной клик на run-tests.bat
```

**Вариант B — PowerShell**
```powershell
cd jmeter-tests
.\run-tests.ps1
```

**Вариант C — С параметрами**
```powershell
.\run-tests.ps1 -BaseUrl "http://192.168.1.100:8080/re-center/api"
```

### 3. Посмотри результаты

```powershell
# Откроется папка с результатами
explorer .\results
```

## Параметры скрипта

```powershell
# Кастомный URL бэкенда
.\run-tests.ps1 -BaseUrl "http://192.168.1.100:8080/re-center/api"

# Кастомный путь к бэкенду
.\run-tests.ps1 -BackendPath "C:\path\to\backend"

# Кастомная папка результатов
.\run-tests.ps1 -ResultsDir "C:\test-results"

# Все параметры вместе
.\run-tests.ps1 -BaseUrl "http://192.168.1.100:8080/re-center/api" `
                -BackendPath "C:\path\to\backend" `
                -ResultsDir "C:\test-results"
```

## Структура результатов

```
results/
├── Booking_Flow_20260505_163000.jtl
├── Booking_Flow_20260505_163000.log
├── Admin_Service_Flow_20260505_163500.jtl
├── Admin_Service_Flow_20260505_163500.log
├── Profile_Flow_20260505_164000.jtl
└── Profile_Flow_20260505_164000.log
```

- `.jtl` — результаты тестов (CSV формат)
- `.log` — логи JMeter

## Интерпретация результатов

Скрипт выводит для каждого теста:

```
[16:30:00] [Success] Booking Flow: 50/50 успешно (100%) за 45.23s
```

Где:
- **50/50** — количество успешных запросов из общего числа
- **100%** — процент успеха (success rate)
- **45.23s** — время выполнения теста

## Тестовые данные

### Обычные пользователи (lab4-users.csv)
```
lab4_user1@example.com / lab41234
lab4_user2@example.com / lab41234
lab4_user3@example.com / lab41234
lab4_user4@example.com / lab41234
lab4_user5@example.com / lab41234
```

### Администраторы (lab4-admin-users.csv)
```
lab4_admin1@example.com / lab41234
lab4_admin2@example.com / lab41234
lab4_admin3@example.com / lab41234
lab4_admin4@example.com / lab41234
lab4_admin5@example.com / lab41234
```

## Решение проблем

### Бэкенд не запускается
```powershell
# Проверь, что бэкенд запущен
curl http://localhost:8080/re-center/api/auth/me
```

### JMeter не найден
```powershell
# Проверь версию
jmeter --version

# Если не работает, установи переменную окружения
[Environment]::SetEnvironmentVariable("JMETER_HOME", "C:\Apache\JMeter", "User")
[Environment]::SetEnvironmentVariable("PATH", "$env:PATH;C:\Apache\JMeter\bin", "User")

# Перезагрузи PowerShell и попробуй снова
```

### БД не очищается
```powershell
# Удали БД вручную
rm -r ..\..\backend\data\db*
```

### Ошибка "ExecutionPolicy"
```powershell
# Разреши выполнение скриптов
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

## Дополнительно

- Все логи сохраняются с временными метками
- Скрипт не требует ручного вмешательства
- Можно запускать несколько раз подряд
- Результаты не перезаписываются (каждый запуск — новые файлы)
- Тесты запускаются **последовательно**, не параллельно

## Контакты

Если возникли проблемы, проверь:
1. Бэкенд запущен и доступен
2. JMeter установлен и в PATH
3. CSV файлы находятся в папке jmeter-tests
4. Достаточно места на диске для результатов
