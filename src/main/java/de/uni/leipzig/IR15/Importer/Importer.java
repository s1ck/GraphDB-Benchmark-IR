package de.uni.leipzig.IR15.Importer;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Connectors.MySQLConnector;
import de.uni.leipzig.IR15.Support.Configuration;

public abstract class Importer {
	protected static Configuration graphConfiguration;
	protected static Configuration mySQLConfiguration;
	protected static Connection mySQLConnection;
	protected static MySQLConnector mySQLConnector;	
	
	protected static Logger log = Logger.getLogger(Importer.class);

	public void reset() {
		File location = new File(graphConfiguration.getPropertyAsString("location"));
		if (location.exists()) {
			if (location.isDirectory()) {
				recursiveDeleteDirectory(location);
			}
			
			if (location.isFile()) {
				location.delete();
			}
		}
	}
	
	public void setUp(String graphDBName) {
		// load properties
		mySQLConfiguration = Configuration.getInstance("mysql");
		graphConfiguration = Configuration.getInstance(graphDBName);

		// connect to mysql
		String database = "jdbc:mysql://" 
		+ mySQLConfiguration.getPropertyAsString("host") 
		+ ":" + mySQLConfiguration.getPropertyAsString("port") 
		+ "/" + mySQLConfiguration.getPropertyAsString("database");
		
		mySQLConnector = new MySQLConnector(database, 
				mySQLConfiguration.getPropertyAsString("username"), 
				mySQLConfiguration.getPropertyAsString("password")
				);
		
		mySQLConnection = mySQLConnector.createConnection();		
	}
	
	public abstract void setUp();	
	public abstract void importData();
	public abstract String getName();
	public abstract Object getDatabaseInstance();

	
	public void tearDown() {
		mySQLConnector.destroyConnection();		
	}
		
	private void recursiveDeleteDirectory(File path) {
		for (File file : path.listFiles()) {
			if (file.isDirectory())
				recursiveDeleteDirectory(file);
			file.delete();
	    }
	    path.delete();
	}
	protected Integer getMysqlRowCount(String table) {
	    String query = "SELECT COUNT(*) FROM " + table;
	    Integer count = null;
	    try {
	      Statement st = mySQLConnection.createStatement();
	      ResultSet rs = st.executeQuery(query);
	      
	      while (rs.next()) {
	        count = Integer.valueOf(rs.getInt("COUNT(*)"));
	      }
	    }
	    catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	    }
	    return count;
	}	
}
