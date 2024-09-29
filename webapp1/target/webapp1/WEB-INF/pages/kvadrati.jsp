<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<p>Evo traženih rezultata.</p>

<table border="1">
<tr><th>Broj</th><th>Njegov kvadrat</th></tr>
<c:forEach var="zapis" items="${rezultati}">
  <tr><td>${zapis.broj}</td><td>${zapis.kvadrat}</td></tr>
</c:forEach>
</table>
