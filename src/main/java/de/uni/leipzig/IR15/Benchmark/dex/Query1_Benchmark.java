package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
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

	    ObjectsIterator iter = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Any).iterator();
	    while(iter.hasNext()) {
	    	node = iter.nextObject();

			graph.getAttribute(node, wordIDAttribute, wordIDValue = new Value());
			wordIDValue.getInteger();
	    }
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "DEX Query 1";
	}
}
