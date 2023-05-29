<%@ page contentType="text/html;charset=UTF-8" %>

<form action="${pageContext.request.contextPath}/add-movie" method="post">
    <label for="title"><fmt:message key="page.addMovie.title"/>
        <input type="text" name="title" id="title" required>
    </label><br>
    <label for="release_year"><fmt:message key="page.addMovie.releaseYear"/>
        <input type="number" min="1900" max="2030" name="release_year" id="release_year" required>
    </label><br>
    <label for="country"><fmt:message key="page.addMovie.country"/>
        <input type="text" name="country" id="country" required>
    </label><br>
    <label for="genre"><fmt:message key="page.addMovie.genre"/>
        <select name="genre" id="genre" required>
            <c:forEach var="genre" items="${requestScope.genres}">
                <option value="${genre}"><fmt:message key="${genre.getCode()}"/></option>
            </c:forEach>
        </select>
    </label><br>
    <label for="description"></label>
    <textarea name="description"
              placeholder="<fmt:message key="page.addMovie.description"/>"
              cols="40" rows="4" maxlength="700" id="description"
              required></textarea><br>

    <label for="member"></label><br>
    <label for="movie_role"></label>

    <c:forEach var="i" begin="1" end="4">
        <span>${i}: </span>
        <select name="member${i}" id="member" required>
            <option selected disabled hidden><fmt:message key="page.addMovie.movieParticipant"/></option>
            <c:forEach var="member" items="${applicationScope.filmMembers}">
                <option value="${member.id()}">${member.personData()}</option>
            </c:forEach>
        </select>

        <select name="movie_role${i}" id="movie_role" required>
            <option selected disabled hidden><fmt:message key="page.addMovie.movieRole"/></option>
            <c:forEach var="role" items="${requestScope.personRoles}">
                <option value="${role}"><fmt:message key="${role.getCode()}"/></option>
            </c:forEach>
        </select><br>
    </c:forEach>

    <label for="submit"></label><br>
    <button type="submit" id="submit"><fmt:message key="page.admin.add.button"/></button>
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