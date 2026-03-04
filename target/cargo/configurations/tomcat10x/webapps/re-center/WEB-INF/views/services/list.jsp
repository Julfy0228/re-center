<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html>
<head>
    <title>Каталог услуг</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <h1>Наши услуги</h1>
        <div class="grid">
            <c:forEach var="service" items="${services}">
                <div class="card">
                    <h3>${service.title}</h3>
                    <p style="min-height: 40px;">${service.description}</p>
                    <div class="price">
                        ${service.basePrice} ₽ /
                        <c:choose>
                            <c:when test="${service.serviceType == 'DAILY'}">Сутки</c:when>
                            <c:otherwise>Час</c:otherwise>
                        </c:choose>
                    </div>
                    <a href="${pageContext.request.contextPath}/services/${service.id}" class="btn-small">Подробнее</a>
                </div>
            </c:forEach>
            <c:if test="${empty services}">
                <p>Услуг пока нет.</p>
            </c:if>
        </div>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>