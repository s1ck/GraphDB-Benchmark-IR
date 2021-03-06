package de.uni.leipzig.IR15.Benchmark.orientdb;


import java.util.HashSet;
import java.util.Set;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Query 3 selects all sentence co-occurrences of a given word which are
 * co-occurences of themselves.
 *
 * A -> B , A -> C, B -> C - The relationship B -> C is selected
 *
 * In SQL this is the following query:
 *
 * select w1.w1_id,w1.w2_id,w1.freq,w1.sig
 * from co_s w1
 * where w1.w1_id in
 * 		(select w2.w2_id
 * 		 from co_s w2
 * 		 where w2.w1_id=137)
 * and w1.w2_id in
 * 		(select w3.w2_id
 * 		 from co_s w3
 * 		 where w3.w1_id=137);
 *
 * @author Sascha 'peil' Ludwig
 *
 */

public class Query3_LowL_Benchmark extends OrientDBBenchmark{

	/**
	 * Finds all nodes and gets the w1_id, w2_id, sig and freq attributes
	 * for nodes that are neighbors to each other and to a given start node.
	 */
	@Override
	public void run() {
		Set<ODocument> FOVertices = new HashSet<ODocument>();
		
		// get all the outgoing edges
		Set<OIdentifiable> FOoutEdges = orientdb.getOutEdges(startVertex);
		// for all outgoing edges
		for (OIdentifiable FOoutEdgeIter : FOoutEdges) {
			// cast the Edge to a real Edge (because of return-type of getOutEdges)
			ODocument FOoutEdge = (ODocument) FOoutEdgeIter.getRecord();
			// if the edge is of type co_s
			if ( FOoutEdge.field("type").toString().equalsIgnoreCase("co_s")) {
				// get the Vertex at the end of the edge
				ODocument FOoutVertex = orientdb.getInVertex(FOoutEdge);
				FOVertices.add(FOoutVertex);
			}
		}
		
		for(ODocument FOoutVertex : FOVertices  ){
			//and from there all outgoing edges again
			Set<OIdentifiable> SOoutEdges = orientdb.getOutEdges(FOoutVertex);
			// for all second order outgoing edges
			for (OIdentifiable SOoutEdgeIter : SOoutEdges) {
				// cast the Edge to a real Edge (because of return-type of getOutEdges)
				ODocument SOoutEdge = (ODocument) SOoutEdgeIter.getRecord();
				// if type is co_s
				if ( SOoutEdge.field("type").toString().equalsIgnoreCase("co_s") && FOVertices.contains(orientdb.getInVertex(SOoutEdge))) {
					SOoutEdge.field("freq");
					SOoutEdge.field("sig");
					orientdb.getOutVertex(SOoutEdge).field("w_id");
					orientdb.getInVertex(SOoutEdge).field("w_id");
				}
			}
		}
		
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "OrientDB Query3 LowLevel Benchmark";
	}
}