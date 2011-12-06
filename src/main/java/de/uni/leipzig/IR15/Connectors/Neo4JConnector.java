package de.uni.leipzig.IR15.Connectors;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import de.uni.leipzig.IR15.Support.Configuration;

public class Neo4JConnector {
	private static Logger log = Logger.getLogger(Neo4JConnector.class);	
	private static GraphDatabaseService neo4j;
	
	public static GraphDatabaseService getConnection() {
		Configuration config = Configuration.getInstance("neo4j");		
		
		// connect to neo4j and create an index on the nodes
		neo4j = new EmbeddedGraphDatabase(config.getPropertyAsString("location"));		
		if (neo4j != null) {
			log.info("Create connection successful");
		} else {
			log.error("Create connection failed");
		}
		registerShutdownHook(neo4j);
		
		return neo4j;
	}
	
	public static void destroyConnection() {
		try {
			neo4j.shutdown();
			log.info("Destroy connection successful");
		} catch (Exception e) {
			log.error("Destroy connection failed");
			e.printStackTrace();
		}
	}
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb ) {
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running example before it's completed)
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            Neo4JConnector.destroyConnection();
	        }
	    } );
	}
}
