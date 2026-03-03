<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html>
<head>
    <title>Личный кабинет</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <div style="display: flex; gap: 20px;">
            <aside style="width: 250px; background: white; padding: 20px; border: 1px solid #ddd; height: fit-content;">
                <h3>Меню</h3>
                <ul style="list-style: none; padding: 0;">
                    <li style="margin-bottom: 10px;"><strong>Мои бронирования</strong></li>
                    <li style="margin-bottom: 10px;"><a href="${pageContext.request.contextPath}/cabinet/profile" style="color: #666; text-decoration: none;">Профиль</a></li>
                    <li style="margin-bottom: 10px;"><a href="${pageContext.request.contextPath}/cabinet/history" style="color: #666; text-decoration: none;">История платежей</a></li>
                </ul>
            </aside>

            <div style="flex: 1;">
                <h2>Активные бронирования</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Услуга</th>
                            <th>Даты</th>
                            <th>Сумма</th>
                            <th>Статус</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${bookings}">
                            <tr>
                                <td>ID услуги: ${b.serviceId}</td>
                                <td>${b.startDate} — ${b.endDate}</td>
                                <td>${b.totalPrice} ₽</td>
                                <td>
                                    <span class="${b.status == 'PAID' ? 'status-paid' : 'status-pending'}">
                                        ${b.status}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty bookings}">
                            <tr><td colspan="4">У вас пока нет активных бронирований.</td></tr>
                        </c:if>
                    </tbody>
                </table>

                <div class="card" style="margin-top: 30px; background: #fdfdfd;">
                    <h3>🎉 Персональные предложения</h3>
                    <p>Для вас доступна скидка 5% на следующее бронирование!</p>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>