<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
                            <th>Дата</th>
                            <th>Операция</th>
                            <th>Метод</th>
                            <th>Сумма</th>
                            <th>Статус</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>20.05.2025</td>
                            <td>Оплата бронирования</td>
                            <td>Банковская карта</td>
                            <td>5 000 ₽</td>
                            <td class="status-paid">Успешно</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
    <%@ include file="../layout/footer.jsp" %>
</body>
</html>