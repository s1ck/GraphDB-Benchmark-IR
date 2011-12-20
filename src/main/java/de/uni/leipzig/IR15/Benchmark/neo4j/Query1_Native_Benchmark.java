package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

import de.uni.leipzig.IR15.Importer.Neo4JImporter;

/**
 * Query 1 selects the wordID of all the sentence co-occurrences of a given
 * word.
 *
 * In SQL this is the following query:
 *
 * select w1.w2_id from co_s w1 where w1.w1_id=137;
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public class Query1_Native_Benchmark extends Neo4jBenchmark {

	/**
	 * Iterates over all neighbors to a given start node and gets their w_id.
	 */
	@Override
	public void run() {
		for (Relationship e : startNode.getRelationships(
				Neo4JImporter.RelTypes.CO_S, Direction.OUTGOING)) {
			e.getEndNode().getId(); // w2_id
		}
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "neo4j Query 1 (native)";
	}
}