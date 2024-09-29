package hr.fer.opprp2.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/servleti/author/*/new")
public class NewBlogPostServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.setContentType("text/plain");
        resp.getWriter().println("CREATE NEW BLOG POST");
        resp.getWriter().flush();
        req.getRequestDispatcher("/WEB-INF/pages/NewOrEditBlog.jsp").forward(req, resp);
    }
}
