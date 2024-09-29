<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 26/04/2024
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Application information</title>
</head>
<body>
    <h1>Running for:</h1>
    <% long delta = System.currentTimeMillis() - (long)request.getServletContext().getAttribute("initTime");
        final long MS_IN_DAY = 24 * 60 * 60 * 1000;
        long days = Math.floorDiv(delta, MS_IN_DAY);
        delta -= days * MS_IN_DAY;

        final long MS_IN_HOUR = 60*60*1000;
        long hours = Math.floorDiv(delta, MS_IN_HOUR);
        delta -= hours * MS_IN_HOUR;

        final long MS_IN_MINUTE = 60*1000;
        long minutes = Math.floorDiv(delta, MS_IN_MINUTE);
        delta -= minutes * MS_IN_MINUTE;

        final long MS_IN_SECOND = 1000;
        long seconds = Math.floorDiv(delta, MS_IN_SECOND);
        delta -= seconds * MS_IN_SECOND;
    %>
    <p><%= days%> days <%= hours %> hours <%= minutes %> minutes <%= seconds %> seconds and <%= delta%> milliseconds.</p>
</body>
</html>
