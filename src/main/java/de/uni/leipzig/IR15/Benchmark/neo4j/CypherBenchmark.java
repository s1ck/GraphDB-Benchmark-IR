package de.uni.leipzig.IR15.Benchmark.neo4j;


/**
 * Abstract Base Class for all Cypher-based benchmarks. It just executes the
 * query and counts the rows in the result to avoid paging effects.
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public abstract class CypherBenchmark extends Neo4jBenchmark {
	protected static String CYPHER_QUERY;

	/**
	 * Execute the prepared statement.
	 */
	@Override
	public void run() {
		engine.execute(CYPHER_QUERY).size();
	}
}