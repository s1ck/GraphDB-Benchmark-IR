package de.uni.leipzig.IR15.Importer;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.neo4j.graphdb.RelationshipType;

import edu.upc.dama.dex.core.DEX;
import edu.upc.dama.dex.core.GraphPool;

public class DEXImporter extends Importer {
	static enum RelTypes
	{
		CO_S,
		CO_N
	}

	private DEX dexConnector;
	private GraphPool dex;

	@Override
	public void setUp() {
		super.setUp("dex");
		
		dexConnector = new DEX();
		try {
			dex = dexConnector.create(graphConfiguration.getPropertyAsString("location"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		registerShutdownHook();
	}

	@Override
	public void tearDown() {
		// shutdown the connections
		dex.close();
	    dexConnector.close();
		mySQLConnector.destroyConnection();
		
		reset();
	}
	
	/**
	 * @param args
	 */
	public void importData() {													
		// transfer the data from mysql to neo4j		
		transferData();											
	}
	
	public Object getDatabaseInstance() {
		return dex;
	}
	
	private void transferData()
	{		
		importWords(mySQL, dex);
		importCooccurrences(mySQL, dex, RelTypes.CO_N);
		importCooccurrences(mySQL, dex, RelTypes.CO_S);
	}
	
	private void registerShutdownHook() {
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running example before it's completed)
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	        	dex.close();
	            dexConnector.close();
	        }
	    } );
	}
	
	private void importCooccurrences(Connection mySQL, GraphPool dex, RelTypes relType) {
		String table = relType.toString().toLowerCase();
		Integer count = getMysqlRowCount(mySQL, table);
		System.out.println("Importing " + count + " cooccurrences (" + table + ")");
		Integer step  = 0;
		
	    String query = "SELECT * FROM " + table;
	    
	    /*
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
	    */
	}
	
	private void importWords(Connection mySQL, GraphPool dex) {		
	    String query = "SELECT * FROM words";
	    
	    /*
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
	    */
	}

	@Override
	public String getName() {		
		return "dex";
	}
}
