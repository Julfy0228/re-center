<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Re-Center — Главная</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <main>
        <section class="news">
            <h2>Новости</h2>
            <div class="card">Открытие летнего сезона — 15 мая 2025</div>
            <div class="card">Акция для групп — 10 мая 2025</div>
        </section>

        <section class="popular">
            <h2>Популярные услуги</h2>
            <div class="card">
                <h3>Проживание в коттедже</h3>
                <p>от 5000₽/сутки</p>
                <a href="service-detail.jsp">Подробнее</a>
            </div>
            <div class="card">
                <h3>Экскурсии</h3>
                <p>от 7500₽</p>
                <a href="service-detail.jsp">Подробнее</a>
            </div>
        </section>
    </main>

    <%@ include file="footer.jsp" %>
</body>
</html>
