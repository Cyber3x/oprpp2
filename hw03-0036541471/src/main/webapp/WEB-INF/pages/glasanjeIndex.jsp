<%@ page import="hr.fer.oprpp2.servlets.glasanje.GlasanjeEntry" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 26/04/2024
  Time: 14:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Glasanje</title>
</head>
<body>
    <h1>Glasajete za omiljeni bend:</h1>
    <p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste
        glasali!</p>
    <ol>
        <% List<GlasanjeEntry> entries = (List<GlasanjeEntry>) request.getAttribute("glasanjeEntryList");
            for (GlasanjeEntry entry : entries) {
                out.print(String.format("<li><a href=\"glasanje-glasaj?id=%s\">%s</a></li>", entry.id(), entry.bandName()));
            }
        %>
    </ol>
</body>
</html>
