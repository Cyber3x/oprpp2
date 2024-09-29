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

public class GlasanjeGlasajServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String id = req.getParameter("id");

        Map<String, Integer> votes = new HashMap<>();

        Path rezulatiFilePath = Path.of(fileName);

        // load votes
        if (!rezulatiFilePath.toFile().exists()) {
            List<GlasanjeEntry> entries = Utils.parseGlasanjeEntries(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
            for (GlasanjeEntry entry : entries) {
                votes.put(entry.id(), 0);
            }
            Files.createFile(rezulatiFilePath);
        } else {
            votes = Utils.loadVotes(rezulatiFilePath);
        }

        // increment
        if (votes.containsKey(id)) {
            votes.put(id, votes.get(id) + 1);
        }

        // write results down
        Utils.saveVotes(votes, rezulatiFilePath);
        req.setAttribute("votes", votes);

        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }
}
