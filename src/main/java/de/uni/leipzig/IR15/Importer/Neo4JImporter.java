package de.uni.leipzig.IR15.Importer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import de.uni.leipzig.IR15.Connectors.MySQLConnector;

public class Neo4JImporter {

	private static enum RelTypes implements RelationshipType
	{
		CO_S,
		CO_N
	}
	
	private static Index<Node> nodeIndex;

	private static Integer operationsPerTx;
	
	private static Properties mySQLProps;
	
	private static Properties neo4jProps;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// load properties
		neo4jProps = new Properties();
		neo4jProps.load(new FileReader("src/main/resources/neo4j.properties"));
		mySQLProps = new Properties();
		mySQLProps.load(new FileReader("src/main/resources/mysql.properties"));
		
		operationsPerTx = Integer.parseInt(neo4jProps.get("operations_per_transaction").toString());
		
		// connect to mysql
		String database = "jdbc:mysql://" 
		+ mySQLProps.getProperty("host") 
		+ ":" + mySQLProps.getProperty("port") 
		+ "/" + mySQLProps.getProperty("database");
		
		MySQLConnector mySQLConnector = new MySQLConnector(database, 
				mySQLProps.getProperty("username"), 
				mySQLProps.getProperty("password")
				);
		
		Connection mySQL = mySQLConnector.createConnection();
		
		// connect to neo4j and create an index on the nodes
		
		GraphDatabaseService neo4j = new EmbeddedGraphDatabase(neo4jProps.getProperty("location"));
		nodeIndex = neo4j.index().forNodes("words");
			
				
		registerShutdownHook(neo4j);
		
		// transfer the data from mysql to neo4j
		long start = System.currentTimeMillis();
		transferData(neo4j, mySQL);
		long diff = System.currentTimeMillis() - start;
		
		// count
		int n = 0;
		int m = 0;
		for (Node node : neo4j.getAllNodes())
		{
			n++;
			for(Relationship edge : node.getRelationships(Direction.OUTGOING))
			{
				m++;
			}
		}		
		System.out.printf("transfered %d nodes and %d edges. these are %d objects / second\n", n, m, (n+m) / (diff / 1000));
		
		// shutdown the connections
		neo4j.shutdown();
		mySQLConnector.destroyConnection();
	}
	
	private static void transferData(GraphDatabaseService neo4j, Connection mySQL)
	{		
		importWords(mySQL, neo4j);
		importCooccurrences(mySQL, neo4j, RelTypes.CO_N);
		importCooccurrences(mySQL, neo4j, RelTypes.CO_S);
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
	            graphDb.shutdown();
	        }
	    } );
	}
	
	private static void importCooccurrences(Connection mySQL, GraphDatabaseService neo4j, RelationshipType relType) {
		String table = relType.toString().toLowerCase();
		Integer count = getRowCount(mySQL, table);
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
	
	private static void importWords(Connection mySQL, GraphDatabaseService neo4j) {
		Integer count = getRowCount(mySQL, "words");
		System.out.println("Importing " + count + " words");
		Integer step = 0;
		
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
	        
	        step++;	        
	      }
	      tx.success();
	    } catch (SQLException ex) {
	      System.err.println(ex.getMessage());
	    }
	    finally {
	    	tx.finish();
	    }
	}
	
	private static Integer getRowCount(Connection sqlConnection, String table) {
	    String query = "SELECT COUNT(*) FROM " + table;
	    Integer count = null;
	    try {
	      Statement st = sqlConnection.createStatement();
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
