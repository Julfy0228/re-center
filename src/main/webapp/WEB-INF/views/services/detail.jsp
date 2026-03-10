<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<html>
<head>
    <title>${service.title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>

    <main>
        <h1>${service.title}</h1>

        <div class="detail-layout">
            <div class="detail-img">
                📷 ФОТО
            </div>

            <div class="detail-info card">
                <h2>Описание</h2>
                <p>${service.description}</p>
                <p><strong>Вместимость:</strong> ${service.minPeople} - ${service.maxPeople} чел.</p>
                <div class="price">
                    Цена: ${service.basePrice} ₽
                    <small style="color: #666; font-size: 0.6em; font-weight: normal;">
                        (<c:choose>
                            <c:when test="${service.serviceType == 'DAILY'}">за сутки</c:when>
                            <c:otherwise>за час</c:otherwise>
                        </c:choose>)
                    </small>
                </div>

                <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">

                <h3>Забронировать</h3>
                <sec:authorize access="isAnonymous()">
                    <p style="color: #e74c3c; margin-bottom: 15px;">
                        <strong>Пожалуйста, <a href="${pageContext.request.contextPath}/auth/login">войдите в систему</a>, чтобы забронировать услугу</strong>
                    </p>
                </sec:authorize>

                <sec:authorize access="isAuthenticated()">
                    <form action="${pageContext.request.contextPath}/services/${service.id}/book" method="post">
                        <c:choose>
                            <c:when test="${service.serviceType == 'HOURLY'}">
                                <div class="form-group">
                                    <label>Дата</label>
                                    <input type="date" name="startDate" required>
                                </div>
                                <div class="form-group">
                                    <label>С</label>
                                    <input type="time" name="timeFrom" required>
                                </div>
                                <div class="form-group">
                                    <label>До</label>
                                    <input type="time" name="timeTo" required>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="form-group">
                                    <label>Дата заезда</label>
                                    <input type="date" name="startDate" required>
                                </div>
                                <div class="form-group">
                                    <label>Дата выезда</label>
                                    <input type="date" name="endDate" required>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <div class="form-group">
                            <label>Количество гостей</label>
                            <input type="number" name="numberOfGuests" min="1" max="${service.maxPeople}" value="1">
                        </div>

                        <button type="submit">Подтвердить бронирование</button>
                    </form>
                </sec:authorize>
            </div>
        </div>
    </main>

    <%@ include file="../layout/footer.jsp" %>
</body>
</html>