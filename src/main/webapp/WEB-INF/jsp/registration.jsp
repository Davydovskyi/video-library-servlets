<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<%@include file="header.jsp" %>

<img src="${pageContext.request.contextPath}/images/applicationImage/image.PNG" alt="Welcome Image"/>

<form action="${pageContext.request.contextPath}/registration" method="post" enctype="multipart/form-data">
    <label for="user_name"><fmt:message key="page.registration.name"/>
        <input type="text" name="user_name" id="user_name" required>
    </label><br>
    <label for="birthday"><fmt:message key="page.registration.birthday"/>
        <input type="date" name="birthday" id="birthday" required>
    </label><br>
    <label for="image"><fmt:message key="page.registration.image"/>
        <input type="file" name="image" id="image" required>
    </label><br>
    <label for="email"><fmt:message key="page.registration.email"/>
        <input type="text" name="email" id="email" required>
    </label><br>
    <label for="password"><fmt:message key="page.registration.password"/>
        <input type="password" name="password" id="password" required>
    </label><br>
    <c:forEach var="gender" items="${requestScope.genders}">
        <label for="gender">
            <input type="radio" name="gender" id="gender" value="${gender}" required>
        </label><fmt:message key="${gender.getCode()}"/>
        <br>
    </c:forEach>

    <c:if test="${sessionScope.user.role().name() != 'SUPER_ADMIN'}">
        <input type="hidden" name="role" value="USER">
    </c:if>

    <c:if test="${sessionScope.user.role().name() == 'SUPER_ADMIN'}">
        <label for="role"></label><select name="role" id="role" required>
        <c:forEach var="role" items="${requestScope.roles}">
            <option value="${role}">${role}</option>
        </c:forEach>
    </select><br>
    </c:if>

    <button type="submit"><fmt:message key="page.registration.send"/></button>
    <c:if test="${sessionScope.user.role().name() == 'SUPER_ADMIN'}">
        <a href="${pageContext.request.contextPath}/admin">
            <button type="button"><fmt:message key="page.registration.backToAdmin.button"/></button>
        </a>
    </c:if>

    <c:if test="${not empty requestScope.success}">
        <div style="color: green">
            <span><fmt:message key="add.success"/></span><br>
        </div>
    </c:if>

    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <span><fmt:message key="${error.getCode()}"/></span><br>
            </c:forEach>
        </div>
    </c:if>
</form>
</body>
</html>
