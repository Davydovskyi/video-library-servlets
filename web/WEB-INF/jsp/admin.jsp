<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Admin</title>
</head>
<body>
<%@include file="header.jsp" %>

<c:if test="${not empty requestScope.addPerson}">
    <%@include file="addPerson.jsp" %>
</c:if>

<c:if test="${empty requestScope.addPerson}">
    <form action="${pageContext.request.contextPath}/admin" method="post">
        <button type="submit" name="addPerson" value="addPerson"><fmt:message key="page.admin.addPerson.button"/>
        </button>
        <br>
    </form>
</c:if>

<c:if test="${not empty requestScope.addMovie}">
    <%@include file="addMovie.jsp" %>
</c:if>

<c:if test="${empty requestScope.addMovie}">
    <form action="${pageContext.request.contextPath}/admin" method="post">
        <button type="submit" name="addMovie" value="addMovie"><fmt:message key="page.admin.addMovie.button"/>
        </button>
        <br>
    </form>
</c:if>


<c:if test="${sessionScope.user.role().name() == 'SUPER_ADMIN'}">
    <form action="${pageContext.request.contextPath}/admin" method="post">
        <a href="${pageContext.request.contextPath}/registration">
            <button type="button"><fmt:message key="page.admin.addUser.button"/>
            </button>
            <br>
        </a>
    </form>
</c:if>

</body>
</html>
