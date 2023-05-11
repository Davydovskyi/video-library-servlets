<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Билеты</title>
</head>
<body>
<%@include file="header.jsp" %>
<c:if test="${not empty requestScope.tickets}">
    <h1>Количество билетов: ${requestScope.tickets.size()}</h1>
    <h2>Купленные билеты:</h2>
    <ul>
        <c:forEach var="ticket" items="${requestScope.tickets}">
            <li>${fn:toUpperCase(ticket.seatNo())}</li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>
