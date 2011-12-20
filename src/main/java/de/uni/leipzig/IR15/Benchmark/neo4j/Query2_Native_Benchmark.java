package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

import de.uni.leipzig.IR15.Importer.Neo4JImporter;

/**
 * Query 2 selects the wordID of all sentence co-occurrences of co-occurrences
 * of a given word. It also selects the significance and the frequency of the
 * 2nd degree co-occurrences.
 *
 * In SQL this is the following query:
 *
 * select w1.w1_id,w1.w2_id,w1.freq,w1.sig from co_s w1 where w1.w1_id in
 * (select w2.w2_id from co_s w2 where w2.w1_id=137)
 *
 * @author s1ck
 *
 */
public class Query2_Native_Benchmark extends Neo4jBenchmark {

	@Override
	public void run() {
		for(Relationship e1 : startNode.getRelationships(Neo4JImporter.RelTypes.CO_S, Direction.OUTGOING)) {
			for(Relationship e2 : e1.getEndNode().getRelationships(Neo4JImporter.RelTypes.CO_S, Direction.OUTGOING)) {
				e2.getStartNode().getProperty("w_id"); // w1_id
				e2.getEndNode().getProperty("w_id"); // w2_id
				e2.getProperty("freq"); // frequency
				e2.getProperty("sig"); // significance
			}
		}
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "neo4j Query 2 (native)";
	}

}
