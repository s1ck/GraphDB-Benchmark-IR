package de.uni.leipzig.IR15.Importer;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.impl.ODocument;

import de.uni.leipzig.IR15.Connectors.MySQLConnector;
import de.uni.leipzig.IR15.Connectors.OrientDBConnector;

/**
 * Importer to load all data from mysql to orientdb database.
 *
 * @author Sascha 'peil' Ludwig
 *
 */
public class OrientDBImporter extends Importer {

	private static OGraphDatabase orientdb;
	private static enum RelTypes { CO_S, CO_N }


	public OGraphDatabase getDB() {
		return orientdb;
	}

	/**
	 * Setup the importer, reset and get a connection to the database.
	 */
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

		//define schema and index
		OSchema dbschema = orientdb.getMetadata().getSchema();

		OClass cWord = orientdb.createVertexType("WORD");
		cWord.createProperty("w_id", OType.INTEGER);
		cWord.createProperty("word", OType.STRING);
		cWord.createIndex("word_id_Index", OClass.INDEX_TYPE.UNIQUE, "w_id");

		OClass cco_s = orientdb.createEdgeType("CO_S");
		cco_s.createProperty("freq", OType.INTEGER);
		cco_s.createProperty("sig", OType.DOUBLE);
		cco_s.createProperty("type", OType.STRING);

		OClass cco_n = orientdb.createEdgeType("CO_N");
		cco_n.createProperty("freq", OType.INTEGER);
		cco_n.createProperty("sig", OType.DOUBLE);
		cco_n.createProperty("type", OType.STRING);

		dbschema.save();
	}

	/**
	 * Destroy database connection after running importer.
	 *
	 * @see de.uni.leipzig.IR15.Importer.Importer#tearDown()
	 */
	@Override
	public void tearDown() {
		// shutdown the connections
		OrientDBConnector.destroyConnection();
		super.tearDown();
	}

	/**
	 * Import all data.
	 */
	@Override
	public void importData() {
		// transfer the data from mysql to orientdb
		importWords(mySQLConnection, orientdb);

		// create an index on the nodes for Word_ID
		// String q1 = "CREATE INDEX WordIds ON OGRAPHVERTEX (W_ID) UNIQUE";
		// List<ODocument> result = orientdb.query(new OSQLSynchQuery<ODocument>(q1));

		importCooccurrences(mySQLConnection, orientdb, RelTypes.CO_N);
		importCooccurrences(mySQLConnection, orientdb, RelTypes.CO_S);
	}

	/**
	 * Import all words.
	 *
	 * @param mySQL
	 * @param orientdb
	 */
	private void importWords(Connection mySQL, OGraphDatabase orientdb) {
		String query = "SELECT * FROM words";

		Integer count = getRowCount(mySQL, "words"), step = 0;
		log.info("Importing " + count + " words ");

		try {
			Statement st = mySQL.createStatement();
			st.setFetchSize(Integer.MIN_VALUE);
			ResultSet rs = st.executeQuery(query);
			// v-type

			ODocument vertex;
			String word;
			Integer word_id;

			while (rs.next()) {
				if ((++step % 10000) == 0) {
					log.info(step + "/" + count);
				}

				word = rs.getString("word");
				word_id = rs.getInt("w_id");

				vertex = orientdb.createVertex("word");

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

	/**
	 * Import all cooccurrences by <relType>.
	 *
	 * @param mySQL
	 * @param orientdb
	 * @param relType
	 */
	private void importCooccurrences(Connection mySQL, OGraphDatabase orientdb, RelTypes relType) {
		String table = relType.toString().toLowerCase();
		Integer count = getRowCount(mySQL, table), step = 0;

		log.info("Importing " + count + " cooccurrences (" + table + ")");

		String query = "SELECT * FROM " + table;

		try {
			Statement st = mySQL.createStatement();
			st.setFetchSize(Integer.MIN_VALUE);
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				if ((++step % 10000) == 0) {
					log.info(step + "/" + count);
				}

				Integer w1_id = rs.getInt("w1_id");
				Integer w2_id = rs.getInt("w2_id");
				Double sig = rs.getDouble("sig");
				Integer freq = rs.getInt("freq");

				// @TODO Dirty, maybe better solution for bigger Graphs
				// but not with queries. One query takes 0.25s to return 1 vertex
				// maybe lowlevel extraction by id
				ODocument source = orientdb.getRoot(w1_id.toString());
				ODocument target = orientdb.getRoot(w2_id.toString());
				// e_type.getName() = co_n or co_s
				ODocument edge = orientdb.createEdge(source, target, table.toUpperCase());

				edge.field("freq", freq);
				edge.field("sig", sig);
				edge.field("type", table);
				edge.save();  // make it persistent
			}

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * Get mysql row count.
	 *
	 * @param sqlConnection
	 * @param table
	 * @return
	 */
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

	/**
	 * Get the name of the importer.
	 */
	@Override
	public String getName() {
		return "orientdb";
	}
}
