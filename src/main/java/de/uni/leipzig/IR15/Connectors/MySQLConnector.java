package de.uni.leipzig.IR15.Connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Support.Configuration;

/**
 * Class to handle connection to the mysql database.
 *
 * @author IR-Team
 *
 */

public class MySQLConnector {
	private static Logger log = Logger.getLogger(MySQLConnector.class);
	private static Connection connection;

	/**
	 * Create and return a database connection.
	 *
	 * @return database connection
	 */
	public static Connection getConnection() {
		Configuration mySQLConfiguration = Configuration.getInstance("mysql");

		String database = "jdbc:mysql://"
				+ mySQLConfiguration.getPropertyAsString("host") + ":"
				+ mySQLConfiguration.getPropertyAsString("port") + "/"
				+ mySQLConfiguration.getPropertyAsString("database");

		String username = mySQLConfiguration.getPropertyAsString("username");
		String password = mySQLConfiguration.getPropertyAsString("password");

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			log.error("Missing JDBC driver");
			e.printStackTrace();
			return null;
		}

		try {
			connection = DriverManager.getConnection(database, username,
					password);
		} catch (SQLException e) {
			log.error("Create connection failed");
			e.printStackTrace();
			return null;
		}

		if (connection != null) {
			log.info("Create MySQL-connection successful");
		} else {
			log.error("Create connection failed");
		}
		return connection;
	}

	/**
	 * Destroy a database connection.
	 */
	public static void destroyConnection() {
		try {
			connection.close();
			log.info("Destroy connection successful");
		} catch (SQLException e) {
			log.error("Destroy connection failed");
			e.printStackTrace();
		}
	}
}
