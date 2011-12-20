package de.uni.leipzig.IR15.Benchmark.orientdb;


import java.util.Set;

import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class Query1_LowL_Benchmark extends OrientDBBenchmark{

	@Override
	public void run() {

		// get the vertex with the given word_id
		ODocument iVertex = orientdb.getRoot(String.valueOf( startWordID ));
		// test if not null. should always be true, because the before_run methods tests on existence
		if (iVertex != null) {

			// get all the outgoing edges
			Set<OIdentifiable> FOoutEdges = orientdb.getOutEdges(iVertex);
			// for all outgoing edges
			for (OIdentifiable FOoutEdgeIter : FOoutEdges)
			{
				// cast the Edge to a real Edge (because of return-type of getOutEdges)
				ODocument FOoutEdge = orientdb.load(FOoutEdgeIter.getIdentity());
				// if the edge is of type co_s
				if ( FOoutEdge.field("type").toString().equalsIgnoreCase("co_s"))
				{
					int w2_id = orientdb.getInVertex(FOoutEdge).field("w_id");
				}
			}
		}
	}

	@Override
	public String getName() {
		return "OrientDB Query1 LowLevel Benchmark";
	}

}
