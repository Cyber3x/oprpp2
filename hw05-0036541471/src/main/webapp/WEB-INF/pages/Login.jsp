<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        .login-form {
            width: 300px;
            margin: 0 auto;
            padding: 30px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .login-form h2 {
            text-align: center;
        }

        .login-form input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
        }

        .login-form button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .login-form button:hover {
            background-color: #45a049;
        }

        .error-message {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
</head>

<body>
<div>
    <%
        if (request.getSession().getAttribute("current.user.fn") != null) {
    %>
    <%= request.getSession().getAttribute("current.user.fn") %> <%= request.getSession().getAttribute("current.user.ln") %>
    <a href="<%= request.getContextPath() %>/servleti/main?logout=true">Log out</a>
    <%
    } else {
    %>
    User not logged in!
    <%
        }
    %>
</div>
<%
    if (request.getSession().getAttribute("current.user.fn") == null) {
%>
<div class="login-form">
    <h2>Login</h2>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>
    <form action="${pageContext.request.contextPath}/servleti/main" method="post">
        <label for="nick">Nick:</label>
        <input type="text" id="nick" name="nick" value="${nick}" required/>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required/>

        <button type="submit">Login</button>
    </form>
    <p style="text-align: center">Don't have an account? <a
            href="${pageContext.request.contextPath}/servleti/register">Register
        here!</a></p>

</div>
<%
    }
%>
<div>
    <h3>Registered authors</h3>
    <ul>
    <c:forEach var="author" items="${blogAuthors}">
        <li><a href="${pageContext.request.contextPath}/servleti/author/${author.nick}">${author.nick}</a></li>
    </c:forEach>
    </ul>
</div>
</body>
</html>
