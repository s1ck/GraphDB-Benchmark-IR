package de.uni.leipzig.IR15.Benchmark.orientdb;


import java.util.Set;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Query 2 selects all sentence co-occurrences of sentence co-occurrences (co_s)
 * of a given word.
 *
 * @author Sascha 'peil' Ludwig
 *
 */
public class Query2_LowL_Benchmark extends OrientDBBenchmark{

	/**
	 * Fetches all neighbors of neighbors of a given start node.
	 */
	@Override
	public void run() {
		// get all the outgoing edges
		Set<OIdentifiable> FOoutEdges = orientdb.getOutEdges(startVertex);
		
		// for all outgoing edges
		for (OIdentifiable FOoutEdgeIter : FOoutEdges) {
			// cast the Edge to a real Edge (because of return-type of getOutEdges)
			ODocument FOoutEdge = (ODocument) FOoutEdgeIter.getRecord();
			
			// if the edge is of type co_s
			if ( FOoutEdge.field("type").toString().equalsIgnoreCase("co_s") ) {
				// get the Vertex at the end of the edge
				ODocument FOoutVertex = orientdb.getInVertex(FOoutEdge);

				// and from there all outgoing edges again
				Set<OIdentifiable> SOoutEdges = orientdb.getOutEdges(FOoutVertex);

				// for all second order outgoing edges
				for (OIdentifiable SOoutEdgeIter : SOoutEdges) {
					// cast the Edge to a real Edge (because of return-type of getOutEdges)
					ODocument SOoutEdge = (ODocument) SOoutEdgeIter.getRecord();
					
					// if type is co_s
					if ( SOoutEdge.field("type").toString().equalsIgnoreCase("co_s") ) {
						SOoutEdge.field("freq");
						SOoutEdge.field("sig");
						orientdb.getOutVertex(SOoutEdge).field("w_id");
						orientdb.getInVertex(SOoutEdge).field("w_id");
					}
				}
			}
		}
		
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "OrientDB Query2 LowLevel Benchmark";
	}
}