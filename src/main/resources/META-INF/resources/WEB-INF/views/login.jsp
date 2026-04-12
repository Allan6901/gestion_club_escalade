<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Connexion</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Connexion Espace Membre</h1>
        <div class="card p-4 mx-auto shadow-sm" style="max-width: 400px;">
            <c:if test="${param.error != null}">
                <div class="alert alert-danger" role="alert">
                    Email ou mot de passe incorrect.
                </div>
            </c:if>
            <c:if test="${param.logout != null}">
                <div class="alert alert-success" role="alert">
                    Vous avez été déconnecté avec succès.
                </div>
            </c:if>

            <c:if test="${param.registered != null}">
                <div class="alert alert-success" role="alert">
                    Compte créé avec succès ! Vous pouvez vous connecter.
                </div>
            </c:if>

            <form action="/login" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Email</label>
                    <input type="email" class="form-control" id="username" name="username" required autofocus>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Mot de passe</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Se connecter</button>
            </form>
            <div class="mt-3 text-center">
                <a href="/forgot-password" class="text-decoration-none">Mot de passe oublié ?</a>
            </div>
            <hr>
            <div class="text-center">
                <span class="text-muted">Pas encore membre ?</span>
                <a href="/register" class="btn btn-outline-success btn-sm ms-2">Créer un compte</a>
            </div>
            <div class="mt-3 text-center">
                <a href="/" class="btn btn-secondary btn-sm">Retour à l'accueil</a>
            </div>
        </div>
    </div>
</body>
</html>