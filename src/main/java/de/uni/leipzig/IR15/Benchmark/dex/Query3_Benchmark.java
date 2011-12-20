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
		ObjectsIterator iter2;

	    Session session = dex.newSession();
	    session.begin();

		Objects neighbors_1 = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Outgoing);
		ObjectsIterator iter1 = neighbors_1.iterator();
		while (iter1.hasNext()) {
			node1 = iter1.nextObject();

			iter2 = neighbors_1.iterator();
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

				graph.getAttribute(edge, coSEdgeSigAttribute, new Value());
				graph.getAttribute(edge, coSEdgeFreqAttribute, new Value());
			}
			iter2.close();
			iter2.delete();
		}
		iter1.close();
		iter1.delete();

    	session.commit();
    	session.close();
	}

	@Override
	public String getName() {
		return "DEX Query 3";
	}
}