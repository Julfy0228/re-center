<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Профиль пользователя</title>
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
                    <li style="margin-bottom: 10px;"><strong>Профиль</strong></li>
                    <li style="margin-bottom: 10px;"><a href="${pageContext.request.contextPath}/cabinet/history" style="color: #666; text-decoration: none;">История платежей</a></li>
                </ul>
            </aside>

            <div style="flex: 1;">
                <h2>Редактирование профиля</h2>

                <c:if test="${param.success != null}">
                    <div style="background: #dff0d8; color: #3c763d; padding: 10px; margin-bottom: 15px; border-radius: 4px;">Данные успешно сохранены!</div>
                </c:if>

                <div class="card">
                    <form action="${pageContext.request.contextPath}/cabinet/profile" method="post">
                        <div class="form-group">
                            <label>Имя</label>
                            <input type="text" name="firstName" value="${sessionScope.user.firstName}">
                        </div>
                        <div class="form-group">
                            <label>Фамилия</label>
                            <input type="text" name="lastName" value="${sessionScope.user.lastName}">
                        </div>
                        <div class="form-group">
                            <label>Телефон</label>
                            <input type="text" name="phone" value="${sessionScope.user.phone}">
                        </div>
                        <div class="form-group">
                            <label>Email (нельзя изменить)</label>
                            <input type="text" value="${sessionScope.user.email}" disabled style="background: #eee;">
                        </div>
                        <button type="submit" style="width: auto;">Сохранить изменения</button>
                    </form>
                </div>
            </div>
        </div>
    </main>
    <%@ include file="../layout/footer.jsp" %>
</body>
</html>