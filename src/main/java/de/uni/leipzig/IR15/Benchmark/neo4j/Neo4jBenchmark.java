package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Importer.Neo4JImporter;

public abstract class Neo4jBenchmark extends Benchmark {
	protected Neo4JImporter importer;
	protected GraphDatabaseService neo4j;
}
