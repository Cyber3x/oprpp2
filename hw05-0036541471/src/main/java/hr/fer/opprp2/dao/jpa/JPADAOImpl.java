package hr.fer.opprp2.dao.jpa;

import hr.fer.opprp2.dao.DAO;
import hr.fer.opprp2.model.BlogEntry;
import hr.fer.opprp2.dao.DAOException;
import hr.fer.opprp2.model.BlogUser;
import hr.fer.opprp2.model.ProfileMessage;
import jakarta.persistence.NoResultException;
import jdk.jfr.Frequency;

import java.util.List;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
        return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}

	@Override
	public BlogUser getBlogUser(Long id) throws DAOException {
        return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
	}

	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		try {
			return JPAEMProvider.getEntityManager().createQuery("SELECT u FROM BlogUser u WHERE u.nick = :nick", BlogUser.class)
					.setParameter("nick", nick)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<BlogUser> getBlogUsers() throws DAOException {
		return JPAEMProvider.getEntityManager().createQuery("SELECT u FROM BlogUser u", BlogUser.class).getResultList();
	}

	@Override
	public List<ProfileMessage> getReceivedProfileMessages(Long receiverUserId) throws DAOException {
		return JPAEMProvider.getEntityManager().createQuery("SELECT m FROM ProfileMessage m WHERE m.target.id = :targetId ORDER BY m.createdAt desc ", ProfileMessage.class)
				.setParameter("targetId", receiverUserId).getResultList();
	}

	@Override
	public List<ProfileMessage> getSendProfileMessagesForUser(Long senderUserId, Long targetUserId) throws DAOException {
		return JPAEMProvider.getEntityManager().createQuery("SELECT m FROM ProfileMessage m WHERE m.target.id = :targetId and m.creator.id = :creatorId ORDER BY m.createdAt desc ", ProfileMessage.class)
				.setParameter("targetId", targetUserId)
				.setParameter("creatorId", senderUserId)
				.getResultList();
	}
}