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

        <form action="/trips/search" method="get" class="mb-4 p-4 border rounded bg-light">
            <div class="row g-3">
                <div class="col-md-4">
                    <label for="name" class="form-label">Nom de la sortie</label>
                    <input type="text" class="form-control" id="name" name="name"
                           placeholder="Mot-clé..." value="${searchName}">
                </div>
                <div class="col-md-4">
                    <label for="categoryId" class="form-label">Catégorie</label>
                    <select class="form-select" id="categoryId" name="categoryId">
                        <option value="">Toutes les catégories</option>
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat.id}"
                                ${searchCategoryId == cat.id ? 'selected' : ''}>${cat.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <label for="startDate" class="form-label">Date début</label>
                    <input type="date" class="form-control" id="startDate" name="startDate"
                           value="${searchStartDate}">
                </div>
                <div class="col-md-2">
                    <label for="endDate" class="form-label">Date fin</label>
                    <input type="date" class="form-control" id="endDate" name="endDate"
                           value="${searchEndDate}">
                </div>
                <div class="col-12 d-flex justify-content-end gap-2">
                    <a href="/trips/search" class="btn btn-outline-secondary">Réinitialiser</a>
                    <button type="submit" class="btn btn-primary">Rechercher</button>
                </div>
            </div>
        </form>

        <%-- "Sorties de {créateur}" quand filtré par créateur --%>
        <c:if test="${creatorFilter != null}">
            <div class="alert alert-info d-flex justify-content-between align-items-center">
                <span>Sorties proposées par <strong>${creatorFilter}</strong></span>
                <a href="/trips/search" class="btn btn-sm btn-outline-secondary">Effacer le filtre</a>
            </div>
        </c:if>

        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="mb-0">Résultats</h2>
            <span class="badge bg-secondary fs-6">${totalElements} sortie(s) trouvée(s)</span>
        </div>

        <c:if test="${empty trips}">
            <p class="text-muted">Aucune sortie trouvée pour ces critères.</p>
        </c:if>

        <c:if test="${not empty trips}">
            <table class="table table-striped table-hover">
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

            <%-- Pagination — conserve les paramètres de recherche --%>
            <c:if test="${totalPages > 1}">
                <nav aria-label="Pagination recherche">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="/trips/search?name=${searchName}&categoryId=${searchCategoryId}&memberId=${searchMemberId}&startDate=${searchStartDate}&endDate=${searchEndDate}&page=${currentPage - 1}">
                                &laquo; Précédent
                            </a>
                        </li>
                        <li class="page-item disabled">
                            <span class="page-link">Page ${currentPage + 1} / ${totalPages}</span>
                        </li>
                        <li class="page-item ${currentPage + 1 >= totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="/trips/search?name=${searchName}&categoryId=${searchCategoryId}&memberId=${searchMemberId}&startDate=${searchStartDate}&endDate=${searchEndDate}&page=${currentPage + 1}">
                                Suivant &raquo;
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </c:if>

        <a href="/" class="btn btn-secondary mt-3">Retour à l'accueil</a>
    </div>
</body>
</html>