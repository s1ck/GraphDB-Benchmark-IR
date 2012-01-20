package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.ObjectsIterator;
import com.sparsity.dex.gdb.Value;

/**
 * Query 1 selects all sentence co-occurrences (co_s) of a given word.
 *
 * @author robbl
 *
 */
public class Query1_Benchmark extends DEXBenchmark {

	/**
	 * Iterates over all neighbors to a given start node and gets their w_id.
	 */
	@Override
	public void run() {
		long node;

		Value wordIDValue;

		// iterate over all neighbors of a given start node
		Objects neighbours = graph.neighbors(startNodeID, coSEdgeType,
				EdgesDirection.Any);
		ObjectsIterator iter = neighbours.iterator();
		while (iter.hasNext()) {
			node = iter.next();

			// get the w_id attribute value and cleanup
			graph.getAttribute(node, wordIDAttribute, wordIDValue = new Value());
			wordIDValue.getInteger();
			wordIDValue.delete();
		}
		iter.close();
		neighbours.close();
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "DEX Query 1";
	}
}