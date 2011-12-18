package de.uni.leipzig.IR15.Benchmark.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

import de.uni.leipzig.IR15.Importer.Neo4JImporter;

/**
 * Query 2 Traverser collects all nodes with are 2 hops away from the start node and
 * connected via outgoing CO_S relationships between start and end node.
 * 
 * A-[CO_S]->B-[CO_S]->C
 * 
 * @author Martin 's1ck' Junghanns
 * 
 */
public class Query2_Traverser_Benchmark extends TraverserBenchmark {

	@Override
	public void beforeRun() {
		super.beforeRun();
		td = Traversal.description()
				.uniqueness(Uniqueness.RELATIONSHIP_PATH)
				// RELATIONSHIP_PATH = only one relationship-wise unique path to
				// each end node
				.relationships(Neo4JImporter.RelTypes.CO_S, Direction.OUTGOING)
				.breadthFirst().evaluator(new Evaluator() {
					@Override
					public Evaluation evaluate(Path path) {
						if (path.length() == 2) {
							return Evaluation.INCLUDE_AND_PRUNE;
						} else {
							return Evaluation.EXCLUDE_AND_CONTINUE;
						}
					}
				});
	}

	@Override
	public void run() {

		List<Relationship> relevantEdges = new ArrayList<Relationship>();
		for (Path p : td.traverse(startNode)) {
			// the last edge in the path is the one we are interested in
			relevantEdges.add(p.lastRelationship());
			// System.out.println(String.format(
			// "(%s)-->(%s) with sig: %.2f and freq: %d", e.getStartNode()
			// .getProperty("w_id"),
			// e.getEndNode().getProperty("w_id"), e.getProperty("sig"), e
			// .getProperty("freq")));
		}
	}

	@Override
	public String getName() {
		return "neo4j Query 2 (traverser)";
	}

}
