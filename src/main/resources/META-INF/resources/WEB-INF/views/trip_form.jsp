<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Gérer une sortie</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">${empty trip.id ? 'Nouvelle Sortie' : 'Modifier la Sortie'}</h1>

        <form:form action="/member/trips" method="post" modelAttribute="trip" class="p-4 border rounded shadow-sm bg-light">
            <form:hidden path="id"/>

            <div class="mb-3">
                <label for="name" class="form-label">Nom de la sortie</label>
                <form:input path="name" id="name" class="form-control" required="true"/>
            </div>

            <div class="mb-3">
                <label for="categoryId" class="form-label">Catégorie</label>
                <select name="categoryId" id="categoryId" class="form-select" required="true">
                    <option value="">Sélectionnez une catégorie...</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.id}" ${trip.category.id == cat.id ? 'selected' : ''}>${cat.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="mb-3">
                <label for="date" class="form-label">Date de la sortie</label>
                <form:input path="date" type="date" id="date" class="form-control" required="true"/>
            </div>

            <div class="mb-3">
                <label for="description" class="form-label">Description</label>
                <form:textarea path="description" id="description" class="form-control" rows="4"/>
            </div>

            <div class="mb-3">
                <label for="website" class="form-label">Site Web (optionnel)</label>
                <form:input path="website" type="url" id="website" class="form-control" placeholder="https://..."/>
            </div>

            <button type="submit" class="btn btn-primary w-100">${empty trip.id ? 'Créer' : 'Mettre à jour'}</button>
            <a href="/member/my-trips" class="btn btn-secondary w-100 mt-2">Annuler</a>
        </form:form>
    </div>
</body>
</html>