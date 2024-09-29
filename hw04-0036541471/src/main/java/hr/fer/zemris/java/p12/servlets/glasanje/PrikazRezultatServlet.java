package hr.fer.zemris.java.p12.servlets.glasanje;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servleti/rezultati")
public class PrikazRezultatServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pollID = req.getParameter("pollID");

        if (pollID == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);
        req.setAttribute("pollOptions", pollOptions);
        req.setAttribute("pollID", pollID);
        req.getRequestDispatcher("/WEB-INF/pages/rezultati.jsp").forward(req, resp);
    }
}
