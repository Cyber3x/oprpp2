package hr.fer.opprp2.web.servlets;

import hr.fer.opprp2.dao.DAOProvider;
import hr.fer.opprp2.dao.jpa.JPADAOImpl;
import hr.fer.opprp2.dao.jpa.JPAEMProvider;
import hr.fer.opprp2.model.BlogUser;
import hr.fer.opprp2.utils.PasswordsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@WebServlet(urlPatterns = "/servleti/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("current.user.fn") != null) {
            resp.sendRedirect(req.getContextPath() + "/servleti/main");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String nick = req.getParameter("nick");
        String password = req.getParameter("password");

        BlogUser blogUser = new BlogUser();
        blogUser.setFirstName(firstName);
        blogUser.setLastName(lastName);
        blogUser.setEmail(email);
        blogUser.setNick(nick);
        try {
            blogUser.setPasswordHash(PasswordsUtil.getPasswordHash(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        BlogUser targetBlogUser = DAOProvider.getDAO().getBlogUserByNick(nick);
        if (targetBlogUser != null) {
            req.setAttribute("firstName", firstName);
            req.setAttribute("lastName", lastName);
            req.setAttribute("email", email);
            req.setAttribute("nick", nick);
            req.setAttribute("errorMessage", "This nickname is already in use!");
            req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
            return;
        }
        JPAEMProvider.getEntityManager().persist(blogUser);

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }
}
