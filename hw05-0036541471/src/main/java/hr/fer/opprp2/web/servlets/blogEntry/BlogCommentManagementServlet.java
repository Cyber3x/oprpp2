package hr.fer.opprp2.web.servlets.blogEntry;

import hr.fer.opprp2.dao.DAOProvider;
import hr.fer.opprp2.dao.jpa.JPAEMProvider;
import hr.fer.opprp2.model.BlogComment;
import hr.fer.opprp2.model.BlogEntry;
import hr.fer.opprp2.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/servleti/blog-comment")
public class BlogCommentManagementServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long blogEntryId = Long.valueOf(req.getParameter("blogEntryId"));
        String commentText = req.getParameter("comment");
        Long currentUserId = (Long) req.getSession().getAttribute("current.user.id");

        BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(blogEntryId);
        BlogUser blogUser = DAOProvider.getDAO().getBlogUser(currentUserId);
        BlogComment comment = new BlogComment(commentText, blogUser, blogEntry);
        JPAEMProvider.getEntityManager().persist(comment);

        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + blogEntry.getCreator().getNick() + "/" + blogEntryId);
    }
}
