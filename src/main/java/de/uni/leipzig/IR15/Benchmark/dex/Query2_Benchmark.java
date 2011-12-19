package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.ObjectsIterator;
import com.sparsity.dex.gdb.Session;
import com.sparsity.dex.gdb.Value;

public class Query2_Benchmark extends DEXBenchmark {

	@Override
	public void run() {
		long edge, node_depth_1, node_depth_2;
		Value value = new Value();

		Objects neighbors_depth_2;
		ObjectsIterator iter_depth_2;

	    Session session = dex.newSession();
	    session.begin();

		Objects neighbors_depth_1 = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Outgoing);
		ObjectsIterator iter_depth_1 = neighbors_depth_1.iterator();

		while (iter_depth_1.hasNext()) {
			node_depth_1 = iter_depth_1.next();

			neighbors_depth_2 = graph.neighbors(node_depth_1, coSEdgeType, EdgesDirection.Outgoing);
			iter_depth_2 = neighbors_depth_2.iterator();

			while (iter_depth_2.hasNext()) {
				node_depth_2 = iter_depth_2.next();
				edge = graph.findEdge(coSEdgeType, node_depth_2, node_depth_1);

				graph.getAttribute(edge, coSEdgeSigAttribute, value);
				graph.getAttribute(edge, coSEdgeFreqAttribute, value);
			}
			iter_depth_2.close();
			iter_depth_2.delete();
		}
		iter_depth_1.close();
		iter_depth_1.delete();

    	session.commit();
    	session.close();
	}

	@Override
	public String getName() {
		return "DEX Query 2";
	}

}
