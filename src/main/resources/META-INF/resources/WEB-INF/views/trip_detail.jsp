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
                <p><strong>Participants inscrits :</strong> ${trip.participants.size()}</p>

                <%-- Infos complètes pour les membres authentifiés uniquement --%>
                <c:if test="${isAuthenticated}">
                    <hr>
                    <p><strong>Site Web :</strong>
                        <a href="${trip.website}" target="_blank">${trip.website}</a>
                    </p>
                    <p><strong>Créateur :</strong>
                        <a href="/trips/search?memberId=${trip.creator.id}">
                            ${trip.creator.firstName} ${trip.creator.lastName}
                        </a>
                        (<a href="mailto:${trip.creator.email}">${trip.creator.email}</a>)
                    </p>
                </c:if>

                <c:if test="${!isAuthenticated}">
                    <hr>
                    <div class="alert alert-warning">
                        <a href="/login">Connectez-vous</a> pour voir le site web, les informations
                        du créateur et vous inscrire à cette sortie.
                    </div>
                </c:if>

                <%-- Inscription / désinscription : membres non-créateurs --%>
                <c:if test="${isAuthenticated && !isAdmin && !canManage}">
                    <hr>
                    <c:choose>
                        <c:when test="${isParticipant}">
                            <form action="/trips/${trip.id}/unregister" method="post" class="d-inline">
                                <button type="submit" class="btn btn-outline-danger">Se désinscrire</button>
                            </form>
                            <span class="ms-2 text-success fw-bold">&#10003; Vous êtes inscrit(e)</span>
                        </c:when>
                        <c:otherwise>
                            <form action="/trips/${trip.id}/register" method="post" class="d-inline">
                                <button type="submit" class="btn btn-success">S'inscrire à cette sortie</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:if>

                <%-- Modifier / Supprimer : créateur de la sortie ou admin --%>
                <c:if test="${canManage}">
                    <hr>
                    <a href="/member/trips/${trip.id}/edit" class="btn btn-warning">Modifier</a>
                    <form action="/member/trips/${trip.id}/delete" method="post" class="d-inline">
                        <button type="submit" class="btn btn-danger"
                                onclick="return confirm('Supprimer cette sortie ?');">Supprimer</button>
                    </form>
                </c:if>
            </div>
            <div class="card-footer text-muted d-flex justify-content-between align-items-center">
                <a href="/categories/${trip.category.id}/trips" class="btn btn-secondary">Retour à la liste</a>
                <a href="/" class="btn btn-outline-secondary btn-sm">Accueil</a>
            </div>
        </div>
    </div>
</body>
</html>