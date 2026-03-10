<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Пользователи</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <h1>Пользователи</h1>

        <c:if test="${not empty created}">
            <div class="card" style="border-left: 4px solid #28a745; margin-bottom: 15px;">
                Пользователь создан.
            </div>
        </c:if>
        <c:if test="${error == 'duplicate'}">
            <div class="card" style="border-left: 4px solid #dc3545; margin-bottom: 15px;">
                Такой email уже существует.
            </div>
        </c:if>
        <c:if test="${error == 'validation'}">
            <div class="card" style="border-left: 4px solid #dc3545; margin-bottom: 15px;">
                Проверьте введённые данные.
            </div>
        </c:if>

        <p style="margin-bottom: 15px;">
            <a href="${pageContext.request.contextPath}/admin/users/create" class="btn-small" style="background: var(--primary); color: white; text-decoration:none;">+ Создать пользователя</a>
        </p>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Email</th>
                    <th>Имя</th>
                    <th>Фамилия</th>
                    <th>Телефон</th>
                    <th>Роль</th>
                    <th>Действие</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.email}</td>
                        <td>${u.firstName}</td>
                        <td>${u.lastName}</td>
                        <td>${u.phone}</td>
                        <td>${u.role}</td>
                        <td>
                            <div style="display:flex; gap:8px; align-items:center; flex-wrap: wrap;">
                                <form action="${pageContext.request.contextPath}/admin/users/${u.id}/role" method="post" style="display:flex; gap:8px; align-items:center;">
                                    <select name="role">
                                        <option value="CLIENT" <c:if test="${u.role == 'CLIENT'}">selected</c:if>>CLIENT</option>
                                        <option value="MANAGER" <c:if test="${u.role == 'MANAGER'}">selected</c:if>>MANAGER</option>
                                        <option value="ADMIN" <c:if test="${u.role == 'ADMIN'}">selected</c:if>>ADMIN</option>
                                    </select>
                                    <button type="submit" class="btn-small" style="background: var(--primary); color: white;">Сохранить</button>
                                </form>

                                <form action="${pageContext.request.contextPath}/admin/users/${u.id}/delete" method="post" onsubmit="return confirm('Удалить пользователя?');">
                                    <button type="submit" class="btn-small" style="background: #dc3545; color: white;">Удалить</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p style="margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/admin" class="btn-small" style="background: var(--primary); color: white;">Назад в админку</a>
        </p>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>
