package hr.fer.oprpp2.servlets;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class ReportImage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DefaultPieDataset<String> results = new DefaultPieDataset<>();
        results.setValue("Linux", 29);
        results.setValue("Mac", 20);
        results.setValue("Windows", 51);

        JFreeChart chart = ChartFactory.createPieChart(
                "Operating systems",
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
