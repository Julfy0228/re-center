<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
<head>
    <title>Добавить новость</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles.css">
</head>
<body>
    <%@ include file="../layout/header.jsp" %>
    <main>
        <div class="form-container">
            <h2>Публикация новости</h2>
            <form action="${pageContext.request.contextPath}/admin/news/add" method="post">
                <div class="form-group">
                    <label>Заголовок</label>
                    <input type="text" name="title" required>
                </div>
                <div class="form-group">
                    <label>Текст новости</label>
                    <textarea name="content" rows="6" required></textarea>
                </div>
                <button type="submit">Опубликовать</button>
            </form>
        </div>
    </main>
    <%@ include file="../layout/footer.jsp" %>
</body>
</html>