<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 23/05/2024
  Time: 18:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Polls</title>
</head>
<body>
    <h1>Ovo su trenutno dostupna glasanja.</h1>
    <ol>
        <c:forEach items="polls" var="poll">
            <li> ${poll.name}</li>
        </c:forEach>
    </ol>
</body>
</html>
