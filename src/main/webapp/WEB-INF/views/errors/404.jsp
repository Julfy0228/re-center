<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
<head>
    <title>404</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <h1>404 — Страница не найдена</h1>
        <div class="card" style="margin-top: 15px;">
            <p>Запрошенная страница не существует.</p>
            <p style="margin-top: 10px;">
                <a href="${pageContext.request.contextPath}/" class="btn-small">На главную</a>
            </p>
        </div>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>
