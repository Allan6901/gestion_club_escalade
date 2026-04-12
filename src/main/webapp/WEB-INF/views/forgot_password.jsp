<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Mot de passe oublié</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Récupération de mot de passe</h1>
        <div class="card p-4 mx-auto shadow-sm" style="max-width: 400px;">
            <c:if test="${message != null}">
                <div class="alert alert-success" role="alert">
                    ${message}
                </div>
            </c:if>
            <c:if test="${error != null}">
                <div class="alert alert-danger" role="alert">
                    ${error}
                </div>
            </c:if>

            <form action="/forgot-password" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Entrez votre adresse email</label>
                    <input type="email" class="form-control" id="email" name="email" required autofocus>
                </div>
                <button type="submit" class="btn btn-primary w-100">Envoyer le lien</button>
            </form>
            <div class="mt-3 text-center">
                <a href="/login" class="btn btn-link btn-sm">Retour à la connexion</a>
            </div>
        </div>
    </div>
</body>
</html>