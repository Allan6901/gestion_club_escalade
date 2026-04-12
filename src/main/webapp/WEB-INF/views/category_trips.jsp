<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Sorties - ${category.name}</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>Sorties : ${category.name}</h1>
            <span class="badge bg-secondary fs-6">${totalElements} sortie(s)</span>
        </div>

        <c:if test="${empty trips}">
            <p class="text-muted">Aucune sortie dans cette catégorie.</p>
        </c:if>

        <c:if test="${not empty trips}">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Nom de la sortie</th>
                        <th>Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="trip" items="${trips}">
                        <tr>
                            <td>${trip.name}</td>
                            <td><fmt:formatDate value="${trip.date}" pattern="dd/MM/yyyy"/></td>
                            <td>
                                <a href="/trips/${trip.id}" class="btn btn-sm btn-info text-white">Détails</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <%-- Pagination --%>
            <c:if test="${totalPages > 1}">
                <nav aria-label="Pagination sorties">
                    <ul class="pagination justify-content-center">
                        <li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="/categories/${category.id}/trips?page=${currentPage - 1}">
                                &laquo; Précédent
                            </a>
                        </li>
                        <li class="page-item disabled">
                            <span class="page-link">Page ${currentPage + 1} / ${totalPages}</span>
                        </li>
                        <li class="page-item ${currentPage + 1 >= totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="/categories/${category.id}/trips?page=${currentPage + 1}">
                                Suivant &raquo;
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </c:if>

        <a href="/categories" class="btn btn-secondary mt-3">Retour aux catégories</a>
    </div>
</body>
</html>