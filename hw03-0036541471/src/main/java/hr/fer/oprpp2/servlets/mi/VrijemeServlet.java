package hr.fer.oprpp2.servlets.mi;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class VrijemeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionBgUrl = (String) req.getSession().getAttribute("bgUrl");

        if (sessionBgUrl == null) {
            if (new Random().nextBoolean()) {
                sessionBgUrl = req.getContextPath() + "/mi/squares";
            } else {
                sessionBgUrl = req.getContextPath() + "/mi/ellipses";
            }
        }
        req.setAttribute("bgUrl", sessionBgUrl);
        req.getRequestDispatcher("/WEB-INF/pages/vrijeme.jsp").forward(req, resp);
    }
}
