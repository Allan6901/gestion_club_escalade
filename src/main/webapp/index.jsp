<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<!-- my first JSP page -->
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>My first page</title>
</head>
<html><body>
    <jsp:useBean id="now" class="java.util.Date" />

    <p>Today: ${now}</p>
    <p>Hello  1 + 2 = <%= (1+2) %></p>
    <p><a href="/members">View members</a></p>
</body></html>