<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Вход в систему</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <div class="form-container">
            <h2 style="text-align: center">Вход</h2>

            <c:if test="${not empty error}">
                <div style="color: #721c24; background-color: #f8d7da; border-color: #f5c6cb; padding: 10px; margin-bottom: 15px; border-radius: 4px; text-align: center;">
                    ${error}
                </div>
            </c:if>

            <c:if test="${param.registered != null}">
                <div style="color: #155724; background-color: #d4edda; border-color: #c3e6cb; padding: 10px; margin-bottom: 15px; border-radius: 4px; text-align: center;">
                    Регистрация успешна! Войдите в систему.
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/auth/login" method="post">
                <div class="form-group">
                    <label>Email</label>
                    <input type="text" name="email" required>
                </div>
                <div class="form-group">
                    <label>Пароль</label>
                    <input type="password" name="password" required>
                </div>
                <button type="submit">Войти</button>
            </form>

            <p style="text-align: center; margin-top: 15px;">
                Нет аккаунта? <a href="${pageContext.request.contextPath}/auth/register">Зарегистрироваться</a>
            </p>
        </div>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>