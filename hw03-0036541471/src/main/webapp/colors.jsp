<%@ page contentType="text/html;charset=UTF-8" session="true" %>

<%
    String bgColor = (String) request.getSession().getAttribute("bgcolor");
%>

<html>
<head>
    <title>Pick a color</title>
</head>
    <body style="background: <%= bgColor %>">
        <a href="${pageContext.request.contextPath}/setcolor?bgcolor=ffffff">WHITE</a>
        <a href="${pageContext.request.contextPath}/setcolor?bgcolor=ff0000">RED</a>
        <a href="${pageContext.request.contextPath}/setcolor?bgcolor=00ff00">GREEN</a>
        <a href="${pageContext.request.contextPath}/setcolor?bgcolor=00FFFF">CYAN</a>
    <h1><%= bgColor %></h1>
    </body>
</html>