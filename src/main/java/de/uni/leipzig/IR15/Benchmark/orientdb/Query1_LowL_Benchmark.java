package de.uni.leipzig.IR15.Benchmark.orientdb;


import java.util.Set;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class Query1_LowL_Benchmark extends OrientDBBenchmark{

	@Override
	public void run() {
		// get the vertex with the given word_id
		ODocument iVertex = orientdb.getRoot(String.valueOf(startWordID));

		// get all the outgoing edges
		Set<OIdentifiable> FOoutEdges = orientdb.getOutEdges(iVertex);

		ODocument FOoutEdge;

		// for all outgoing edges
		for (OIdentifiable FOoutEdgeIter : FOoutEdges) {
			// cast the Edge to a real Edge (because of return-type of getOutEdges)
			FOoutEdge = orientdb.load(FOoutEdgeIter.getIdentity());

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