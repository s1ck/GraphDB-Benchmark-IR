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

/**
 * Abstract Base Class for all benchmarks running on DEX graph database. It
 * holds a reference to the database, the session and the graph, it also cares
 * about generating random (and existing) node ids.
 * 
 * @author Robert 'robbl' Schulze
 * 
 */
public abstract class DEXBenchmark extends Benchmark {
	protected Database dex;
	protected Session session;
	protected Graph graph;
	protected int startWordID, maxWordID;
	protected long startNodeID;
	protected int wordNodeType;
	protected int wordIDAttribute;
	protected int coNEdgeType, coNEdgeSigAttribute, coNEdgeFreqAttribute;
	protected int coSEdgeType, coSEdgeSigAttribute, coSEdgeFreqAttribute;

	/**
	 * Setup the database connection and find all needed node, edge and
	 * attribute types.
	 */
	@Override
	public void setUp() {
		dex = DEXConnector.getConnection();

		session = dex.newSession();
		graph = session.getGraph();

//		session.begin();

		wordNodeType = graph.findType("word");
		wordIDAttribute = graph.findAttribute(wordNodeType, "w_id");

		coNEdgeType = graph.findType("CO_N");
		coNEdgeSigAttribute = graph.findAttribute(coNEdgeType, "sig");
		coNEdgeFreqAttribute = graph.findAttribute(coNEdgeType, "freq");
		coSEdgeType = graph.findType("CO_S");
		coSEdgeSigAttribute = graph.findAttribute(coSEdgeType, "sig");
		coSEdgeFreqAttribute = graph.findAttribute(coSEdgeType, "freq");

		maxWordID = findMaxWordID();

//		session.commit();
	}

	/**
	 * Get a new random node before each run.
	 */
	@Override
	public void beforeRun() {		
		startNodeID = getRandomNode(20);
	}

	/**
	 * Commit the session after each run.
	 */
	@Override
	public void afterRun() {
//		session.commit();
	}

	/**
	 * Find the maximal word id.
	 * 
	 * @return maximal word id
	 */
	public int findMaxWordID() {
		AttributeStatistics statistic = graph.getAttributeStatistics(
				wordIDAttribute, false);
		return statistic.getMax().getInteger();
	}

	/**
	 * Get a random node with a minimum degree as threshold.
	 * 
	 * @param outDegree
	 * @return random node id
	 */
	public long getRandomNode(int outDegree) {
		long node = 0;
		Objects neighbors;

		while (true) {
			startWordID = r.nextInt(maxWordID);
			node = graph.findObject(wordIDAttribute,
					new Value().setInteger(startWordID));
			if (node != 0) {
				neighbors = graph.neighbors(node, coSEdgeType,
						EdgesDirection.Outgoing);
				if (neighbors.size() >= outDegree) {
					return node;
				}
			}
		}
	}

	/**
	 * Cleanup after benchmarking.
	 */
	@Override
	public void tearDown() {
//		session.commit();
//		session.close();
		DEXConnector.destroyConnection();
	}

	@Override
	@SuppressWarnings("unused")
	public void warmup() {
		for (Integer nType : graph.findNodeTypes()) {
			for (long v : graph.select(nType)) {
			}
		}

		for (Integer eType : graph.findEdgeTypes()) {
			for (long e : graph.select(eType)) {
			}
		}
	}
}