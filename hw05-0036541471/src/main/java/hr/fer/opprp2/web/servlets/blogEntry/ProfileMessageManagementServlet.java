package hr.fer.opprp2.web.servlets.blogEntry;

import hr.fer.opprp2.dao.DAOProvider;
import hr.fer.opprp2.dao.jpa.JPAEMProvider;
import hr.fer.opprp2.model.BlogUser;
import hr.fer.opprp2.model.ProfileMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/servleti/profile-message")
public class ProfileMessageManagementServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String targetNick = req.getParameter("targetNick");
        String creatorNick = req.getParameter("creatorNick");
        String messageText = req.getParameter("messageText");

        BlogUser target = DAOProvider.getDAO().getBlogUserByNick(targetNick);
        BlogUser creator = DAOProvider.getDAO().getBlogUserByNick(creatorNick);
        ProfileMessage profileMessage = new ProfileMessage(messageText, creator, target);

        JPAEMProvider.getEntityManager().persist(profileMessage);

        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + target.getNick());
    }
}
