package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Session;

public class Query1_Benchmark extends DEXBenchmark {
	@Override
	public void run() {
	    Session session = dex.newSession();
	    session.begin();

	    long neighbours_count = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Any).count();

		session.commit();
    	session.close();
	}

	@Override
	public String getName() {
		return "DEX Query 1";
	}
}
