<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@include file="header.jsp" %>
<div>
    <span>Content. Русский.</span>
    <p>Size: ${requestScope.flights.size()}</p>
    <p>Id: ${requestScope.flights.get(0).id()}</p>
    <p>Id 2: ${requestScope.flights[1].id()}</p>
    <p>Map Id: ${sessionScope.flightsMap[1]}</p>
    <p>JSession id: ${cookie["JSESSIONID"].value}, unique identifier</p>
    <p>Header: ${header["Cookie"]}</p>
    <p>Param id: ${param.id}</p>
    <p>Param test: ${param.test}</p>
    <p>Empty List: ${not empty requestScope.flights}</p>
</div>
<%@include file="footer.jsp" %>
</body>
</html>
