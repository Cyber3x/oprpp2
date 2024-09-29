<%@ page contentType="text/html;charset=UTF-8" session="true" %>

<html>
    <head>
        <title>Colors</title>
    </head>
    <body style="background: <%= (String)request.getSession().getAttribute("bgcolor") %>">
        <ul>
            <li><a href="${pageContext.request.contextPath}/colors.jsp">Background color chooser</a></li>
            <li><a href="${pageContext.request.contextPath}/trigonometric?a=0&b=90">Trigonometry table</a></li>
            <li><a href="${pageContext.request.contextPath}/stories/funny.jsp">Funny story</a></li>
            <li><a href="${pageContext.request.contextPath}/report.jsp">OS Report</a></li>
            <li><a href="${pageContext.request.contextPath}/powers?a=1&b=100&n=3">Generate Excel</a></li>
            <li><a href="${pageContext.request.contextPath}/appinfo.jsp">App Info</a></li>
            <li><a href="${pageContext.request.contextPath}/glasanje">Glasanje</a></li>
        </ul>
        <form action="trigonometric" method="GET">
            Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
            Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
            <input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
        </form>
    </body>
</html>
