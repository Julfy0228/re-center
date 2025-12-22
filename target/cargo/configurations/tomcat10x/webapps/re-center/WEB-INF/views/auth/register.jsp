<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <div class="form-container">
            <h2 style="text-align: center">Регистрация</h2>

            <c:if test="${not empty error}">
                <div style="color: #721c24; background-color: #f8d7da; border-color: #f5c6cb; padding: 10px; margin-bottom: 15px; border-radius: 4px; text-align: center;">
                    ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth/register" method="post">
                <div class="form-group">
                    <label>Имя *</label>
                    <input type="text" name="firstName" value="${regDto.firstName}" required>
                </div>

                <div class="form-group">
                    <label>Фамилия *</label>
                    <input type="text" name="lastName" value="${regDto.lastName}" required>
                </div>

                <div class="form-group">
                    <label>Телефон</label>
                    <input type="tel" name="phone" value="${regDto.phone}" placeholder="+7...">
                </div>

                <div class="form-group">
                    <label>Email (Логин) *</label>
                    <input type="email" name="email" value="${regDto.email}" required>
                </div>

                <div class="form-group">
                    <label>Пароль *</label>
                    <input type="password" name="password" required>
                </div>

                <button type="submit">Зарегистрироваться</button>
            </form>

            <p style="text-align: center; margin-top: 15px;">
                Уже есть аккаунт? <a href="${pageContext.request.contextPath}/auth/login">Войти</a>
            </p>
        </div>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>