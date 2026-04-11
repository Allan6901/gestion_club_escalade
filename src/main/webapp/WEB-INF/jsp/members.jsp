<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Members</title>
</head>
<body>
    <h1>List of Members</h1>
    <ul>
        <c:forEach var="member" items="${members}">
            <li>${member.lastName} ${member.firstName} - ${member.email}</li>
        </c:forEach>
    </ul>

    <h2>Add a Member</h2>
    <form action="/members" method="post">
        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" required><br>
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" required><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br>
        <button type="submit">Add</button>
    </form>
</body>
</html>
