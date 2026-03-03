<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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

        <sec:authorize access="isAnonymous()">
            <a href="${pageContext.request.contextPath}/auth/login" class="btn-small">Войти</a>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <div class="user-block">
                <sec:authorize access="hasRole('ADMIN')">
                    <a href="${pageContext.request.contextPath}/admin" style="color:#e74c3c; margin-right: 15px;">Админка</a>
                </sec:authorize>

                <a href="${pageContext.request.contextPath}/cabinet" class="user-profile-link">
                    <sec:authentication property="principal.username" var="userEmail" />
                    <span>${userEmail}</span>
                    <div class="avatar-circle">
                        ${fn:substring(userEmail, 0, 1)}
                    </div>
                </a>

                <form action="${pageContext.request.contextPath}/auth/logout" method="post" style="display:inline;">
                    <button type="submit" style="all:unset; cursor:pointer; font-size: 0.8em; margin-left: 10px; color: #888; text-decoration: none;">(Выход)</button>
                </form>
            </div>
        </sec:authorize>
    </nav>
</header>