package de.uni.leipzig.IR15.Connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class MySQLConnector {
	private static Logger log = Logger.getLogger(MySQLConnector.class);
	
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
			log.error("Missing JDBC driver");
			e.printStackTrace();
			return null;
		}
 
		try {
			connection = DriverManager.getConnection(database, username, password);
 		} catch (SQLException e) {
 			log.error("MySQL Create connection failed");
			e.printStackTrace();
			return null;
		}
 
		if (connection != null) {
			log.info("MySQL Create connection successful");
		} else {
			log.error("MySQL Create connection failed");
		}	
		return connection;
	}
	
	public void destroyConnection() {
		try {
			connection.close();
			log.info("Destroy connection successful");
		} catch (SQLException e) {
			log.error("Destroy connection failed");
			e.printStackTrace();
		}
	}
}
