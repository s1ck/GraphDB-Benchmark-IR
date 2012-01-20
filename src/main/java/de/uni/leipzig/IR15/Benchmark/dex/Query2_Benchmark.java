package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.ObjectsIterator;
import com.sparsity.dex.gdb.Value;

/**
 * Query 2 selects all sentence co-occurrences of sentence co-occurrences (co_s)
 * of a given word.
 *
 * @author Robert 'robbl' Schulze
 *
 */
public class Query2_Benchmark extends DEXBenchmark {

	/**
	 * Fetches all neighbors of neighbors of a given start node.
	 */
	@Override
	public void run() {
		long edge, node_depth_1, node_depth_2;
		Value sigValue, freqValue, word1IDValue, word2IDValue;

		// get all outgoing neighbors to a given start node
		Objects neighbors_depth_1 = graph.neighbors(startNodeID, coSEdgeType,
				EdgesDirection.Outgoing);

		// iterate over all neighbors
		ObjectsIterator iter_depth_1 = neighbors_depth_1.iterator();
		while (iter_depth_1.hasNext()) {
			node_depth_1 = iter_depth_1.next();

			// get all outgoing neighbors (degree = 2) to each neighbor of the
			// start node
			Objects neighbors_depth_2 = graph.neighbors(node_depth_1, coSEdgeType,
					EdgesDirection.Outgoing);

			// iterate over all neighbors with degree 2
			ObjectsIterator iter_depth_2 = neighbors_depth_2.iterator();
			while (iter_depth_2.hasNext()) {
				node_depth_2 = iter_depth_2.next();

				// find the edge between the two nodes
				edge = graph.findEdge(coSEdgeType, node_depth_2, node_depth_1);

				// get the w_id attribute value and cleanup
				graph.getAttribute(node_depth_2, wordIDAttribute,
						word1IDValue = new Value());
				word1IDValue.getInteger();
				word1IDValue.delete();

				// get the w_id attribute value and cleanup
				graph.getAttribute(node_depth_2, wordIDAttribute,
						word2IDValue = new Value());
				word2IDValue.getInteger();
				word2IDValue.delete();

				// get the sig attribute value and cleanup
				graph.getAttribute(edge, coSEdgeSigAttribute,
						sigValue = new Value());
				sigValue.getDouble();
				sigValue.delete();

				// get the freq attribute value and cleanup
				graph.getAttribute(edge, coSEdgeFreqAttribute,
						freqValue = new Value());
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