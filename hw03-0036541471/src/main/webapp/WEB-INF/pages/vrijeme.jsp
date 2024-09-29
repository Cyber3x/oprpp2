<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
    <title>Vrijeme</title>
    <style>
        body {
            background: url("<%= request.getAttribute("bgUrl")%>");
        }
    </style>
</head>
<body>
    <h1> <%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%></h1>

    <div>
        <a href="${pageContext.request.contextPath}/mi/setVar?bgVar=squares"><button>Stalno kvadratiće</button></a>
        <a href="${pageContext.request.contextPath}/mi/setVar?bgVar=ellipses"><button>Stalno elipse</button></a>
        <a href="${pageContext.request.contextPath}/mi/setVar?bgVar=random"><button>Želim nasumično</button></a>
    </div>
</body>
</html>
