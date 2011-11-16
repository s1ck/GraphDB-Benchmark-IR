package de.uni.leipzig.IR15.Importer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

import de.uni.leipzig.IR15.Connectors.Neo4JConnector;

public class Neo4JImporter extends Importer {
	public static enum RelTypes implements RelationshipType
	{
		CO_S,
		CO_N
	}

	private Index<Node> nodeIndex;
	private Integer operationsPerTx;
	private GraphDatabaseService neo4j;

	@Override
	public void setUp() {
		super.setUp("neo4j");		
		operationsPerTx = graphConfiguration.getPropertyAsInteger("operations_per_transaction");		
		// connect to neo4j and create an index on the nodes
		neo4j = Neo4JConnector.getConnection();
		nodeIndex = neo4j.index().forNodes("words");				
	}

	@Override
	public void tearDown() {
		// shutdown the connections
		Neo4JConnector.destroyConnection();
		super.tearDown();
	}
	
	public Object getDatabaseInstance() {
		return neo4j;
	}
		
	/**
	 * @param args
	 */
	public void importData() {		
		// transfer the data from mysql to neo4j		
		transferData();		
	}
	
	private void transferData()
	{		
		importWords(mySQLConnection, neo4j);
		importCooccurrences(mySQLConnection, neo4j, RelTypes.CO_N);
		importCooccurrences(mySQLConnection, neo4j, RelTypes.CO_S);
	}
	
	private void registerShutdownHook( final GraphDatabaseService graphDb ) {
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running example before it's completed)
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	private void importCooccurrences(Connection mySQL, GraphDatabaseService neo4j, RelationshipType relType) {
		String table = relType.toString().toLowerCase();
		Integer count = getMysqlRowCount(table);
		System.out.println("Importing " + count + " cooccurrences (" + table + ")");
		Integer step  = 0;
		
	    String query = "SELECT * FROM " + table;
	    
	    // start transaction
	    Transaction tx = neo4j.beginTx();	    
	    try {	    	
	      Statement st = mySQL.createStatement();
	      st.setFetchSize(Integer.MIN_VALUE);
	      ResultSet rs = st.executeQuery(query);
	      
	      int i = 0;
	      
	      while (rs.next()) {
	        Integer w1_id 	= rs.getInt("w1_id");
	        Integer w2_id 	= rs.getInt("w2_id");
	        Integer sig 	= rs.getInt("sig");
	        Integer freq 	= rs.getInt("freq");
	        	        
	        Node source = nodeIndex.get("w_id", w1_id).getSingle();
	        Node target = nodeIndex.get("w_id", w2_id).getSingle();
	        Relationship edge = source.createRelationshipTo(target, relType);
	        	      	        
	        edge.setProperty("freq", freq);
	        edge.setProperty("sig", sig);
	        
	        if(++i % operationsPerTx == 0)
	        {
	        	// commit
	        	tx.success();
	        	tx.finish();
	        	tx = neo4j.beginTx();
	        	System.out.println(".");
	        }
	        
	        step++;	       
	      }
	      tx.success();
	    } catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	    } finally {
	    	tx.finish();
	    }
	}
	
	private void importWords(Connection mySQL, GraphDatabaseService neo4j) {		
	    String query = "SELECT * FROM words";
	    Transaction tx = neo4j.beginTx();
	    try {
	      Statement st = mySQL.createStatement();
	      st.setFetchSize(Integer.MIN_VALUE);
	      ResultSet rs = st.executeQuery(query);
	      	      
	      int i = 0;
	      
	      while (rs.next()) {
	        String word 	= rs.getString("word");
	        Integer word_id = rs.getInt("w_id");
	        
	        Node vertex = neo4j.createNode();	        
	        vertex.setProperty("w_id", word_id);
	        vertex.setProperty("word", word);
	        // store the node at the index
	        nodeIndex.add(vertex, "w_id", word_id);
	        
	        if(++i % operationsPerTx == 0)
	        {
	        	// commit
	        	tx.success();
	        	tx.finish();
	        	tx = neo4j.beginTx();
	        	System.out.println(".");
	        }        
	      }
	      tx.success();
	    } catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	    }
	    finally {
	    	tx.finish();
	    }
	}

	@Override
	public String getName() {		
		return "neo4j";
	}
}
