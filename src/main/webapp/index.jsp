<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<html>
<head>
    <title>Re-Center - База отдыха</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .main-container {
            min-height: 80vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand fw-bold" href="/re-center/">
                <i class="fas fa-umbrella-beach me-2"></i>Re-Center
            </a>
            
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="/re-center/booking">
                    <i class="fas fa-calendar-check me-1"></i>Бронирование
                </a>
                <a class="nav-link" href="/re-center/login">
                    <i class="fas fa-sign-in-alt me-1"></i>Войти
                </a>
                <a class="nav-link" href="/re-center/register">
                    <i class="fas fa-user-plus me-1"></i>Регистрация
                </a>
            </div>
        </div>
    </nav>

    <div class="main-container">
        <div class="text-center">
            <h1 class="display-4 mb-4">Добро пожаловать в Re-Center</h1>
            <p class="lead text-muted mb-4">Система бронирования базы отдыха</p>
            
            <div class="d-grid gap-2 d-md-block">
                <a href="/re-center/booking" class="btn btn-primary btn-lg me-md-3">
                    <i class="fas fa-calendar-plus me-2"></i>Начать бронирование
                </a>
                <a href="/re-center/register" class="btn btn-outline-primary btn-lg">
                    <i class="fas fa-user me-2"></i>Создать аккаунт
                </a>
            </div>
        </div>
    </div>

    <footer class="bg-dark text-light py-4 mt-5">
        <div class="container text-center">
            <p class="mb-0">&copy; 2025 Re-Center. Система бронирования.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>