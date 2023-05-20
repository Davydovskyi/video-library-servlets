<%@ page contentType="text/html;charset=UTF-8" %>

<form action="${pageContext.request.contextPath}/add-person" method="post">
    <label for="name"><fmt:message key="page.addPerson.name"/>
        <input type="text" name="name" id="name" required>
    </label><br>
    <label for="birthday"><fmt:message key="page.addPerson.birthday"/>
        <input type="date" name="birthday" id="birthday" required>
    </label><br>
    <button type="submit"><fmt:message key="page.admin.add.button"/></button>
    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <span><fmt:message key="${error.getCode()}"/></span><br>
            </c:forEach>
        </div>
    </c:if>
    <c:if test="${not empty requestScope.success}">
        <div style="color: green">
            <span><fmt:message key="add.success"/></span><br>
        </div>
    </c:if>
</form>

