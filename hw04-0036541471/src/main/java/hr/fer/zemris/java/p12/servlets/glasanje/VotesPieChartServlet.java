package hr.fer.zemris.java.p12.servlets.glasanje;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet("/servleti/votes-pie")
public class VotesPieChartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pollID = req.getParameter("pollID");

        if (pollID == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("pollID not specified");
            return;
        }

        List<PollOption> options = DAOProvider.getDao().getPollOptions(pollID);

        DefaultPieDataset<String> results = new DefaultPieDataset<>();

        for (PollOption option : options) {
            results.setValue(option.getTitle(), option.getLikeCount());
        }

        JFreeChart chart = ChartFactory.createPieChart(
                null,
                results,
                true,
                true,
                false
                                                      );

        resp.setContentType("image/png");
        resp.setStatus(HttpServletResponse.SC_OK);
        OutputStream os = resp.getOutputStream();

        ChartUtils.writeChartAsPNG(os, chart, 800, 800);
        os.flush();
    }
}
