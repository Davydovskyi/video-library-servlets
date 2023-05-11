<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
    <c:if test="${not empty sessionScope.user}">
        <div id="logout">
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button type="submit">Logout</button>
            </form>
        </div>
    </c:if>
    <div id="locale">
        <form action="${pageContext.request.contextPath}/locale" method="post">
            <button type="submit" name="locale" value="ru_RU">RU</button>
            <button type="submit" name="locale" value="en_US">EN</button>
        </form>
    </div>

    <fmt:setLocale
            value="${sessionScope.locale != null ? sessionScope.locale : (param.locale != null ? param.locale : 'en_US')}"/>
    <fmt:setBundle basename="translations"/>
</div>
