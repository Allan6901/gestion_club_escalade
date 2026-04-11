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

    <p>Aujourdhui : ${now}</p>
   <p>Hello  1 + 2 = <%= (1+2) %></p>
</body></html>