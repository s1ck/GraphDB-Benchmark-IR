package de.uni.leipzig.IR15.Benchmark.orientdb;


import java.util.Set;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.OrientDBConnector;

/**
 * Abstract Base Class for all benchmarks running on orient graph database. It
 * holds a reference to the database and the word index, it also cares about
 * generating random (and existing) node ids.
 *
 * @author Sascha 'peil' Ludwig
 *
 */
public abstract class OrientDBBenchmark extends Benchmark {

	protected long startWordID;
	protected OGraphDatabase orientdb;
	protected int maxID;
	protected OSQLSynchQuery<ODocument> prep_query1;
	protected OSQLSynchQuery<ODocument> prep_query2;

	/**
	 * Setup the database connection and get the maximum word id.
	 */
	@Override
	public void setUp() {
		orientdb = OrientDBConnector.getConnection();
		maxID = findMaxWordID();
	}

	/**
	 * Find a random start vertex.
	 */
	@Override
	public void beforeRun() {
		ODocument startVertex;
		int runs = 0;
		int numoutEdges = 0;

		do {
			runs++;
			startWordID = r.nextInt(maxID);

			// check if WordID exists and if degree of outgoing edges is big enough
			startVertex = orientdb.getRoot(String.valueOf( startWordID ));
			numoutEdges = 0;
			if (startVertex != null) {
				Set<OIdentifiable> outEdges = orientdb.getOutEdges(startVertex);
				numoutEdges = outEdges.size();
			}
		} while ( runs < 10000 && (startVertex == null || numoutEdges < 20) );
	}

	@Override
	public void afterRun() {}

	/**
	 * Find the maxium word id.
	 * @return
	 */
	// gets the max WordID. Needed for random WordID
	public int findMaxWordID() {
		int m = 0;
		int tmp = 0;

		// get all Vertices aka words
		Iterable<ODocument> allWords = orientdb.browseVertices();
		// for all words
		for(ODocument word : allWords) {
			tmp = word.field("w_id");
			// check if w_id is bigger than current max
			if (tmp > m) {
				m = tmp;
			}
		}

		return m;
	}

	/**
	 * Cleanup after running benchmarks.
	 */
	@Override
	public void tearDown() {
		OrientDBConnector.destroyConnection();
	}
}