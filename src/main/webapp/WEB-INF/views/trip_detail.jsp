<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Détail de la sortie</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h2>${trip.name}</h2>
            </div>
            <div class="card-body">
                <p><strong>Catégorie :</strong> ${trip.category.name}</p>
                <p><strong>Date :</strong> <fmt:formatDate value="${trip.date}" pattern="dd/MM/yyyy"/></p>
                <p><strong>Description :</strong> ${trip.description}</p>

                <c:if test="${isAuthenticated}">
                    <hr>
                    <p><strong>Site Web :</strong> <a href="${trip.website}" target="_blank">${trip.website}</a></p>
                    <p><strong>Créateur :</strong> ${trip.creator.firstName} ${trip.creator.lastName} (<a href="mailto:${trip.creator.email}">${trip.creator.email}</a>)</p>
                </c:if>
                <c:if test="${!isAuthenticated}">
                    <hr>
                    <div class="alert alert-warning">
                        <a href="/login">Connectez-vous</a> pour voir le site web et les informations du créateur.
                    </div>
                </c:if>

                <c:if test="${isCreator}">
                    <div class="mt-4">
                        <a href="/member/trips/${trip.id}/edit" class="btn btn-warning">Modifier</a>
                        <form action="/member/trips/${trip.id}/delete" method="post" class="d-inline">
                            <button type="submit" class="btn btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette sortie ?');">Supprimer</button>
                        </form>
                    </div>
                </c:if>
            </div>
            <div class="card-footer text-muted">
                <a href="/categories/${trip.category.id}/trips" class="btn btn-secondary">Retour à la liste</a>
            </div>
        </div>
    </div>
</body>
</html>