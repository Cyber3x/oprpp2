<%@ page import="hr.fer.opprp2.model.BlogEntry" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: cyber
  Date: 14/06/2024
  Time: 00:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>
        <c:choose>
            <c:when test="${empty blogEntry}">Not found</c:when>
            <c:otherwise>${blogEntry.title}</c:otherwise>
        </c:choose>
    </title>
    <style>
        .comment-form {
            display: flex;
            flex-direction: column;
            padding: 1rem 0;
        }
        .comment {
            h3 {
                margin-bottom: 0;
            }
            p {
                margin: 0;
            }
        }

    </style>
</head>
<body>
<div>
    <%
        if (request.getSession().getAttribute("current.user.fn") != null) {
    %>
    <%= request.getSession().getAttribute("current.user.fn") %> <%= request.getSession().getAttribute("current.user.ln") %>
    <a href="<%= request.getContextPath() %>/servleti/main?logout=true">Log out</a>
    <%
    } else {
    %>
    User not logged in!
    <%
        }
    %>
</div>

<c:choose>
    <c:when test="${empty blogEntry}">
        <p>Blog entry with this id not found</p>
    </c:when>
    <c:otherwise>
        <h1>${blogEntry.title}</h1>
        <p>${blogEntry.text}</p>
        <% if (
                ((BlogEntry)request.getAttribute("blogEntry")).getCreator().getId() ==
                request.getSession().getAttribute("current.user.id")) {
        %>
        <a href="${pageContext.request.contextPath}/servleti/author/${blogEntry.creator.nick}/${blogEntry.id}/edit">Edit this blog post</a>
        <%
            }
        %>

        <c:if test="${not empty pageContext.request.getSession().getAttribute(\"current.user.id\")}">
            <form action="${pageContext.request.contextPath}/servleti/blog-comment" method="post" class="comment-form">
                <label for="comment">Your comment:</label>
                <input type="hidden" name="blogEntryId" value="${blogEntry.id}">
                <textarea id="comment" name="comment" rows="3" placeholder="Write your comment here..." required></textarea>
                <button type="submit">Submit Comment</button>
            </form>
        </c:if>

        <c:choose>
            <c:when test="${empty blogEntry.comments}">
                <p>No comments yet on this blog.</p>
            </c:when>

            <c:otherwise>
                <h1>Komentari</h1>
               <c:forEach var="comment" items="${blogEntry.comments}">
                   <div class="comment">
                       <h3>${comment.creator.nick}</h3>
                       <p>${comment.content}</p>
                   </div>
               </c:forEach>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
</body>
</html>
