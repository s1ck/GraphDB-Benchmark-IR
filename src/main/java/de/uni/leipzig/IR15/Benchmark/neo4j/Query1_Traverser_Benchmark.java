package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.kernel.Traversal;

import de.uni.leipzig.IR15.Importer.Neo4JImporter;

/**
 * Query 1 Traverser collects all nodes which are directly connected to the
 * start node via an outgoing CO_S relationship.
 *
 * A-[CO_S]->B
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public class Query1_Traverser_Benchmark extends TraverserBenchmark {

	@Override
	public void beforeRun() {
		super.beforeRun();
		td = Traversal.description().breadthFirst()
				.relationships(Neo4JImporter.RelTypes.CO_S, Direction.OUTGOING)
				.evaluator(new Evaluator() {

					@Override
					public Evaluation evaluate(Path path) {
						if (path.length() == 1) {
							return Evaluation.INCLUDE_AND_PRUNE;
						} else {
							return Evaluation.EXCLUDE_AND_CONTINUE;
						}

					}
				});
	}

	@Override
	public void run() {
		for (Path p : td.traverse(startNode)) {
			p.endNode().getProperty("w_id"); // w2_id
		}
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "neo4j Query 1 (traverser)";
	}

}
