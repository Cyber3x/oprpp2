<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 23/05/2024
  Time: 19:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Glasaj</title>
</head>
<body>
    <h1>${poll.title}</h1>
    <p>${poll.message}</p>

    <ul>
        <c:forEach items="${pollOptions}" var="pollOption">
            <li><a href="${pageContext.request.contextPath}/servleti/glasaj-za?pollID=${poll.id}&optionID=${pollOption.id}">${pollOption.title}</a>, LINK: ${pollOption.link}</li>
        </c:forEach>
    </ul>
</body>
</html>
