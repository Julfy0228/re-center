<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Контакты</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="layout/header.jsp" %>
    <main>
        <h1>Контакты</h1>
        <div class="grid" style="grid-template-columns: 1fr 1fr;">
            <div class="card">
                <h3>Наш адрес</h3>
                <p>г. Владимир, ул. Лесная, д. 1</p>
                <p><strong>Телефон:</strong> +7 (999) 000-00-00</p>
                <p><strong>Email:</strong> info@re-center.ru</p>
                <p><strong>Режим работы:</strong> Круглосуточно</p>
            </div>
            <div class="card" style="background: #eee; display: flex; align-items: center; justify-content: center; min-height: 200px; color: #888;">
                [Место для карты Google/Yandex]
            </div>
        </div>
    </main>
    <%@ include file="layout/footer.jsp" %>
</body>
</html>