<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
    <title>Добавить услугу</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>
    <main>
        <div class="form-container" style="max-width: 600px;">
            <h2>Новая услуга</h2>
            <form action="${pageContext.request.contextPath}/admin/services/add" method="post">
                <div class="form-group">
                    <label>Название</label>
                    <input type="text" name="title" required>
                </div>
                <div class="form-group">
                    <label>Описание</label>
                    <textarea name="description" rows="4"></textarea>
                </div>
                <div class="form-group">
                    <label>Цена (руб.)</label>
                    <input type="number" name="basePrice" required>
                </div>
                <div class="form-group">
                    <label>Тип</label>
                    <select name="serviceType">
                        <option value="DAILY">Посуточно</option>
                        <option value="HOURLY">Почасовая</option>
                    </select>
                </div>
                <div style="display: flex; gap: 10px;">
                    <div class="form-group" style="flex: 1;">
                        <label>Мин. чел.</label>
                        <input type="number" name="minCapacity" value="1">
                    </div>
                    <div class="form-group" style="flex: 1;">
                        <label>Макс. чел.</label>
                        <input type="number" name="maxCapacity" value="4">
                    </div>
                </div>
                <button type="submit">Сохранить</button>
            </form>
        </div>
    </main>
    <%@ include file="../layout/footer.jsp" %>
</body>
</html>