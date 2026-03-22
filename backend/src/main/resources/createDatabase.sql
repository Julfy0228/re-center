PRAGMA foreign_keys = ON;

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS Users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    role TEXT NOT NULL,
    firstName TEXT,
    lastName TEXT,
    middleName TEXT,
    email TEXT NOT NULL UNIQUE,
    phoneNumber TEXT UNIQUE,
    passwordHash TEXT,
    createdAt TIMESTAMP,
    enabled INTEGER NOT NULL DEFAULT 1
);

-- Таблица категорий услуг
CREATE TABLE IF NOT EXISTS Categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT
);

-- Таблица услуг
CREATE TABLE IF NOT EXISTS Services (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    duration INTEGER,
    price NUMERIC(10,2),
    maxPeople INTEGER,
    categoryId INTEGER,
    FOREIGN KEY (categoryId) REFERENCES Categories(id) ON DELETE SET NULL
);

-- Таблица бронирований
CREATE TABLE IF NOT EXISTS Bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    serviceId INTEGER NOT NULL,
    startDate TIMESTAMP,
    peopleCount INTEGER,
    initialPrice NUMERIC(10,2),
    status TEXT,
    createdAt TIMESTAMP NOT NULL,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (serviceId) REFERENCES Services(id) ON DELETE CASCADE
);

-- Таблица платежей
CREATE TABLE IF NOT EXISTS Payments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    bookingId INTEGER NOT NULL,
    amount NUMERIC(10,2),
    paymentDate TIMESTAMP,
    status TEXT,
    paymentMethod TEXT,
    FOREIGN KEY (bookingId) REFERENCES Bookings(id) ON DELETE CASCADE
);

-- Таблица отзывов
CREATE TABLE IF NOT EXISTS Reviews (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    bookingId INTEGER NOT NULL UNIQUE,
    content TEXT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    status TEXT NOT NULL DEFAULT 'PENDING',
    createdAt TIMESTAMP,
    FOREIGN KEY (bookingId) REFERENCES Bookings(id) ON DELETE CASCADE
);

-- Таблица скидок
CREATE TABLE IF NOT EXISTS Discounts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    startDate TIMESTAMP,
    endDate TIMESTAMP,
    type TEXT NOT NULL,
    value NUMERIC(10,2)
);

-- Таблица выданных пользователям скидок
CREATE TABLE IF NOT EXISTS UserDiscounts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    discountId INTEGER NOT NULL,
    isUsed INTEGER NOT NULL DEFAULT 0,
    expireDate TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (discountId) REFERENCES Discounts(id) ON DELETE CASCADE
);

-- Таблица акций
CREATE TABLE IF NOT EXISTS Promotions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    startDate TIMESTAMP,
    endDate TIMESTAMP
);

-- Таблица связей акций с конкретными услугами
CREATE TABLE IF NOT EXISTS PromotionServices (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    promotionId INTEGER NOT NULL,
    serviceId INTEGER NOT NULL,
    discountPercent NUMERIC(5,2),
    FOREIGN KEY (promotionId) REFERENCES Promotions(id) ON DELETE CASCADE,
    FOREIGN KEY (serviceId) REFERENCES Services(id) ON DELETE CASCADE
);

-- Таблица уведомлений
CREATE TABLE IF NOT EXISTS Notifications (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    type TEXT NOT NULL,
    title TEXT NOT NULL,
    message TEXT NOT NULL,
    read INTEGER NOT NULL DEFAULT 0,
    createdAt TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE
);

-- Таблица новостей
CREATE TABLE IF NOT EXISTS News (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    authorId INTEGER NOT NULL,
    status TEXT NOT NULL,
    publishedAt TIMESTAMP,
    FOREIGN KEY (authorId) REFERENCES Users(id) ON DELETE CASCADE
);

-- Таблица аудита (активности пользователей)
CREATE TABLE IF NOT EXISTS Activities (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    type TEXT NOT NULL,
    details TEXT,
    createdAt TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE
);
