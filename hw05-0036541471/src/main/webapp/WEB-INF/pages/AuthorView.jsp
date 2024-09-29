<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 06/06/2024
  Time: 00:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Author view</title>
</head>
<body>
<div>
    <%
        if (request.getSession().getAttribute("current.user.fn") != null) {
    %>
    <%= request.getSession().getAttribute("current.user.fn") %> <%= request.getSession()
        .getAttribute("current.user.ln") %>
    <a href="<%= request.getContextPath() %>/servleti/main?logout=true">Log out</a>
    <%
    } else {
    %>
    User not logged in!
    <%
        }
    %>
</div>
<h1><%= request.getAttribute("target.author.nick")%>
</h1>
<h3>Author's blogs</h3>
<c:choose>
    <c:when test="${empty blogEntries}">
        <p>No blog entries for this author yet.</p>
    </c:when>
    <c:otherwise>
        <ol>
            <c:forEach var="blogEntry" items="${blogEntries}">
                <li>
                    <a href="${pageContext.request.contextPath}/servleti/author/<%= request.getAttribute("target.author.nick")%>/${blogEntry.id}">${blogEntry.title}</a>
                </li>
            </c:forEach>
        </ol>
    </c:otherwise>
</c:choose>
<%
    String currentNick = (String) request.getSession().getAttribute("current.user.nick");
    String targetNick = (String) request.getAttribute("target.author.nick");
    if (currentNick != null && currentNick.equals(targetNick)) {
%>
<a href="<%= request.getContextPath() %>/servleti/author/<%= targetNick %>/new">Create new blog post</a>
<%
    }
%>

<%
    if (currentNick != null && !currentNick.equals(targetNick)) {
%>
    <p>Write a message</p>
    <form action="${pageContext.request.contextPath}/servleti/profile-message" method="post">
        <input type="hidden" name="creatorNick" value="${pageContext.request.session.getAttribute("current.user.nick")}">
        <input type="hidden" name="targetNick" value="${pageContext.request.getAttribute("target.author.nick")}">
        <textarea name="messageText"></textarea>
        <button type="submit">Send</button>
    </form>
<%
    }
%>


<%
    if (currentNick != null && currentNick.equals(targetNick)) {
%>
<h1>Your messages</h1>
<c:forEach items="${receivedMessages}" var="m">
    <div>
        <h3>${m.creator.firstName}</h3>
        <p>${m.messageText}</p>
        <p>${m.createdAt}</p>
    </div>
</c:forEach>
<%
    }
%>

<%
    if (currentNick != null && !currentNick.equals(targetNick)) {
%>
<h1>Sent messages to this user</h1>
<c:forEach items="${sentMessages}" var="m">
    <div>
        <h3>${m.creator.firstName}</h3>
        <p>${m.messageText}</p>
        <p>${m.createdAt}</p>
    </div>
</c:forEach>
<%
    }
%>
</body>
</html>
