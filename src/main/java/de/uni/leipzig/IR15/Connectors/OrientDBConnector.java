package de.uni.leipzig.IR15.Connectors;

import java.io.File;

import org.apache.log4j.Logger;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import de.uni.leipzig.IR15.Support.Configuration;


public class OrientDBConnector {
	
	private static Logger log = Logger.getLogger(OrientDBConnector.class);
	private static OGraphDatabase orientdb;

	
	// make a connection to the OrientDB and return an Orient-DB-Object to work with
	public static OGraphDatabase getConnection() {
		
		// get OrientDB connection-configuration from property-file
		Configuration config = Configuration.getInstance("orientdb");
		
		// specify that the filesystem is used
		String url2db = "local:"
				+ config.getPropertyAsString("location");
		
		// create folder if not existent
		File loc = new File(config.getPropertyAsString("location"));
		if (loc.exists() == false) {
			loc.mkdir();
		}
		
		// create an OrientDB-Database
		new ODatabaseDocumentTx(url2db).create();
		
		// create an OrientDB-Database-Object from the specified location
		orientdb = new OGraphDatabase(url2db);
		
		// check, if creation was successful
		if (orientdb != null) {
			log.info("Create connection successful");
		} else {
			log.error("Create connection failed");
		}
		registerShutdownHook(orientdb);
		
		// connect to DB
		orientdb.open("admin", "admin");
		
		return orientdb;
	}

	
	
	public static void destroyConnection() {
		try {
			orientdb.close();
			log.info("Destroy connection successful");
		} catch (Exception e) {
			log.error("Destroy connection failed");
			e.printStackTrace();
		}
	}

	private static void registerShutdownHook(final OGraphDatabase graphDb) {
		// Registers a shutdown hook for the OrientDB instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running example before it's completed)
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				OrientDBConnector.destroyConnection();
			}
		});
	}
}
