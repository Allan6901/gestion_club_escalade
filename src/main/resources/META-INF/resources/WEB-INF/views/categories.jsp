<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Catégories d'Escalade</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Nos Catégories</h1>

        <div class="list-group">
            <c:forEach var="category" items="${categories}">
                <a href="/categories/${category.id}/trips" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                    ${category.name}
                    <span class="badge bg-primary rounded-pill">${category.trips.size()} sorties</span>
                </a>
            </c:forEach>
        </div>

        <a href="/" class="btn btn-secondary mt-3">Retour à l'accueil</a>
    </div>
</body>
</html>