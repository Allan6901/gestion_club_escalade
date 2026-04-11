<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 1. INDISPENSABLE : La ligne taglib pour activer la JSTL --%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Liste des Étudiants</title>
    <%-- Bootstrap pour que le tableau soit propre --%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">

    <h1>Tableau des étudiants</h1>

    <table class="table table-striped">
        <thead>
            <tr>
                <th>Nom</th>
                <th>Âge</th>
                <th>Meilleur Ami</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${students}" var="s">
                <tr>
                    <td><c:out value="${s.name}" /></td>
                    <td>
                        <c:choose>
                            <c:when test="${s.age > 24}">
                                <strong><c:out value="${s.age}" /></strong>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${s.age}" />
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:out value="${s.bestFriend.name}" default="personne" />
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>