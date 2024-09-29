package hr.fer.zemris.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ListajModernije {

	public static void main(String[] args) {
		
		// Za Derby registracija nije potrebna -- maknut ćemo u sljedećim primjerima.
//		try {
//			Class.forName("org.apache.derby.client.ClientAutoloadedDriver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			System.exit(0);
//		}

		String dbName="baza1DB";
		String connectionURL = "jdbc:derby://localhost:1527/" + dbName;
		
		// Alternativa uporabi parametara je koristiti URL koji ima na kraju ;user=perica;password=pero
		java.util.Properties dbProperties = new java.util.Properties();
		dbProperties.setProperty("user", "perica");
		dbProperties.setProperty("password", "pero");

		try(Connection con = DriverManager.getConnection(connectionURL, dbProperties)) {
			try(PreparedStatement pst = con.prepareStatement("SELECT id, title, message, createdOn, userEMail from Poruke order by id")) {
				try(ResultSet rset = pst.executeQuery()) {
					while(rset.next()) {
						long id = rset.getLong(1); // ili rset.getLong("id"); 
						String title = rset.getString(2); // ili rset.getString("title");
						String message = rset.getString(3); // ili rset.getString("message");
						Date createdOn = rset.getTimestamp(4); // ili rset.getTimestamp("createdOn");
						String userEMail = rset.getString(5); // ili rset.getString("userEMail");
						System.out.println("Zapis "+id);
						System.out.println("===================================");
						System.out.println("Naziv: "+title);
						System.out.println("Poruka: "+message);
						System.out.println("Stvoreno: "+createdOn);
						System.out.println("EMail: "+userEMail);
						System.out.println("");
					}
				}
			}
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
}
