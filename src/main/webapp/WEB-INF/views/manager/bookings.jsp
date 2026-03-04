<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Бронирования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <h1>Бронирования</h1>

        <div class="card" style="margin-bottom: 20px;">
            <form method="get" action="${pageContext.request.contextPath}/manager/bookings" style="display:flex; gap: 10px; align-items: end; flex-wrap: wrap;">
                <div class="form-group" style="margin:0;">
                    <label>Фильтр по статусу</label>
                    <select name="status">
                        <option value="" <c:if test="${empty param.status}">selected</c:if>>Все</option>
                        <option value="CREATED" <c:if test="${param.status == 'CREATED'}">selected</c:if>>CREATED</option>
                        <option value="CONFIRMED" <c:if test="${param.status == 'CONFIRMED'}">selected</c:if>>CONFIRMED</option>
                        <option value="PAID" <c:if test="${param.status == 'PAID'}">selected</c:if>>PAID</option>
                        <option value="CANCELED" <c:if test="${param.status == 'CANCELED'}">selected</c:if>>CANCELED</option>
                    </select>
                </div>
                <button type="submit" class="btn-small" style="background: var(--primary); color: white;">Применить</button>
                <a href="${pageContext.request.contextPath}/manager/bookings" class="btn-small" style="background: var(--primary); color: white; text-decoration:none;">Сбросить</a>
            </form>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Клиент</th>
                    <th>Услуга</th>
                    <th>Даты</th>
                    <th>Сумма</th>
                    <th>Статус</th>
                    <th>Изменить статус</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="b" items="${bookings}">
                    <tr>
                        <td>${b.id}</td>
                        <td>${b.user_email}</td>
                        <td>${b.service_title}</td>
                        <td>${b.start_date} — ${b.end_date}</td>
                        <td>${b.total_price}</td>
                        <td>${b.status}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/manager/bookings/${b.id}/status" method="post" style="display:flex; gap:8px; align-items:center;">
                                <select name="status">
                                    <option value="CREATED" <c:if test="${b.status == 'CREATED'}">selected</c:if>>CREATED</option>
                                    <option value="CONFIRMED" <c:if test="${b.status == 'CONFIRMED'}">selected</c:if>>CONFIRMED</option>
                                    <option value="PAID" <c:if test="${b.status == 'PAID'}">selected</c:if>>PAID</option>
                                    <option value="CANCELED" <c:if test="${b.status == 'CANCELED'}">selected</c:if>>CANCELED</option>
                                </select>
                                <button type="submit" class="btn-small" style="background: var(--primary); color: white;">Сохранить</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty bookings}">
                    <tr>
                        <td colspan="7">Нет бронирований</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <p style="margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/" class="btn-small" style="background: var(--primary); color: white;">На главную</a>
        </p>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>
