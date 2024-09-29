package hr.fer.zemris.java.p12.servlets.glasanje;

import hr.fer.zemris.java.p12.dao.DAOProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/servleti/glasaj-za")
public class ZabiljeziGlas extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pollIDString = req.getParameter("pollID");
        String optionIDString = req.getParameter("optionID");
        int likeOrDislike = Integer.parseInt(req.getParameter("isLike"));

        if (pollIDString == null || optionIDString == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean voted;
        if (likeOrDislike == 1) {
            voted = DAOProvider.getDao().markLikeFor(pollIDString, optionIDString);
        } else {
            voted = DAOProvider.getDao().markDislikeFor(pollIDString, optionIDString);
        }

        if (!voted) System.out.println("failed to mark vote for pollID" + pollIDString + " optionID" + optionIDString);
        resp.sendRedirect(req.getContextPath() + "/servleti/rezultati?pollID=" + pollIDString);
    }
}
