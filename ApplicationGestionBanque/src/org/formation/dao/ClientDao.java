package org.formation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.formation.beanMetier.Client;

public class ClientDao {
	// Un singleton naif (minimale) qui appelle une seule instance. eviter de
	// repetre à chaque fois de faire new. constructeur privé permet de ne pas
	// utiliser new.

	// declaration des attributs
	private static ClientDao instance;
	private DataSource dataSource;

	// pur dire que me serveur qui s'occupe du driver (on écrit"java:comp/env/"
	// et copi col le reste du web context).
	private String jndiName = "java:comp/env/jdbc/client_h2";

	public static ClientDao getInstance() throws Exception {
		if (instance == null) {
			instance = new ClientDao();
		}
		
		return instance;
	}
	
	private ClientDao() throws Exception {		
		dataSource = getDataSource();
	}

	private DataSource getDataSource() throws NamingException {
		Context context = new InitialContext();
		
		DataSource theDataSource = (DataSource) context.lookup(jndiName);
		
		return theDataSource;
	}
		
	public List<Client>getClients() throws Exception {

		List<Client> Clients = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from Client order by prenom";

			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				
				// retrieve data from result set row
			
				int id = myRs.getInt("id");
				String nom = myRs.getString("nom");
				String prenom = myRs.getString("prenom");
				String adresse = myRs.getString("adresse");
				String codePostal = myRs.getString("codePostal");
				String ville = myRs.getString("ville");
				String tel = myRs.getString("tel");
				String email = myRs.getString("email");
				int IdConseiller = myRs.getInt("IdConseiller");


				// create new Client object
				Client client = new Client(id, IdConseiller, nom, prenom, adresse, codePostal, ville, tel, email);

				// add it to the list of Clients
				Clients.add(client);
			}
			
			return Clients;		
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}

	public void ajouterClient(Client client) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "insert into Client (nom, prenom, adresse, codePostal, ville, tel, email, IdConseiller) values (?, ?, ?, ?, ?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, client.getNom());
			myStmt.setString(2, client.getprenom());
			myStmt.setString(3, client.getAdresse());
			myStmt.setString(4, client.getCodePostal());
			myStmt.setString(5, client.getVille());
			myStmt.setString(6, client.getTel());
			myStmt.setString(7, client.getEmail());
			myStmt.setInt(8, client.getidConseiller());
			
			myStmt.execute();			
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	@SuppressWarnings("unused")
	public Client loadClient(int id) throws Exception {
	
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = getConnection();

			String sql = "select * from Client where id=?";

			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, id);
		
			myRs = myStmt.executeQuery();

			Client client = null;
			
			// retrieve data from result set row
			if (myRs.next()) {

				int idClient = myRs.getInt("id");
				String nom = myRs.getString("nom");
				String prenom = myRs.getString("prenom");
				String adresse = myRs.getString("adresse");
				String codePostal = myRs.getString("codePostal");
				String ville = myRs.getString("ville");
				String tel = myRs.getString("tel");
				String email = myRs.getString("email");
				int IdConseiller = myRs.getInt("IdConseiller");
				
				

				client = new Client(id, IdConseiller, nom, prenom, adresse, codePostal, ville, tel, email);
			
			}
			else {
				throw new Exception("Impossible de trouver le client qui a commeidentifiant:  " + id);
			}

			return client;
		}
		finally {
			close (myConn, myStmt, myRs);
		}
	}
	
	public void msjClient(Client client) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "update Client "+"set nom=?, prenom=?, adresse=?, codePostal=?, ville=?, tel=?, email=?, IdConseiller=?,"
						+ " where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setString(1, client.getNom());
			myStmt.setString(2, client.getprenom());
			myStmt.setString(3, client.getAdresse());
			myStmt.setString(2, client.getCodePostal());
			myStmt.setString(3, client.getVille());
			myStmt.setString(1, client.getTel());
			myStmt.setString(2, client.getEmail());
			myStmt.setInt(4, client.getId());
			myStmt.setInt(4, client.getidConseiller());
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}
		
	}
	
	public void supClient(int client) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = getConnection();

			String sql = "delete from Client where id=?";

			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, client);
			
			myStmt.execute();
		}
		finally {
			close (myConn, myStmt);
		}		
	}	
	
	private Connection getConnection() throws Exception {

		Connection theConn = dataSource.getConnection();
		
		return theConn;
	}
	
	private void close(Connection theConn, Statement theStmt) {
		close(theConn, theStmt, null);
	}
	
	private void close(Connection theConn, Statement theStmt, ResultSet theRs) {

		try {
			if (theRs != null) {
				theRs.close();
			}

			if (theStmt != null) {
				theStmt.close();
			}

			if (theConn != null) {
				theConn.close();
			}
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}	
}