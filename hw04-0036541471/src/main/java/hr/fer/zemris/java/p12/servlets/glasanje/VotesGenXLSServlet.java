package hr.fer.zemris.java.p12.servlets.glasanje;

import hr.fer.zemris.java.p12.dao.DAOProvider;
import hr.fer.zemris.java.p12.model.PollOption;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/servleti/votes-gen-xls")
public class VotesGenXLSServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pollID = req.getParameter("pollID");

        if (pollID == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("missing pollID");
            return;
        }

        List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);

        resp.setContentType("application/vnd.ms-excel");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Content-Disposition", "attachment; filename=\"vote.xls\"");

        int lastRowCreatedIndex = 0;

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            HSSFSheet sheet = workbook.createSheet("votes");

            HSSFRow rowhead = sheet.createRow(lastRowCreatedIndex++);
            rowhead.createCell(0).setCellValue("Option name");
            rowhead.createCell(1).setCellValue("Like count");
            rowhead.createCell(2).setCellValue("Dislike count");
            for (PollOption option : pollOptions) {
                HSSFRow row = sheet.createRow(lastRowCreatedIndex++);
                row.createCell(0).setCellValue(option.getTitle());
                row.createCell(1).setCellValue(option.getLikeCount());
                row.createCell(2).setCellValue(option.getDislikeCount());
            }

            workbook.write(resp.getOutputStream());
        }
        resp.getOutputStream().flush();
    }
}
