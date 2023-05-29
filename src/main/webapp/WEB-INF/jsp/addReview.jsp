<form action="${pageContext.request.contextPath}/add-review/${requestScope.movieId}" method="post">
    <label for="rate"><fmt:message key="page.addReview.rate"/>
        <select name="rate" id="rate" required>
            <c:forEach var="i" begin="1" end="10">
                <option value="${i}">${i}</option>
            </c:forEach>
        </select>
    </label><br>
    <label for="review"></label>
    <textarea name="review"
              placeholder="<fmt:message key="page.addReview.review"/>"
              cols="40" rows="4" maxlength="256" id="review"
              required></textarea><br>

    <label for="submit"></label><br>
    <button type="submit" id="submit"><fmt:message key="page.addReview.send.button"/></button>
    <a href="${pageContext.request.contextPath}/movie/${requestScope.movieId}">
        <button type="button"><fmt:message key="page.addReview.cancel.button"/></button>
    </a>
    <c:if test="${not empty requestScope.errors}">
        <div style="color: red">
            <c:forEach var="error" items="${requestScope.errors}">
                <span><fmt:message key="${error.getCode()}"/></span><br>
            </c:forEach>
        </div>
    </c:if>
</form>