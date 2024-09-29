package hr.fer.opprp2.dao;

import hr.fer.opprp2.model.BlogEntry;
import hr.fer.opprp2.model.BlogUser;
import hr.fer.opprp2.model.ProfileMessage;

import java.util.List;

public interface DAO {

    BlogEntry getBlogEntry(Long id) throws DAOException;

    BlogUser getBlogUser(Long id) throws DAOException;

    BlogUser getBlogUserByNick(String nick) throws DAOException;

    List<BlogUser> getBlogUsers() throws DAOException;

    List<ProfileMessage> getReceivedProfileMessages(Long receiverUserId) throws DAOException;

    List<ProfileMessage> getSendProfileMessagesForUser(Long senderUserId, Long targetUserId) throws DAOException;
}