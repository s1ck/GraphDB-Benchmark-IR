package de.uni.leipzig.IR15.Importer;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Connectors.MySQLConnector;
import de.uni.leipzig.IR15.Support.Configuration;

/**
 * Abstract base class for all database importer.
 *
 * @author robbl
 *
 */
public abstract class Importer {
	protected static Configuration graphConfiguration;
	protected static Configuration mySQLConfiguration;
	protected static Connection mySQLConnection;

	public static enum RelTypes {
		CO_S,
		CO_N
	}

	protected static Logger log = Logger.getLogger(Importer.class);

	/**
	 * Deletes all files associated with the repective graph database
	 */
	public void reset() {
		File location = new File(
				graphConfiguration.getPropertyAsString("location"));
		if (location.exists()) {
			if (location.isDirectory()) {
				recursiveDeleteDirectory(location);
			}

			if (location.isFile()) {
				location.delete();
			}
		} else {
			log.info("Database storage location does not exist, no need to reset.");
		}
	}

	/**
	 * Initializes the configuration instances
	 *
	 * @param graphDBName
	 *            the name of the graph database which is used (e.g. neo4j,
	 *            orientdb, dex)
	 */
	public void setUp(String graphDBName) {
		// load properties
		graphConfiguration = Configuration.getInstance(graphDBName);
		mySQLConnection = MySQLConnector.getConnection(graphConfiguration.getPropertyAsString("dataset"));
	}

	/**
	 * This method is called before the import is started
	 */
	public abstract void setUp();

	/**
	 * This method imports the data
	 */
	public abstract void importData();

	/**
	 * Returns the name of the importer
	 *
	 * @return importer's name
	 */
	public abstract String getName();

	/**
	 * Cleanup mysql connection after running importer.
	 */
	public void tearDown() {
		MySQLConnector.destroyConnection();
	}

	/**
	 * Delete a directory recursively.
	 *
	 * @param path
	 */
	private void recursiveDeleteDirectory(File path) {
		for (File file : path.listFiles()) {
			if (file.isDirectory()) {
				recursiveDeleteDirectory(file);
			}
			file.delete();
		}
		path.delete();
	}

	/**
	 * Get mysql row counts.
	 *
	 * @param table
	 * @return
	 */
	protected Integer getMysqlRowCount(String table) {
		String query = "SELECT COUNT(*) FROM " + table;
		Integer count = null;
		try {
			Statement st = mySQLConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				count = Integer.valueOf(rs.getInt("COUNT(*)"));
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return count;
	}
}