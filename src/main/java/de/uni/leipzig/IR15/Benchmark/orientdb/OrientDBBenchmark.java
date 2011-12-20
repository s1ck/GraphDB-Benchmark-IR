package de.uni.leipzig.IR15.Benchmark.orientdb;


import java.util.List;
import java.util.Set;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.OrientDBConnector;

public abstract class OrientDBBenchmark extends Benchmark {

	protected long startWordID;
	protected OGraphDatabase orientdb;
	protected int maxID;
	protected OSQLSynchQuery<ODocument> prep_query1;
	protected OSQLSynchQuery<ODocument> prep_query2;

	@Override
	public void setUp() {
		orientdb = OrientDBConnector.getConnection();
		// numVertices
		maxID = (int) findMaxWordID();
		
		// select w1.w2_id 
		// from co_s w1 

		// select w1.w2_id
		// from co_s w1
		// where w1.w1_id=137;
		String q1 = "select out.w_id from E where in.w_id = ? and type = 'co_s'";
		prep_query1 = new OSQLSynchQuery<ODocument>(q1);
		
		// select w1.w1_id, w1.w2_id, w1.freq, w1.sig
		// 		from co_s w1
		// 		where w1.w1_id in
		// (select w2.w2_id
		// 		from co_s w2
		// 		where w2.w1_id=137 )
		// Gremlin.compile("_().out('co_s').outE('co_s')");
		//
	}

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
	
	
	// gets the max WordID. Needed for random WordID
	public int findMaxWordID()
	{
		int m = 0;
		int tmp = 0;
		
		// get all Vertices aka words
		Iterable<ODocument> allWords = orientdb.browseVertices();
		// for all words
		for(ODocument word : allWords) {
			tmp = word.field("w_id");
			// check if w_id is bigger than current max
			if (tmp > m)
				m = tmp;
		}
		
		return m;
	}
	
	@Override
	public void tearDown() {
		OrientDBConnector.destroyConnection();
	}


}