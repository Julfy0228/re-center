<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html>
<head>
    <title>История платежей</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>
    <main>
        <div style="display: flex; gap: 20px;">
            <aside style="width: 250px; background: white; padding: 20px; border: 1px solid #ddd;">
                <h3>Меню</h3>
                <ul style="list-style: none; padding: 0;">
                    <li style="margin-bottom: 10px;"><a href="${pageContext.request.contextPath}/cabinet" style="color: #666; text-decoration: none;">Мои бронирования</a></li>
                    <li style="margin-bottom: 10px;"><a href="${pageContext.request.contextPath}/cabinet/profile" style="color: #666; text-decoration: none;">Профиль</a></li>
                    <li style="margin-bottom: 10px;"><strong>История платежей</strong></li>
                </ul>
            </aside>

            <div style="flex: 1;">
                <h2>История транзакций</h2>
                <table>
                    <thead>
                        <tr>
                            <th>ID брони</th>
                            <th>Услуга</th>
                            <th>Даты</th>
                            <th>Сумма</th>
                            <th>Статус</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${bookings}">
                            <tr>
                                <td>${b.id}</td>
                                <td>${b.serviceTitle} (ID: ${b.serviceId})</td>
                                <td>${b.startDate} — ${b.endDate}</td>
                                <td>${b.totalPrice} ₽</td>
                                <c:choose>
                                    <c:when test="${b.status == 'PAID'}">
                                        <td class="status-paid">${b.status}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="status-pending">${b.status}</td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bookings}">
                            <tr>
                                <td colspan="5">Пока нет данных.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
    <%@ include file="../layout/footer.jsp" %>
</body>
</html>