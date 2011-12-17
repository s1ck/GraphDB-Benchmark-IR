package de.uni.leipzig.IR15.Importer;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;

import de.uni.leipzig.IR15.Connectors.MySQLConnector;
import de.uni.leipzig.IR15.Connectors.OrientDBConnector;


public class OrientDBImporter extends Importer {
	
	private static OGraphDatabase orientdb;
	private static enum RelTypes { CO_S, CO_N }
	
	
	public OGraphDatabase getDB() {
		return orientdb;
	}

	
	@Override
	public void setUp() {
		
		super.setUp("orientdb");
		
		// clean up database directory
		// create folder if not existent
		File loc = new File(graphConfiguration.getPropertyAsString("location"));
		if (loc.exists() == false) {
			loc.mkdir();
		}
		reset();
		
		// connect (create) to OrientDB 
		orientdb = OrientDBConnector.getConnection();
		
		// and to MySQL	
		mySQLConnection = MySQLConnector.getConnection();
	}
	
	
	@Override
	public void tearDown() {
		// shutdown the connections
		OrientDBConnector.destroyConnection();
		super.tearDown();
	}
	
	
	public void importData() {
		// transfer the data from mysql to orientdb
		importWords(mySQLConnection, orientdb);
		
		// create an index on the nodes for Word_ID 
		// String q1 = "CREATE INDEX WordIds ON OGRAPHVERTEX (W_ID) UNIQUE";
		// List<ODocument> result = orientdb.query(new OSQLSynchQuery<ODocument>(q1));
		
		importCooccurrences(mySQLConnection, orientdb, RelTypes.CO_N);
		importCooccurrences(mySQLConnection, orientdb, RelTypes.CO_S);
	}
	
	
	private void importWords(Connection mySQL, OGraphDatabase orientdb) {
		String query = "SELECT * FROM words";

		Integer count = getRowCount(mySQL, "words");
		log.info("Importing " + count + " words ");

		try {
			Statement st = mySQL.createStatement();
			st.setFetchSize(Integer.MIN_VALUE);
			ResultSet rs = st.executeQuery(query);
			// v-type
			while (rs.next()) {
				String word = rs.getString("word");
				Integer word_id = rs.getInt("w_id");

				ODocument vertex = orientdb.createVertex();
				vertex.field("w_id", word_id);
				vertex.field("word", word);
				vertex.save(); // make it persistent

				// @TODO: mayber better solution for getting vertices by id later on?!
				orientdb.setRoot(word_id.toString(), vertex);

			}
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}
	}

	private void importCooccurrences(Connection mySQL, OGraphDatabase orientdb,
			RelTypes relType) {
		String table = relType.toString().toLowerCase();
		Integer count = getRowCount(mySQL, table);

		orientdb.createEdgeType(table);
		log.info("Importing " + count + " cooccurrences (" + table + ")");

		String query = "SELECT * FROM " + table;

		try {
			Statement st = mySQL.createStatement();
			st.setFetchSize(Integer.MIN_VALUE);
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				Integer w1_id = rs.getInt("w1_id");
				Integer w2_id = rs.getInt("w2_id");
				Integer sig = rs.getInt("sig");
				Integer freq = rs.getInt("freq");

				// @TODO Dirty, maybe better solution for bigger Graphs
				// but not with queries. One query takes 0.25s to return 1 vertex
				ODocument source = orientdb.getRoot(w1_id.toString());
				ODocument target = orientdb.getRoot(w2_id.toString());
				ODocument edge = orientdb.createEdge(source, target, table); // table
																				// =
																				// co_n
																				// or
																				// co_s
				edge.field("freq", freq);
				edge.field("sig", sig);
				edge.save();
			}

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	
	private Integer getRowCount(Connection sqlConnection, String table) {
		String query = "SELECT COUNT(*) FROM " + table;
		Integer count = null;
		try {
			Statement st = sqlConnection.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				count = Integer.valueOf(rs.getInt("COUNT(*)"));
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return count;
	}

	@Override
	public String getName() {
		return "orientdb";
	}
}
