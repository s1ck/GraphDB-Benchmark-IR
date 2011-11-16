package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

import de.uni.leipzig.IR15.Benchmark.Benchmark;

public abstract class Neo4jBenchmark extends Benchmark {
	protected GraphDatabaseService neo4j;
	
	public void setNeo4JService(GraphDatabaseService service) {
		this.neo4j = service;
	}
}
