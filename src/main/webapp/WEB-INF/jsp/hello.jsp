<html>
  <head><title>Hello :: Spring Application</title></head>
  <body>
    <%@ include file="/WEB-INF/jsp/header.jsp" %>

    <h1>Hello - Spring Application</h1>
    <p>Greetings, it is now <c:out value="${now}" default="None" /></p>

    <%@ include file="/WEB-INF/jsp/footer.jsp" %>
  </body>
</html>