package hr.fer.zemris.java.p12.dao.sql;

import hr.fer.zemris.java.p12.model.PollOption;
import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.model.Poll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author marcupic
 */
public class SQLDAO implements DAO {
    @Override
    public List<Poll> getAllPolls() {
        List<Poll> polls = new ArrayList<>();
        Connection conn = SQLConnectionProvider.getConnection();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM POLLS")){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Poll poll = new Poll(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)
                );
                polls.add(poll);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return polls;
    }

    @Override
    public Poll getPoll(String pollId) {
        Connection conn = SQLConnectionProvider.getConnection();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM POLLS WHERE ID = ?")){
            ps.setString(1, pollId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Poll(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)
                );
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PollOption> getPollOptions(String pollId) {
        List<PollOption> options = new ArrayList<>();
        Connection conn = SQLConnectionProvider.getConnection();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM POLLOPTIONS WHERE POLLID = ?")){
            ps.setString(1, pollId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                options.add(new PollOption(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getInt(6)
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return options;
    }

    @Override
    public boolean markLikeFor(String pollID, String optionID) {
        Connection conn = SQLConnectionProvider.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE POLLOPTIONS SET likecount = likecount + 1 WHERE pollId = ? AND id = ?");

            ps.setInt(1, Integer.parseInt(pollID));
            ps.setInt(2, Integer.parseInt(optionID));

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean markDislikeFor(String pollID, String optionID) {
        Connection conn = SQLConnectionProvider.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE POLLOPTIONS SET dislikecount = dislikecount + 1 WHERE pollId = ? AND id = ?");

            ps.setInt(1, Integer.parseInt(pollID));
            ps.setInt(2, Integer.parseInt(optionID));

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}