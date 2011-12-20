package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.cypher.commands.Query;

/**
 * Abstract Base Class for all Cypher-based benchmarks. It just executes the
 * query and counts the rows in the result to avoid paging effects.
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public abstract class CypherBenchmark extends Neo4jBenchmark {

	protected static Query CYPHER_QUERY;

	@Override
	public void run() {
		engine.execute(CYPHER_QUERY).size();
	}
}
