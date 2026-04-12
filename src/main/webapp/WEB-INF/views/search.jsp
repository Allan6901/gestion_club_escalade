<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Recherche de sorties</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Recherche de sorties</h1>

        <form action="/trips/search" method="get" class="mb-5 p-4 border rounded bg-light">
            <div class="row">
                <div class="col-md-5 mb-3">
                    <label for="name" class="form-label">Nom de la sortie</label>
                    <input type="text" class="form-control" id="name" name="name" placeholder="Mot-clé...">
                </div>
                <div class="col-md-5 mb-3">
                    <label for="categoryId" class="form-label">Catégorie</label>
                    <select class="form-select" id="categoryId" name="categoryId">
                        <option value="">Toutes les catégories</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}">${cat.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2 d-flex align-items-end mb-3">
                    <button type="submit" class="btn btn-primary w-100">Rechercher</button>
                </div>
            </div>
        </form>

        <h2>Résultats (${trips.size()})</h2>
        <c:if test="${empty trips}">
            <p class="text-muted">Aucune sortie trouvée pour ces critères.</p>
        </c:if>

        <table class="table table-striped table-hover mt-3">
            <thead class="table-dark">
                <tr>
                    <th>Nom</th>
                    <th>Catégorie</th>
                    <th>Date</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="trip" items="${trips}">
                    <tr>
                        <td>${trip.name}</td>
                        <td>${trip.category.name}</td>
                        <td><fmt:formatDate value="${trip.date}" pattern="dd/MM/yyyy"/></td>
                        <td>
                            <a href="/trips/${trip.id}" class="btn btn-sm btn-info text-white">Détails</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <a href="/" class="btn btn-secondary mt-3">Retour à l'accueil</a>
    </div>
</body>
</html>