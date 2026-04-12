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
        <h1 class="mb-4">Sorties de la catégorie : ${category.name}</h1>

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

        <a href="/categories" class="btn btn-secondary mt-3">Retour aux catégories</a>
    </div>
</body>
</html>