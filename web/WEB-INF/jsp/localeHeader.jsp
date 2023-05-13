<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale
        value="${sessionScope.locale != null ? sessionScope.locale : (param.locale != null ? param.locale : 'en_US')}"/>
<fmt:setBundle basename="translations"/>
<div id="locale">
    <form action="${pageContext.request.contextPath}/locale" method="post">
        <button type="submit" name="locale" value="ru_RU">RU</button>
        <button type="submit" name="locale" value="en_US">EN</button>
    </form>
</div>