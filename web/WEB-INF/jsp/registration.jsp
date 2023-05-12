<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<%@include file="header.jsp" %>

<%--<img src="${pageContext.request.contextPath}/images/userImage/image.PNG" alt="Welcome Image"/>--%>

<form action="${pageContext.request.contextPath}/registration" method="post" enctype="multipart/form-data">
    <label for="userName"><fmt:message key="page.registration.name"/>
        <input type="text" name="userName" id="userName" required>
    </label><br>
    <label for="birthday"><fmt:message key="page.registration.birthday"/>
        <input type="date" translate="no" name="birthday" id="birthday" required>
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
        <input type="radio" name="gender" value="${gender}" required><fmt:message
            key="page.registration.${gender.getName()}"/>
        <br>
    </c:forEach>
    <button type="submit"><fmt:message key="page.registration.send"/></button>
    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <span>${error.getMessage}</span><br>
            </c:forEach>
        </div>
    </c:if>
</form>
</body>
</html>
