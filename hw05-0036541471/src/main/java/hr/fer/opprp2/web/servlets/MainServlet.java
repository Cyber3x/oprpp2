package hr.fer.opprp2.web.servlets;

import hr.fer.opprp2.dao.DAOProvider;
import hr.fer.opprp2.model.BlogUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static hr.fer.opprp2.utils.PasswordsUtil.getPasswordHash;


@WebServlet(urlPatterns = "/servleti/main")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<BlogUser> blogAuthors = DAOProvider.getDAO().getBlogUsers();
        req.setAttribute("blogAuthors", blogAuthors);

        if ("true".equals(req.getParameter("logout"))) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/servleti/main");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String nick = req.getParameter("nick");
        String password = req.getParameter("password");

        String passwordHash;
        try {
            passwordHash = getPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(e.getMessage());
            return;
        }

        BlogUser targetBlogUser = DAOProvider.getDAO().getBlogUserByNick(nick);
        if (targetBlogUser == null) {
            req.setAttribute("errorMessage", "Invalid nickname or password");
        } else if (!targetBlogUser.getPasswordHash().equals(passwordHash)) {
            req.setAttribute("errorMessage", "Wrong password");
            req.setAttribute("nick", nick);
        } else {
            req.getSession().setAttribute("current.user.id", targetBlogUser.getId());
            req.getSession().setAttribute("current.user.nick", nick);
            req.getSession().setAttribute("current.user.fn", targetBlogUser.getFirstName());
            req.getSession().setAttribute("current.user.ln", targetBlogUser.getLastName());
            resp.sendRedirect(req.getContextPath() + "/servleti/main");
            System.out.println("user logged in");
            return;
        }
        System.out.println("here");
        req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
    }


}
