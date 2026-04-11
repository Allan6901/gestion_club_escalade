<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<!-- ma première page JSP -->
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Ma première page</title>
</head>
<html><body>
    <jsp:useBean id="now" class="java.util.Date" />

    <p>Aujourd'hui : ${now}</p>
    <p>Hello  1 + 2 = <%= (1+2) %></p>
    <p><a href="/membres">Voir les membres</a></p>
</body></html>