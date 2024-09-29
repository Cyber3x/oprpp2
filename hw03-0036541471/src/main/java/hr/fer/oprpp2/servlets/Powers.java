package hr.fer.oprpp2.servlets;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Powers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aParam = req.getParameter("a");
        String bParam = req.getParameter("b");
        String nParam = req.getParameter("n");

        if (aParam == null || aParam.isBlank() || bParam == null || bParam.isBlank() || nParam == null || nParam.isBlank()) {
            displayErrorPage(req, resp);
            return;
        }

        int a = Integer.parseInt(aParam);
        int b = Integer.parseInt(bParam);
        int n = Integer.parseInt(nParam);
        if (a < -100 || a > 100 || b < -100 || b > 100 || n < 1 || n > 5) {
            displayErrorPage(req, resp);
            return;
        }

        resp.setContentType("application/vnd.ms-excel");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica-powers.xls\"");

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            for (int i = 0; i < n; i++) {
                HSSFSheet sheet = workbook.createSheet(Integer.valueOf(i).toString());

                HSSFRow rowhead = sheet.createRow(0);
                rowhead.createCell(0).setCellValue("broj");
                rowhead.createCell(1).setCellValue(i + "-th power");
                for (int j = a; j <= b; j++) {
                   HSSFRow row = sheet.createRow(j - a + 1);
                   row.createCell(0).setCellValue(j);
                   row.createCell(1).setCellValue(Math.pow(j, i));
                }
            }

            workbook.write(resp.getOutputStream());
        }
        resp.getOutputStream().flush();
    }

    private void displayErrorPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(getServletContext().getContextPath() + "/WEB-INF/pages/error.jsp").forward(req, resp);
    }
}
