<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>Réinitialiser le mot de passe</title>
    <link rel="stylesheet" href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Nouveau mot de passe</h1>
        <div class="card p-4 mx-auto shadow-sm" style="max-width: 400px;">
            <form action="/reset-password" method="post">
                <input type="hidden" name="email" value="${email}">
                <div class="mb-3">
                    <label for="password" class="form-label">Nouveau mot de passe</label>
                    <input type="password" class="form-control" id="password" name="password" required autofocus>
                </div>
                <button type="submit" class="btn btn-primary w-100">Réinitialiser</button>
            </form>
        </div>
    </div>
</body>
</html>