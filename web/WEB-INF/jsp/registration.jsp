<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Регистрация</title>
</head>
<body>

<img src="${pageContext.request.contextPath}/images/userImage/image.PNG" alt="User Image">

<form action="${pageContext.request.contextPath}/registration" method="post" enctype="multipart/form-data">
    <label for="userName">Name:
        <input type="text" name="userName" id="userName" required>
    </label><br>
    <label for="birthday">Birthday:
        <input type="date" translate="no" name="birthday" id="birthday" required>
    </label><br>
    <label for="image">Image:
        <input type="file" name="image" id="image" required>
    </label><br>
    <label for="email">Email:
        <input type="text" name="email" id="email" required>
    </label><br>
    <label for="password">Password:
        <input type="password" name="password" id="password" required>
    </label><br>
    <select name="role" id="role">
        <c:forEach var="role" items="${requestScope.roles}">
            <option value="${role}">${role}</option>
        </c:forEach>
    </select><br>
    <c:forEach var="gender" items="${requestScope.genders}">
        <input type="radio" name="gender" value="${gender}" required> ${gender.getName()}
        <br>
    </c:forEach>
    <button type="submit">Send</button>
    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <span>${error.getMessage()}</span><br>
            </c:forEach>
        </div>
    </c:if>
</form>
</body>
</html>