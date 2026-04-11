<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<h1>Edition</h1>

<form method="post" action="person">
    <div class="mb-3">
        <label>ID :</label>
        <input name="id" class="form-control" type="text"
               value="<c:out value='${person.id}'/>"
               ${not empty sessionScope.originalId ? 'readonly' : ''} />
        <span class="text-danger"><c:out value="${errors.id}"/></span>
    </div>

    <div class="mb-3">
        <label>Name :</label>
        <input name="name" class="form-control" type="text" value="<c:out value='${person.name}'/>" />
        <span class="text-danger"><c:out value="${errors.name}"/></span>
    </div>

    <div class="mb-3">
        <label>Mail :</label>
        <input name="mail" class="form-control" type="text" value="<c:out value='${person.mail}'/>" />
        <span class="text-danger"><c:out value="${errors.mail}"/></span>
    </div>

    <input type="submit" class="btn btn-primary" value="Valider" />
</form>

<%@ include file="footer.jsp" %>