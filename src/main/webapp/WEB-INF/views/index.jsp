<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Re-Center — Главная</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="layout/header.jsp" %>

    <main>
        <section class="news-section">
            <h2>Последние новости</h2>
            <div class="grid">
                <c:forEach var="news" items="${newsList}">
                    <div class="card">
                        <h3>${news.title}</h3>
                        <p>${news.content}</p>
                        <small style="color:#888;">${news.publicationDate}</small>
                    </div>
                </c:forEach>
                <c:if test="${empty newsList}">
                    <p>Новостей пока нет.</p>
                </c:if>
            </div>
            <div style="margin-top: 10px;">
                <a href="${pageContext.request.contextPath}/news">Все новости →</a>
            </div>
        </section>

        <section class="services-section" style="margin-top: 40px;">
            <h2>Популярные услуги</h2>
            <div class="grid">
                <c:forEach var="srv" items="${popServices}">
                    <div class="card">
                        <h3>${srv.title}</h3>
                        <div class="price">от ${srv.basePrice} ₽</div>
                        <a href="${pageContext.request.contextPath}/services/${srv.id}" class="btn-small">Подробнее</a>
                    </div>
                </c:forEach>
                <c:if test="${empty popServices}">
                    <p>Список услуг пуст.</p>
                </c:if>
            </div>
        </section>
    </main>

    <%@ include file="layout/footer.jsp" %>
</body>
</html>