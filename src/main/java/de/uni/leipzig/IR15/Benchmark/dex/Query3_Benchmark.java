package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.ObjectsIterator;
import com.sparsity.dex.gdb.Value;

public class Query3_Benchmark extends DEXBenchmark {

	@Override
	public void run() {
		long edge, node1, node2;

		Value sigValue, freqValue, word1IDValue, word2IDValue;
		int freq, word1ID, word2ID;
		double sig;

		Objects neighbors_1 = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Outgoing);
		Objects neighbors_2;

		ObjectsIterator iter1 = neighbors_1.iterator(), iter2;
		while (iter1.hasNext()) {
			node1 = iter1.nextObject();

			neighbors_2 = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Outgoing);
			iter2 = neighbors_2.iterator();
			while (iter2.hasNext()) {
				node2 = iter2.nextObject();

				// skip self-references
				if (node1 == node2) {
					continue;
				}

				edge = graph.findEdge(coSEdgeType, node1, node2);

				// skip if there is no co_s edge between node1 and node2
				if (edge == 0) {
					continue;
				}

				graph.getAttribute(node1, wordIdAttribute, word1IDValue = new Value());
				word1ID = word1IDValue.getInteger();
				word1IDValue.delete();

				graph.getAttribute(node1, wordIdAttribute, word2IDValue = new Value());
				word2ID = word2IDValue.getInteger();
				word2IDValue.delete();

				graph.getAttribute(edge, coSEdgeSigAttribute, sigValue = new Value());
				sig = sigValue.getDouble();
				sigValue.delete();

				graph.getAttribute(edge, coSEdgeFreqAttribute, freqValue = new Value());
				freq = freqValue.getInteger();
				freqValue.delete();
			}
			iter2.close();
			neighbors_2.close();

		}
		iter1.close();
		neighbors_1.close();
	}

	@Override
	public String getName() {
		return "DEX Query 3";
	}
}