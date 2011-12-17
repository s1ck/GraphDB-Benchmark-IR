package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.tooling.GlobalGraphOperations;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.Neo4JConnector;

public abstract class Neo4jBenchmark extends Benchmark {

	protected Index<Node> index;

	protected GraphDatabaseService neo4j;

	protected ExecutionEngine engine;

	protected Node startNode;
	/*
	 * number of nodes
	 */
	protected int n;

	@Override
	@SuppressWarnings("unused")
	public void setUp() {
		neo4j = Neo4JConnector.getConnection();
		engine = new ExecutionEngine(neo4j);
		// get index
		index = neo4j.index().forNodes("words");
		// neo4j always has one node, so we start at -1 to get the exact count.
		// This is important for the Random instance
		n = -1;

		for (Node v : GlobalGraphOperations.at(neo4j).getAllNodes()) {
			n++;
		}		
	}

	@Override
	public void beforeRun() {
		startNode = getRandomNode();
	}

	@Override
	public void tearDown() {
		Neo4JConnector.destroyConnection();
	}

	/**
	 * creates random id until a matching db entity is found
	 * 
	 * @return
	 */
	protected Node getRandomNode() {
		Node v = null;
		int id = 0;

		while (v == null) {
			id = r.nextInt(n);
			v = index.get("w_id", id).getSingle();
		}
		return v;
	}
}
