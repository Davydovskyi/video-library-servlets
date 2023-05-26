<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>User</title>
</head>
<body>
<%@include file="header.jsp" %>

<h1>${requestScope.user.name()}</h1>

<div>
    <table style="width: 100%;margin-left:20px">
        <caption><b><fmt:message key="page.user.reviews"/></b></caption>
        <c:if test="${not empty requestScope.reviews}">
            <colgroup>
                <col style="width: 200px;">
                <col style="width: 20px;">
                <col style="width: 200px;">
            </colgroup>
            <thead>
            <tr style="text-align: left">
                <th><fmt:message key="page.user.reviews.movie"/></th>
                <th><fmt:message key="page.user.reviews.rate"/></th>
                <th><fmt:message key="page.user.reviews.reviewText"/></th>
            </tr>
            </thead>

            <c:forEach var="review" items="${requestScope.reviews}">
                <tbody>
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/movie/${review.movie().movieId()}">
                                ${review.movie().movieData()}</a>
                    </td>
                    <td>${review.rate()}</td>
                    <td>${review.reviewText()}</td>
                </tr>
                </tbody>
            </c:forEach>
        </c:if>
    </table>
</div>
</body>
</html>