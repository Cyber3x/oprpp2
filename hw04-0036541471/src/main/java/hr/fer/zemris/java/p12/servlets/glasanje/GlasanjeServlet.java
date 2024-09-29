package hr.fer.zemris.java.p12.servlets.glasanje;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pollId = req.getParameter("pollID");

        if (pollId == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("text/plain");
            resp.getWriter().println("pollID is missing mate.");
            return;
        }

        Poll poll = DAOProvider.getDao().getPoll(pollId);
        if (poll == null) {
            resp.getWriter().println("No poll with this id found, sorry mate.");
            return;
        }

        List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollId);
        req.setAttribute("poll", poll);
        req.setAttribute("pollOptions", pollOptions);
        req.getRequestDispatcher("/WEB-INF/pages/glasanje.jsp").forward(req, resp);
    }
}
