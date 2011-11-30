package de.uni.leipzig.IR15.Benchmark.neo4j;

import java.util.Random;

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
		n = 0;		
		
		for (Node v : GlobalGraphOperations.at(neo4j).getAllNodes()) {
			n++;
		}		
		startNode = getRandomNode();				
	}
	
	@Override
	public void tearDown() {
		Neo4JConnector.destroyConnection();		
	}
	
	protected Node getRandomNode() {
		Node v = null;
		int id;
		Random r = new Random();
		
		while(v == null) {
			id = r.nextInt(n);
			v = neo4j.getNodeById(id);
		}
		
		return v;
	}
}
