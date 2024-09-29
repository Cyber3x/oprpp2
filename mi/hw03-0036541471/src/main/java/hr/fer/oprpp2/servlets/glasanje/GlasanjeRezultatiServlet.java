package hr.fer.oprpp2.servlets.glasanje;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlasanjeRezultatiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rezFileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");

        Path rezFilePath = Path.of(rezFileName);

        Map<String, Integer> votes = new HashMap<>();

        if (!rezFilePath.toFile().exists()) {
            List<GlasanjeEntry> entries = Utils.parseGlasanjeEntries(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
            for (GlasanjeEntry entry : entries) {
                votes.put(entry.id(), 0);
            }
            Files.createFile(rezFilePath);
            Utils.saveVotes(votes, rezFilePath);
        } else {
            votes = Utils.loadVotes(rezFilePath);
        }

        req.setAttribute("votes", votes);

        req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
    }
}
