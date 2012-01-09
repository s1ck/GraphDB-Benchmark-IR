package de.uni.leipzig.IR15.Benchmark.orientdb;


import java.util.Set;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Query 1 selects all sentence co-occurrences (co_s) of a given word.
 *
 * @author sascha
 *
 */
public class Query1_LowL_Benchmark extends OrientDBBenchmark{

	@Override
	public void run() {
		ODocument FOoutEdge;
		
		// get all the outgoing edges
		Set<OIdentifiable> FOoutEdges = orientdb.getOutEdges(startVertex);
		
		// for all outgoing edges
		for (OIdentifiable FOoutEdgeIter : FOoutEdges) {
			// cast the Edge to a real Edge (because of return-type of getOutEdges)
			FOoutEdge = (ODocument) FOoutEdgeIter.getRecord();
			// if the edge is of type co_s
			if ( FOoutEdge.field("type").toString().equalsIgnoreCase("co_s")) {
				// get w_id of the second vertex
				orientdb.getInVertex(FOoutEdge).field("w_id");
			}
		}
		
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "OrientDB Query1 LowLevel Benchmark";
	}
}