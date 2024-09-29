package hr.fer.opprp2.web.servlets.blogEntry;


import hr.fer.opprp2.dao.DAOProvider;
import hr.fer.opprp2.dao.jpa.JPAEMProvider;
import hr.fer.opprp2.model.BlogEntry;
import hr.fer.opprp2.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/servleti/blog-entry")
public class BlogEntryManagementServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("current.user.id") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long authorId = (Long) req.getSession().getAttribute("current.user.id");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        BlogUser author = DAOProvider.getDAO().getBlogUser(authorId);
        BlogEntry blogEntry;
        if (req.getParameter("_method").equals("put")) {
            Long blogEntryId = Long.parseLong(req.getParameter("blogEntryId"));
            blogEntry = DAOProvider.getDAO().getBlogEntry(blogEntryId);
            blogEntry.setTitle(title);
            blogEntry.setText(content);
        } else {
            blogEntry = new BlogEntry(title, content, author);
            JPAEMProvider.getEntityManager().persist(blogEntry);
        }
        resp.sendRedirect(req.getContextPath() +
                                  "/servleti/author/" + author.getNick() + "/" + blogEntry.getId());
    }
}
