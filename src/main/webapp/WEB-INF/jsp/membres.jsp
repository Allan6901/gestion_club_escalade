<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Membres</title>
</head>
<body>
    <h1>Liste des Membres</h1>
    <ul>
        <c:forEach var="membre" items="${membres}">
            <li>${membre.nom} ${membre.prenom} - ${membre.email}</li>
        </c:forEach>
    </ul>

    <h2>Ajouter un Membre</h2>
    <form action="/membres" method="post">
        <label for="nom">Nom:</label>
        <input type="text" id="nom" name="nom" required><br>
        <label for="prenom">Prénom:</label>
        <input type="text" id="prenom" name="prenom" required><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br>
        <label for="motDePasse">Mot de passe:</label>
        <input type="password" id="motDePasse" name="motDePasse" required><br>
        <button type="submit">Ajouter</button>
    </form>
</body>
</html>
