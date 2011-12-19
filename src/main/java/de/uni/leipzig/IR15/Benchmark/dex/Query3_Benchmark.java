package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.ObjectsIterator;
import com.sparsity.dex.gdb.Session;
import com.sparsity.dex.gdb.Value;

public class Query3_Benchmark extends DEXBenchmark {

	@Override
	public void run() {
		long edge, node1, node2;
		Value value = new Value();
		ObjectsIterator iter2;

	    Session session = dex.newSession();
	    session.begin();

		Objects neighbors = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Outgoing);
		ObjectsIterator iter1 = neighbors.iterator();
		while (iter1.hasNext()) {
			node1 = iter1.next();

			iter2 = neighbors.iterator();
			while (iter2.hasNext()) {
				node2 = iter2.next();

				// skip self-references
				if (node1 == node2) {
					continue;
				}

				edge = graph.findEdge(coSEdgeType, node1, node2);

				// skip if there is no co_s edge between node1 and node2
				if (edge == 0) {
					continue;
				}

				graph.getAttribute(edge, coSEdgeSigAttribute, new Value());
				graph.getAttribute(edge, coSEdgeFreqAttribute, new Value());
			}
			iter2.close();
		}
		iter1.close();

    	session.commit();
    	session.close();
	}

	@Override
	public String getName() {
		return "DEX Query 3";
	}
}