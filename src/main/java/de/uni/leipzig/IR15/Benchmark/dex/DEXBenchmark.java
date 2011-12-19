package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.Database;
import com.sparsity.dex.gdb.Graph;
import com.sparsity.dex.gdb.Session;
import com.sparsity.dex.gdb.Value;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.DEXConnector;

public abstract class DEXBenchmark extends Benchmark {
	protected Database dex;
	protected Session session;
	protected Graph graph;
	protected int startWordID;
	protected long startNode;
	protected int wordNodeType;
	protected int wordIdAttribute;
	protected int coNEdgeType, coNEdgeSigAttribute, coNEdgeFreqAttribute;
	protected int coSEdgeType, coSEdgeSigAttribute, coSEdgeFreqAttribute;

	@Override
	public void setUp() {
		dex = DEXConnector.getConnection();
		session = dex.newSession();
		session.begin();
		graph = session.getGraph();

		wordNodeType = graph.findType("word");
    	wordIdAttribute = graph.findAttribute(wordNodeType, "w_id");

    	coNEdgeType = graph.findType("CO_N");
    	coNEdgeSigAttribute = graph.findAttribute(coNEdgeType, "sig");
    	coNEdgeFreqAttribute = graph.findAttribute(coNEdgeType, "freq");
    	coSEdgeType = graph.findType("CO_S");
    	coSEdgeSigAttribute = graph.findAttribute(coSEdgeType, "sig");
    	coSEdgeFreqAttribute = graph.findAttribute(coSEdgeType, "freq");
	}

	@Override
	public void beforeRun() {
		startWordID = 137;
		startNode = graph.findObject(wordIdAttribute,
				new Value().setInteger(startWordID));
	}

	@Override
	public void tearDown() {
		DEXConnector.destroyConnection();
	}
}