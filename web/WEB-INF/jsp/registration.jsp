<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<%@include file="localeHeader.jsp" %>
<%@include file="logoutHeader.jsp" %>


<img src="${pageContext.request.contextPath}/images/applicationImage/image.PNG" alt="Welcome Image"/>

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
        <label for="gender">
            <input type="radio" name="gender" id="gender" value="${gender}" required>
        </label><fmt:message
            key="page.registration.${gender.getName()}"/>
        <br>
    </c:forEach>

    <c:if test="${sessionScope.user == null || sessionScope.user.role().name() != 'SUPER_ADMIN'}">
        <input type="hidden" name="role" value="USER">
    </c:if>

    <c:if test="${sessionScope.user != null && sessionScope.user.role().name() == 'SUPER_ADMIN'}">
        ${param.role = null}
        <label for="role"></label><select name="role" id="role" required>
        <c:forEach var="role" items="${requestScope.roles}">
            <option value="${role}">${role}</option>
        </c:forEach>
    </select><br>
    </c:if>

    <button type="submit"><fmt:message key="page.registration.send"/></button>
    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <span><fmt:message key="page.registration.${error.getCode()}"/></span><br>
            </c:forEach>
        </div>
    </c:if>
</form>
</body>
</html>
