<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html>
<head>
    <title>Отчёты</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <h1>Отчёты</h1>
        <div class="card" style="margin-top: 15px;">
            <p>Раздел отчётов (демо для ЛР2).</p>
            <p style="margin-top: 10px;">Доступ: MANAGER, ADMIN.</p>
            <p style="margin-top: 10px;">Данные получены через JPA (EntityManager + JPQL).</p>
        </div>

        <div class="card" style="margin-top: 15px;">
            <h2>Общая статистика</h2>
            <p style="margin-top: 10px;">Пользователей: ${usersCount}</p>
            <p style="margin-top: 10px;">Услуг: ${servicesCount}</p>
            <p style="margin-top: 10px;">Бронирований: ${bookingsCount}</p>
            <p style="margin-top: 10px;">Выручка (PAID): ${totalRevenue}</p>
        </div>

        <div class="card" style="margin-top: 15px;">
            <h2>Бронирования по статусам</h2>
            <table class="table" style="margin-top: 10px; width: 100%;">
                <thead>
                <tr>
                    <th>Статус</th>
                    <th>Кол-во</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="row" items="${bookingsByStatus}">
                    <tr>
                        <td>${row[0]}</td>
                        <td>${row[1]}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="card" style="margin-top: 15px;">
            <h2>Топ-5 услуг по кол-ву бронирований</h2>
            <table class="table" style="margin-top: 10px; width: 100%;">
                <thead>
                <tr>
                    <th>Услуга</th>
                    <th>Кол-во</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="row" items="${topServices}">
                    <tr>
                        <td>${row[0]}</td>
                        <td>${row[1]}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>
