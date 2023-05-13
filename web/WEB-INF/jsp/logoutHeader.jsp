<%@ page contentType="text/html;charset=UTF-8" %>

<div>
    <c:if test="${not empty sessionScope.user}">
        <div id="logout">
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button type="submit"><fmt:message key="page.header.logout"/></button>
            </form>
        </div>
    </c:if>
</div>