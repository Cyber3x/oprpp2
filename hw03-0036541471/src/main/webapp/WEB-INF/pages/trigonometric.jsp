<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 26/04/2024
  Time: 13:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Trig table</title>
</head>
<body>
   <table>
       <thead>
           <tr>
               <th>Angle</th>
               <th>Sing</th>
               <th>Cos</th>
           </tr>
       </thead>
       <tbody>
       <%
           for (int i = (int)request.getAttribute("a"); i <= (int)request.getAttribute("b"); i++ ){
              double sin = Math.sin(Math.toRadians(i));
              double cos = Math.cos(Math.toRadians(i));

              out.print(String.format("<tr><td>%d</td><td>%s</td><td>%s</td></tr>", i, sin, cos));
           }
       %>
       </tbody>
   </table>
</body>
</html>
