<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 23/05/2024
  Time: 20:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rezultati</title>
</head>
<body>
    <h1>Rezultati glasanja</h1>
    <p>Ovo su rezultati glasanja</p>
    <table>
        <thead>
            <tr>
                <th>Bend</th>
                <th>Broj lajkova</th>
                <th>Broj disajkova</th>
                <th>Razlika</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${pollOptions}" var="option">
            <tr>
                <td>${option.title}</td>
                <td>${option.likeCount}</td>
                <td>${option.dislikeCount}</td>
                <th>${option.likeCount - option.dislikeCount}</th>

            </tr>
        </c:forEach>
        </tbody>
    </table>
    <h2>Graficki prikaz rezultat</h2>
    <img src="${pageContext.request.contextPath}/servleti/votes-pie?pollID=${pollID}" />
    <h2>Rezultati u XLS formatu</h2>
    <p>Rezultate u XLS formatu mozete preuzeti <a href="${pageContext.request.contextPath}/servleti/votes-gen-xls?pollID=${pollID}">ovdje</a></p>
    <h1>Razno...</h1>
</body>
</html>
