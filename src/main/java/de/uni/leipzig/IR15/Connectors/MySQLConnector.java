package de.uni.leipzig.IR15.Connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Importer.DEXImporter;

public class MySQLConnector {
	protected static Logger log = Logger.getLogger(MySQLConnector.class);
	
	private String database;	
	private String username;
	private String password;
	
	private Connection connection;
	
	public MySQLConnector(String database, String username, String password) {
		setupConnection(database, username, password);
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setupConnection(String database, String username, String password) {
		this.database = database;
		this.password = password;
		this.username = username;
	}

	public boolean testConnection() {
		connection = createConnection();
		boolean successful = connection == null ? false : true;
		destroyConnection();
		return successful;	} 

	public Connection createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Missing JDBC driver");
			e.printStackTrace();
			return null;
		}
 
		try {
			connection = DriverManager.getConnection(database, username, password);
 		} catch (SQLException e) {
			System.out.println("Create connection failed");
			e.printStackTrace();
			return null;
		}
 
		if (connection != null) {
			System.out.println("Create connection successful");
		} else {
			System.out.println("Create connection failed");
		}	
		return connection;
	}
	
	public void destroyConnection() {
		try {
			connection.close();
			System.out.println("Destroy connection successful");
		} catch (SQLException e) {
			System.out.println("Destroy connection failed");
			e.printStackTrace();
		}
	}
}
