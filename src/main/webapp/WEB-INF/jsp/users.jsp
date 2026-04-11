<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Utilisateurs</title>
</head>
<body>
    <h1>Liste des Utilisateurs</h1>
    <ul>
        <c:forEach var="user" items="${users}">
            <li>${user.name} - ${user.email}</li>
        </c:forEach>
    </ul>

    <h2>Ajouter un Utilisateur</h2>
    <form action="/users" method="post">
        <label for="name">Nom:</label>
        <input type="text" id="name" name="name" required><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br>
        <button type="submit">Ajouter</button>
    </form>
</body>
</html>
