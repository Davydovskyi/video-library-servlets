<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Person</title>
</head>
<body>
<%@include file="header.jsp" %>

<h1>${requestScope.person.personData()}</h1>

<div>
    <span><b><fmt:message key="page.person.filmography"/></b></span>
    <ol>
        <c:forEach var="movie" varStatus="loop" items="${requestScope.movies}">
            <li value="${loop.index + 1}">
                <a href="${pageContext.request.contextPath}/movie/${movie.id()}">
                        ${movie.movieData()}</a>
            </li>
        </c:forEach>
    </ol>
    <br>
</div>

</body>
</html>