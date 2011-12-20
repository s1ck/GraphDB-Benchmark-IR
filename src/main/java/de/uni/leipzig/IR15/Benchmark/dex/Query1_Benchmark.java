package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.ObjectsIterator;
import com.sparsity.dex.gdb.Session;
import com.sparsity.dex.gdb.Value;

public class Query1_Benchmark extends DEXBenchmark {
	@Override
	public void run() {
	    Session session = dex.newSession();
	    session.begin();

		long node;

		Value wordIDValue;
		int wordID;

	    ObjectsIterator iter = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Any).iterator();
	    while(iter.hasNext()) {
	    	node = iter.nextObject();

			graph.getAttribute(node, wordIdAttribute, wordIDValue = new Value());
			wordID = wordIDValue.getInteger();
	    }

		session.commit();
    	session.close();
	}

	@Override
	public String getName() {
		return "DEX Query 1";
	}
}
