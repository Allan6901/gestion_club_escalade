<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Club d'Escalade - Accueil</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Bienvenue au Club d'Escalade</h1>

        <div class="mb-3">
            <sec:authorize access="!isAuthenticated()">
                <a href="/login" class="btn btn-primary">Se connecter</a>
                <a href="/register" class="btn btn-outline-success ms-2">S'inscrire</a>
                <a href="/forgot-password" class="btn btn-link">Mot de passe oublié ?</a>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <p>Bienvenue, <sec:authentication property="name" /> !</p>
                <form action="/logout" method="post" class="d-inline">
                    <button type="submit" class="btn btn-danger">Se déconnecter</button>
                </form>
                <a href="/member/my-trips" class="btn btn-outline-primary ms-2">Mes sorties</a>
                <a href="/member/my-registrations" class="btn btn-outline-info ms-2">Mes inscriptions</a>
                <a href="/member/trips/new" class="btn btn-success ms-2">Proposer une nouvelle sortie</a>
            </sec:authorize>
        </div>

        <div class="list-group">
            <a href="/categories" class="list-group-item list-group-item-action">Voir les catégories</a>
            <a href="/trips/search" class="list-group-item list-group-item-action">Rechercher des sorties</a>
        </div>
    </div>
</body>
</html>