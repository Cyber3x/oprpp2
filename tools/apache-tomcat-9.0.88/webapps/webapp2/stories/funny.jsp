<%@ page import="java.util.Random" %><%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 26/04/2024
  Time: 13:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String[] colors = {"red", "green", "yellow", "orange", "purple"};
    String bgColor = colors[new Random().nextInt(0, colors.length)];

%>
<html>
<head>
    <title>Title</title>
</head>
<body style="background: <%= bgColor %>">
    <p>Dođe Ivica doktoru i kaže mu doktore suzi mi oko.</p>
    <br />
    <br />
    <p>I doktor mu suzi oko.</p>
</body>
</html>
