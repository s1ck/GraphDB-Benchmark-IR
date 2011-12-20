package de.uni.leipzig.IR15.Benchmark.orientdb;

import java.util.List;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;


import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.OrientDBConnector;

public abstract class OrientDBBenchmark extends Benchmark {
	
	protected long startWordID;
	protected OGraphDatabase orientdb;
	protected int numVertices;
	protected OSQLSynchQuery<ODocument> prep_query1;
	protected OSQLSynchQuery<ODocument> prep_query2;
	
	@Override
	public void setUp() {
		orientdb = OrientDBConnector.getConnection();
		// numVertices
		numVertices = (int) orientdb.countVertexes();
		
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
		String q2 = "SELECT in.w_id, out.w_id, freq, sig FROM E WHERE type = 'co_s' AND in.w_id IN [select out.w_id from E where in.w_id = ? and type = 'co_s']";
		prep_query2 = new OSQLSynchQuery<ODocument>(q2);
	}
	
	@Override
	public void beforeRun() {
		ODocument iVertex;
		do {
			startWordID = r.nextInt(numVertices);
			// check if WordID exists
			iVertex = orientdb.getRoot(String.valueOf( startWordID ));
		} while (iVertex == null);
	
		startWordID = r.nextInt(numVertices);
	}	

	@Override
	public void tearDown() {
		OrientDBConnector.destroyConnection();
	}	
	
	
}