package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.AttributeStatistics;
import com.sparsity.dex.gdb.Database;
import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Graph;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.Session;
import com.sparsity.dex.gdb.Value;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.DEXConnector;

public abstract class DEXBenchmark extends Benchmark {
	protected Database dex;
	protected Session session;
	protected Graph graph;
	protected int startWordID, maxWordID;
	protected long startNodeID;
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

    	maxWordID = findMaxWordID();
	}

	@Override
	public void beforeRun() {
	    Session session = dex.newSession();
	    session.begin();

	    startNodeID = getRandomNode(2);

	    session.commit();
    	session.close();
	}

	public int findMaxWordID() {
		AttributeStatistics statistic = graph.getAttributeStatistics(wordIdAttribute, false);
		return statistic.getMax().getInteger();
	}

	public long getRandomNode(int outDegree) {
		long node = 0;
		Objects neighbors;

		while (true) {
			node = graph.findObject(wordIdAttribute, new Value().setInteger(137));
			if (node != 0) {
				neighbors = graph.neighbors(node, coSEdgeType, EdgesDirection.Outgoing);
				if (neighbors.size() >= outDegree) {
					return node;
				}
			}
		}
	}

	@Override
	public void tearDown() {
		DEXConnector.destroyConnection();
	}
}