package hr.fer.opprp2.web.servlets;

import hr.fer.opprp2.dao.DAOProvider;
import hr.fer.opprp2.dao.jpa.JPAEMProvider;
import hr.fer.opprp2.model.BlogEntry;
import hr.fer.opprp2.model.BlogUser;
import hr.fer.opprp2.model.ProfileMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// /servleti/author/marko
// /servleti/author/marko/new
// /servleti/author/marko/blog_id/edit
// /servleti/author/marko/eid

@WebServlet(urlPatterns = "/servleti/author/*")
public class AuthorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty()) {
            resp.getWriter().println("Invalid URL format");
            return;
        }

        String[] parts = pathInfo.substring(1).split("/");
        String targetNick = parts[0];
        if (parts.length == 1) {
            BlogUser targetBlogUser = DAOProvider.getDAO().getBlogUserByNick(targetNick);
            req.setAttribute("target.author.nick", targetBlogUser.getNick());
            req.setAttribute("blogEntries", targetBlogUser.getBlogEntries());

            if (req.getSession().getAttribute("current.user.id") != null && req.getSession().getAttribute("current.user.nick").equals(targetNick)) {
                List<ProfileMessage> receivedProfileMessages = DAOProvider.getDAO()
                        .getReceivedProfileMessages(targetBlogUser.getId());
                System.out.println(receivedProfileMessages);
                req.setAttribute("receivedMessages", receivedProfileMessages);
            } else if (req.getSession().getAttribute("current.user.nick") != null){
                List<ProfileMessage> sentProfileMessages = DAOProvider.getDAO().getSendProfileMessagesForUser((Long) req.getSession().getAttribute("current.user.id"), targetBlogUser.getId());
                req.setAttribute("sentMessages", sentProfileMessages);
            }
            req.getRequestDispatcher("/WEB-INF/pages/AuthorView.jsp").forward(req, resp);
            return;
        }

        if ("new".equals(parts[1])) {
            if (req.getSession().getAttribute("current.user.id") == null) {
                resp.sendRedirect(req.getContextPath() + "/servleti/main");
                return;
            }

            req.getRequestDispatcher("/WEB-INF/pages/NewOrEditBlog.jsp").forward(req, resp);
        }

        // this is a post id here
        Long blogEntryId = Long.parseLong(parts[1]);
        BlogEntry blogEntry = DAOProvider.getDAO().getBlogEntry(blogEntryId);

        if (parts.length == 2) {
            req.setAttribute("blogEntry", blogEntry);
            req.getRequestDispatcher("/WEB-INF/pages/BlogEntryView.jsp").forward(req, resp);
        } else {
          // thres /edit at the end, render eidt page
            req.setAttribute("blogEntry", blogEntry);
            req.getRequestDispatcher("/WEB-INF/pages/NewOrEditBlog.jsp").forward(req, resp);
        }
    }
}
