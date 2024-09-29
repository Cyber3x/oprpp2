package hr.fer.oprpp2.servlets.mi;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class SetVar extends HttpServlet {
    private static final String URL_SQUARES = "/mi/squares";
    private static final String URL_ELLIPSES = "/mi/ellipses";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String bgVar = req.getParameter("bgVar");

        switch (bgVar) {
            case "squares":
                req.getSession().setAttribute("bgUrl", req.getContextPath() + URL_SQUARES);
                break;
            case "ellipses":
                req.getSession().setAttribute("bgUrl", req.getContextPath() + URL_ELLIPSES);
                break;
            default:
                req.getSession().removeAttribute("bgUrl");
        }

        resp.sendRedirect(req.getContextPath() + "/mi/vrijeme");
    }
}
