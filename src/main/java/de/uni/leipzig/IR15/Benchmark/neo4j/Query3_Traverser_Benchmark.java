package de.uni.leipzig.IR15.Benchmark.neo4j;

import java.util.ArrayList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

import de.uni.leipzig.IR15.Importer.Neo4JImporter;

/**
 * Traverser collects all paths starting at A which have the following pattern
 * A-[CO_S]->B-[CO_S]->C<-[CO_S]-A. The relevant edges are the ones between B
 * and C, they are excluded from the results after the traversal.
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public class Query3_Traverser_Benchmark extends TraverserBenchmark {

	@Override
	public void beforeRun() {
		super.beforeRun();
		// define path pattern
		final ArrayList<Direction> orderedPathContext = new ArrayList<Direction>();
		orderedPathContext.add(Direction.OUTGOING);
		orderedPathContext.add(Direction.OUTGOING);
		orderedPathContext.add(Direction.INCOMING);

		td = Traversal.description().breadthFirst()
				.relationships(Neo4JImporter.RelTypes.CO_S, Direction.BOTH)
				.uniqueness(Uniqueness.NONE).evaluator(new Evaluator() {

					@Override
					public Evaluation evaluate(Path path) {

						if (path.length() == 0) {
							return Evaluation.EXCLUDE_AND_CONTINUE;
						}

						Direction expectedDirection = orderedPathContext
								.get(path.length() - 1);
						Relationship edge = path.lastRelationship();

						boolean isExpectedDirection = (expectedDirection
								.equals(Direction.OUTGOING)) ? (edge
								.getEndNode().equals(path.endNode())) : (edge
								.getStartNode().equals(path.endNode()));

						boolean isEndOfPath = path.endNode().equals(startNode);

						boolean included = path.length() == orderedPathContext
								.size() && isExpectedDirection && isEndOfPath;
						boolean continued = path.length() < orderedPathContext
								.size() && isExpectedDirection;

						return Evaluation.of(included, continued);
					}
				});
	}

	@Override
	public void run() {
		int i;
		/**
		 * If there are any paths that match the traverser pattern they look
		 * like A -> B -> C <- A and we are interested in the edge (B,C).
		 */
		for (Path p : td.traverse(startNode)) {
			i = 0;
			for (Relationship e : p.relationships()) {
				if (i++ == 1) {
					e.getStartNode().getProperty("w_id"); // w1_id
					e.getEndNode().getProperty("w_id"); // w2_id
					e.getProperty("freq"); // frequency
					e.getProperty("sig"); // siginificance
				}
			}
		}
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "neo4j Query 3 (traverser)";
	}
}