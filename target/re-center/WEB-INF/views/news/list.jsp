<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Новости</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>
    <main>
        <h1>Новости и события</h1>
        <div class="grid">
            <c:forEach var="n" items="${newsList}">
                <div class="card">
                    <h3>${n.title}</h3>
                    <small style="color: #888">${n.publicationDate}</small>
                    <p>${n.content}</p>
                </div>
            </c:forEach>
            <c:if test="${empty newsList}">
                <p>Новостей пока нет.</p>
            </c:if>
        </div>
    </main>
    <%@ include file="../layout/footer.jsp" %>
</body>
</html>