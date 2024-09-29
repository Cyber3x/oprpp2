package hr.fer.opprp2.dao;

import hr.fer.opprp2.dao.jpa.JPADAOImpl;

public class DAOProvider {

	private static DAO dao = new JPADAOImpl();

	public static DAO getDAO() {
		return dao;
	}

}