package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.Session;

public class Query1_Benchmark extends DEXBenchmark {
	@Override
	public void run() {
	    Session session = dex.newSession();
	    session.begin();

	    Objects neighbours = graph.neighbors(startNodeID, coSEdgeType, EdgesDirection.Any);

		session.commit();
    	session.close();
	}

	@Override
	public String getName() {
		return "DEX Query 1";
	}
}
