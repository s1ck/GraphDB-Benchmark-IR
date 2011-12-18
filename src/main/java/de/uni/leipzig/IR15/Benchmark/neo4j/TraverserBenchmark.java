package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.graphdb.traversal.TraversalDescription;

/**
 * Abstract Base Class for all Cypher-based benchmarks.
 * 
 * @author Martin 's1ck' Junghanns
 *
 */
public abstract class TraverserBenchmark extends Neo4jBenchmark {

	protected TraversalDescription td;
}
