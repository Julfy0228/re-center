<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Админ-панель</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <h1>Админ-панель</h1>

        <div class="stats-row">
            <div class="stat-box">
                <div>Новые брони</div>
                <div class="stat-num">${newBookingsCount}</div>
            </div>
            <div class="stat-box">
                <div>Выручка (сегодня)</div>
                <div class="stat-num">${revenue} ₽</div>
            </div>
             <div class="stat-box">
                <div>Свободные номера</div>
                <div class="stat-num">${freeRooms}</div>
            </div>
        </div>

        <div style="display: flex; gap: 20px; margin-bottom: 20px;">
            <a href="${pageContext.request.contextPath}/admin/services/add" class="btn-small" style="background: var(--primary); color: white;">+ Добавить услугу</a>
            <a href="${pageContext.request.contextPath}/admin/news/add" class="btn-small" style="background: var(--primary); color: white;">+ Создать новость</a>
        </div>

        <h2>Последние бронирования</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Клиент</th>
                    <th>Сумма</th>
                    <th>Статус</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>#1254</td>
                    <td>Тестовый Клиент</td>
                    <td>10 000 ₽</td>
                    <td class="status-paid">Оплачено</td>
                </tr>
            </tbody>
        </table>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>