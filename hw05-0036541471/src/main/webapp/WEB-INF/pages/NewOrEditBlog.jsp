<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Create Blog Post</title>
    <style>
        .blog-form {
            width: 400px;
            margin: 0 auto;
            padding: 30px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .blog-form h2 {
            text-align: center;
        }

        .blog-form input, .blog-form textarea {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
        }

        .blog-form button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .blog-form button:hover {
            background-color: #45a049;
        }

        .success-message {
            color: green;
            text-align: center;
            margin-bottom: 10px;
        }

        .error-message {
            color: red;
            text-align: center;
            margin-bottom: 10px;
        }
    </style>
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

<div class="blog-form">
    <h2>Create Blog Post</h2>
    <c:if test="${not empty successMessage}">
        <div class="success-message">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/servleti/blog-entry" method="post">
        <c:if test="${not empty blogEntry}">
           <input type="hidden" name="_method" value="put"/>
           <input type="hidden" name="blogEntryId" value="${blogEntry.id}">
        </c:if>
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" value="${blogEntry.title}" required/>

        <label for="content">Content:</label>
        <textarea id="content" name="content" rows="10" required>${blogEntry.text}</textarea>

        <button type="submit">
            <c:choose>
                <c:when test="${empty blogEntry}">Create</c:when>
                <c:otherwise>Edit</c:otherwise>
            </c:choose>
            Blog Post
        </button>
    </form>
</div>

</body>
</html>