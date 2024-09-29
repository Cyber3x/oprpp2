<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Register</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        .register-form {
            width: 300px;
            margin: 0 auto;
            padding: 30px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .register-form h2 {
            text-align: center;
        }

        .register-form input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
        }

        .register-form button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .register-form button:hover {
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
<div class="register-form">
    <h2>Register</h2>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>
    <form action="${pageContext.request.contextPath}/servleti/register" method="post">
        <label for="firstname">First Name:</label>
        <input type="text" id="firstname" name="firstName" value="${firstName}" required/>

        <label for="lastname">Last Name:</label>
        <input type="text" id="lastname" name="lastName" value="${lastName}" required/>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="${email}" required/>

        <label for="nick">Nick:</label>
        <input type="text" id="nick" name="nick" value="${nick}" required/>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required/>

        <button type="submit">Register</button>
    </form>
    <p style="text-align: center">Already have an account? <a href="${pageContext.request.contextPath}/servleti/main">Login here!</a></p>
</div>
</body>
</html>
