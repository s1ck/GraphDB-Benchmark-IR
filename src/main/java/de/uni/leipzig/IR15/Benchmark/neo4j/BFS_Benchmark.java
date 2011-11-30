package de.uni.leipzig.IR15.Benchmark.neo4j;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import de.uni.leipzig.IR15.Importer.Neo4JImporter.RelTypes;

/**
 * This Benchmark selects all neighbours up to a given depth of a word using the
 * sentence co-occurrence relation. The query itself is implemented using the
 * native neo4J API.
 * 
 * @author s1ck
 *
 */
public class BFS_Benchmark extends Neo4jBenchmark {

	private int maxDepth;

	private RelTypes relType;

	public BFS_Benchmark(int maxDepth,
			RelTypes relType) {		
		this.maxDepth = maxDepth;
		this.relType = relType;
	}

	@Override
	public void run() {
		doBFS(startNode);
	}

	@Override
	public void reset() {
		startNode = getRandomNode();
	}

	private void doBFS(Node startNode) {
		Queue<Node> q = new LinkedList<Node>();
		Set<Long> visited = new HashSet<Long>();
		int currentDepth = 0;

		q.add(startNode);
		visited.add(startNode.getId());

		Node v;

		while (q.size() > 0 && currentDepth <= maxDepth) {
			currentDepth++;
			v = q.remove();
			for (Relationship e : v.getRelationships(relType, Direction.BOTH)) {
				// load neighbour node
				if (!visited.contains(e.getEndNode().getId())) {
					q.add(e.getEndNode());
					visited.add(e.getEndNode().getId());
				}
			}
		}
	}

	@Override
	public String getName() {
		return String.format("neo4j getNeighbours maxDepth = %d", maxDepth);
	}

}
