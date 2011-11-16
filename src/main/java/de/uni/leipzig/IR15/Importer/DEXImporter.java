package de.uni.leipzig.IR15.Importer;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.RelationshipType;

import com.sparsity.dex.gdb.AttributeKind;
import com.sparsity.dex.gdb.DataType;
import com.sparsity.dex.gdb.Database;
import com.sparsity.dex.gdb.Dex;
import com.sparsity.dex.gdb.DexConfig;
import com.sparsity.dex.gdb.Graph;
import com.sparsity.dex.gdb.Session;
import com.sparsity.dex.gdb.Value;

public class DEXImporter extends Importer {
	protected static Logger log = Logger.getLogger(DEXImporter.class);
	
	static enum RelTypes
	{
		CO_S,
		CO_N
	}

	private Dex dexConnector;
	private Database dex;
	private Integer operationsPerTx;

	@Override
	public void setUp() {
		super.setUp("dex");
		
		operationsPerTx = graphConfiguration.getPropertyAsInteger("operations_per_transaction");
		dexConnector = new Dex(new DexConfig());
		
		try {
			dex = dexConnector.create(graphConfiguration.getPropertyAsString("location"), "dex");
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
		super.tearDown();
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
		importWords();
		importCooccurrences(RelTypes.CO_N);
		importCooccurrences(RelTypes.CO_S);
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
	
	private void importCooccurrences(RelTypes relType) {
		String table = relType.toString().toLowerCase();
		Integer count = getMysqlRowCount(mySQL, table);
		log.info(String.format("Importing %d cooccurences from mysql table %s", count, table));
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
	
	private void importWords() {		
	    String query = "SELECT * FROM words";
	    
	    Session session = dex.newSession();
	    
	    session.begin();
	    try {
	    	Statement st = mySQL.createStatement();
	    	st.setFetchSize(Integer.MIN_VALUE);
	    	ResultSet rs = st.executeQuery(query);
	    	
	    	int i = 0;
	    	
	    	Graph graph = session.getGraph();
	    	int wordNodeType = graph.newNodeType("word");
	    	int wordIdAttribute = graph.newAttribute(wordNodeType, "w_id", DataType.Integer, AttributeKind.Indexed);
	    	int wordAttribute = graph.newAttribute(wordNodeType, "word", DataType.String, AttributeKind.Basic);
	    	
	    	while (rs.next()) {
	    		String word 	= rs.getString("word");
	    		Integer word_id = rs.getInt("w_id");
	    		
	    		long node = graph.newNode(wordNodeType);
	    		
	    		graph.setAttribute(node, wordIdAttribute, new Value().setInteger(word_id));
	    		graph.setAttribute(node, wordAttribute, new Value().setString(word));

	    		if(++i % operationsPerTx == 0) {
		        	// commit
		        	session.commit();
		        	System.out.println(".");
		        }        
	    	}
	    	session.commit();	    	
	    	
	    } catch (SQLException ex) {
		      System.err.println(ex.getMessage());
		} finally {
	    	session.close();
	    }
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
}
