<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Создать пользователя</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <h1>Создать пользователя</h1>

        <form method="post" action="${pageContext.request.contextPath}/admin/users/create" class="card" style="max-width: 520px;">
            <div class="form-group">
                <label>Email (логин)</label>
                <input type="text" name="email" required>
            </div>

            <div class="form-group">
                <label>Пароль</label>
                <input type="password" name="password" required>
            </div>

            <div class="form-group">
                <label>Имя</label>
                <input type="text" name="firstName">
            </div>

            <div class="form-group">
                <label>Фамилия</label>
                <input type="text" name="lastName">
            </div>

            <div class="form-group">
                <label>Телефон</label>
                <input type="text" name="phone">
            </div>

            <div class="form-group">
                <label>Роль</label>
                <select name="role" required>
                    <option value="CLIENT">CLIENT</option>
                    <option value="MANAGER">MANAGER</option>
                    <option value="ADMIN">ADMIN</option>
                </select>
            </div>

            <div style="display:flex; gap:10px; align-items:center;">
                <button type="submit" class="btn-small" style="background: var(--primary); color: white;">Создать</button>
                <a class="btn-small" href="${pageContext.request.contextPath}/admin/users" style="background: var(--primary); color: white; text-decoration:none;">Назад</a>
            </div>
        </form>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>
