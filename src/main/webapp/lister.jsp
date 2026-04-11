<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="header.jsp" %>

<h1>Liste des personnes</h1>

<table class="table">
    <c:forEach items="${persons}" var="p">
        <tr>
            <td><c:out value="${p.id}"/></td>
            <td><c:out value="${p.name}"/></td>
            <td><c:out value="${p.mail}"/></td>
            <td>
                <a href="person?id=${p.id}">Modifier</a>
                <a href="deletePerson?id=${p.id}" onclick="return confirm('Supprimer ?')">Supprimer</a>
            </td>
        </tr>
    </c:forEach>
</table>

<p><a href="person?ajout=1">Ajouter une personne</a></p>

<%@ include file="footer.jsp" %>