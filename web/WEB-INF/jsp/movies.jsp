<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Movies</title>
</head>
<body>
<%@include file="header.jsp" %>

<h1><fmt:message key="page.movies.movieSearch"/></h1>
<form action="${pageContext.request.contextPath}/movies" method="post">
    <label for="title">
        <input type="text" name="title" placeholder="<fmt:message key="page.movies.inputField.title"/>" id="title">
    </label>
    <label for="release_year">
        <input type="number" min="1900" max="2030" name="release_year"
               placeholder="<fmt:message key="page.movies.inputField.releaseYear"/>" id="release_year">
    </label>
    <label for="country">
        <input type="text" name="country" placeholder="<fmt:message key="page.movies.inputField.country"/>"
               id="country">
    </label>
    <label for="genre">
        <select name="genre" id="genre">
            <option selected disabled hidden><fmt:message key="page.movies.inputField.genre"/></option>
            <c:forEach var="genre" items="${requestScope.genres}">
                <option value="${genre}"><fmt:message key="${genre.getCode()}"/></option>
            </c:forEach>
        </select>
    </label>

    <label for="submit"></label>
    <button type="submit" id="submit"><fmt:message key="page.movies.search.button"/></button>

    <input type="hidden" name="limit" value="100">
    <input type="hidden" name="offset" value="0">

    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <span><fmt:message key="${error.getCode()}"/></span><br>
            </c:forEach>
        </div>
    </c:if>
</form>

<c:if test="${not empty requestScope.movies}">
    <h2><fmt:message key="page.movies.result"/></h2>
    <ol>
        <c:forEach var="movie" varStatus="loop" items="${requestScope.movies}">
            <li value="${loop.index + 1}">
                <a href="${pageContext.request.contextPath}/movie/${movie.id()}">${movie.movieData()}</a>
            </li>
        </c:forEach>
    </ol>
</c:if>

</body>
</html>