package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetColor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bgColor = req.getParameter("bgcolor");

        if (bgColor == null || bgColor.trim().isBlank()) {
            bgColor = "ffffff";
        }

        req.getSession().setAttribute("bgcolor", "#" + bgColor);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
