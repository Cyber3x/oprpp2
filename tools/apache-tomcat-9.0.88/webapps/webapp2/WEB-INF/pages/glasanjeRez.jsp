<%@ page import="java.util.Map" %>
<%@ page import="hr.fer.oprpp2.servlets.glasanje.GlasanjeEntry" %><%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 26/04/2024
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Rezultati glasanja</h1>
    <p>Ovo su rezultati glasanja.</p>
    <table>
        <thead><tr><th>ID</th><th>Broj glasova</th></tr></thead>
        <tbody>
            <% Map<String, Integer> votes = (Map<String, Integer>) request.getAttribute("votes");
                for (String s : votes.keySet()) {
                    out.println(String.format("<tr><td>%s</td><td>%s</td></tr>", s, votes.get(s)));
                }
            %>
        </tbody>
    </table>
    <h2>Grafički prikaz rezultata</h2>
    <img alt="Pie-chart" src="/glasanje-grafika" width="400" height="400" />
    <h2>Rezultati u XLS formatu</h2>
    <p>Rezultati u XLS formatu dostupni su <a href="/glasanje-xls">ovdje</a></p>
    <h2>Razno</h2>
    <p>Primjeri pjesama pobjedničkih bendova:</p>
    <ul>
        <li><a href="https://www.youtube.com/watch?v=z9ypq6_5bsg" target="_blank">The
            Beatles</a></li>
        <li><a href="https://www.youtube.com/watch?v=H2di83WAOhU" target="_blank">The
            Platters</a></li>
    </ul>
</body>
</html>
