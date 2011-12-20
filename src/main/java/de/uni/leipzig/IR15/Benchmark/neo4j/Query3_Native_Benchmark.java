package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

import de.uni.leipzig.IR15.Importer.Neo4JImporter;

/**
 * Traverser collects all paths starting at A which have the following pattern
 * A-[CO_S]->B-[CO_S]->C<-[CO_S]-A. The relevant edges are the ones between B
 * and C, they are excluded from the results after the traversal.
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public class Query3_Native_Benchmark extends Neo4jBenchmark {

	@Override
	public void run() {
		for (Relationship e1 : startNode.getRelationships(
				Neo4JImporter.RelTypes.CO_S, Direction.OUTGOING)) {
			for (Relationship e2 : startNode.getRelationships(
					Neo4JImporter.RelTypes.CO_S, Direction.OUTGOING)) {
				if (e1.equals(e2)) {
					continue;
				} else {
					for(Relationship e3 : e1.getEndNode().getRelationships(Neo4JImporter.RelTypes.CO_S, Direction.OUTGOING)) {
						if(e3.getEndNode().equals(e2.getEndNode())) {
							e3.getStartNode().getProperty("w_id"); // w1_id
							e3.getEndNode().getProperty("w_id"); // w2_id
							e3.getProperty("freq"); // frequency
							e3.getProperty("sig"); // significance
						}
					}
				}
			}
		}

	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "neo4j Query 3 (native)";
	}

}
