<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Movie</title>
</head>
<body>
<%@ include file="header.jsp" %>

<h1>${requestScope.movie.title()}</h1>
<div>
    <span><b><fmt:message key="page.movie.genre"/> </b>${requestScope.movie.genre()}</span><br>
    <span><b><fmt:message key="page.movie.releaseYear"/> </b>${requestScope.movie.releaseYear()}</span><br>
    <span><b><fmt:message key="page.movie.country"/> </b>${requestScope.movie.country()}</span><br>
    <span><b><fmt:message key="page.movie.description"/> </b>${requestScope.movie.description()}</span><br>
    <span><b><fmt:message key="page.movie.movieParticipants"/></b></span>
    <ol>
        <c:forEach var="movie_person" varStatus="loop" items="${requestScope.movie.moviePeople()}">
            <li value="${loop.index + 1}">
                <a href="${pageContext.request.contextPath}/person/${movie_person.person().id()}">
                        ${movie_person.person().personData()}</a> - ${movie_person.personRole()}
            </li>
        </c:forEach>
    </ol>
    <br>

    <c:if test="${requestScope.show_add_review == 'yes'}">
        <%@include file="addReview.jsp" %>
    </c:if>

    <c:if test="${empty requestScope.show_add_review || requestScope.show_add_review == 'no'}">
        <form action="${pageContext.request.contextPath}/movie/${requestScope.movieId}" method="post">
            <button type="submit" name="show_add_review" value="addReview"
                    <c:if test="${not empty requestScope.review_exists}">disabled</c:if> >
                <fmt:message key="page.movie.addReview.button"/>
            </button>
            <br>
        </form>
    </c:if>

    <table style="width: 100%;margin-left:20px">
        <caption><b><fmt:message key="page.movie.reviews"/></b></caption>
        <c:if test="${not empty requestScope.movie.reviews()}">
            <colgroup>
                <col style="width: 20px;">
                <col style="width: 50px;">
                <col style="width: 200px;">
            </colgroup>
            <thead>
            <tr style="text-align: left">
                <th><fmt:message key="page.movie.reviews.rate"/></th>
                <th><fmt:message key="page.movie.reviews.user"/></th>
                <th><fmt:message key="page.movie.reviews.text"/></th>
            </tr>
            </thead>

            <c:forEach var="review" items="${requestScope.movie.reviews()}">
                <tbody>
                <tr>
                    <td>${review.rate()}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/user/${review.user().id()}">
                                ${review.user().name()}</a>
                    </td>
                    <td>${review.reviewText()}</td>
                </tr>
                </tbody>
            </c:forEach>
        </c:if>
    </table>
</div>
</body>
</html>