package hr.fer.zemris.java.p12.dao;

import hr.fer.zemris.java.p12.model.PollOption;
import hr.fer.zemris.java.p12.model.Poll;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

    List<Poll> getAllPolls();

    Poll getPoll(String pollId);

    List<PollOption> getPollOptions(String pollId);

    boolean markDislikeFor(String pollID, String optionID);
    boolean markLikeFor(String pollID, String optionID);
}