<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<header>
    <a href="${pageContext.request.contextPath}/" class="logo">
        <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="logo-img" onerror="this.style.display='none'">
        Re-Center
    </a>
    <nav>
        <a href="${pageContext.request.contextPath}/">Главная</a>
        <a href="${pageContext.request.contextPath}/services">Услуги</a>
        <a href="${pageContext.request.contextPath}/news">Новости</a>
        <a href="${pageContext.request.contextPath}/contacts">Контакты</a>

        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <div class="user-block">
                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                         <a href="${pageContext.request.contextPath}/admin" style="color:#e74c3c; margin-right: 15px;">Админка</a>
                    </c:if>

                    <a href="${pageContext.request.contextPath}/cabinet" class="user-profile-link">
                        <span>${sessionScope.user.firstName}</span>
                        <div class="avatar-circle">
                            ${sessionScope.user.firstName.charAt(0)}
                        </div>
                    </a>

                    <a href="${pageContext.request.contextPath}/auth/logout" style="font-size: 0.8em; margin-left: 10px; color: #888; text-decoration: none;">(Выход)</a>
                </div>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/auth/login" class="btn-small">Войти</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>