package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Trigonometric extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aParam = req.getParameter(" aParam");
        String bParam = req.getParameter("b");

        if ( aParam == null ||  aParam.isBlank()) {
             aParam = "0";
        }

        if (bParam == null || bParam.isBlank()) {
            bParam = "360";
        }

        int a = Integer.parseInt(aParam);
        int b = Integer.parseInt(bParam);

        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }
        if (b > 720) {
            b = a + 720;
        }

        req.setAttribute("a", a);
        req.setAttribute("b", b);
        req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
    }
}
