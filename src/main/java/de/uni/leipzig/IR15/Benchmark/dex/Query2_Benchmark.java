package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.ObjectsIterator;
import com.sparsity.dex.gdb.Value;

/**
 * Query 2 selects all sentence co-occurrences of sentence co-occurrences (co_s)
 * of a given word.
 *
 * @author robbl
 *
 */
public class Query2_Benchmark extends DEXBenchmark {

	/**
	 * Fetches all neighbors with degree equals 2 to a given start node and
	 * gets their w_id, sig and freq.
	 */
	@Override
	public void run() {
		long edge, node_depth_1, node_depth_2;

		Value sigValue, freqValue, wordIDValue;

		Objects neighbors_depth_2;
		ObjectsIterator iter_depth_2;

		Objects neighbors_depth_1 = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Outgoing);
		ObjectsIterator iter_depth_1 = neighbors_depth_1.iterator();

		while (iter_depth_1.hasNext()) {
			node_depth_1 = iter_depth_1.nextObject();

			neighbors_depth_2 = graph.neighbors(node_depth_1, coSEdgeType, EdgesDirection.Outgoing);
			iter_depth_2 = neighbors_depth_2.iterator();

			while (iter_depth_2.hasNext()) {
				node_depth_2 = iter_depth_2.nextObject();
				edge = graph.findEdge(coSEdgeType, node_depth_2, node_depth_1);

				graph.getAttribute(node_depth_2, wordIDAttribute, wordIDValue = new Value());
				wordIDValue.getInteger();
				wordIDValue.delete();

				graph.getAttribute(edge, coSEdgeSigAttribute, sigValue = new Value());
				sigValue.getDouble();
				sigValue.delete();

				graph.getAttribute(edge, coSEdgeFreqAttribute, freqValue = new Value());
				freqValue.getInteger();
				freqValue.delete();
			}
			iter_depth_2.close();
			neighbors_depth_2.close();
		}
		iter_depth_1.close();
		neighbors_depth_1.close();
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "DEX Query 2";
	}

}
