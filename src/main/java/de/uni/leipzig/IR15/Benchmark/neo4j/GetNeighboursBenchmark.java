package de.uni.leipzig.IR15.Benchmark.neo4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

import de.uni.leipzig.IR15.Importer.Neo4JImporter;
import de.uni.leipzig.IR15.Importer.Neo4JImporter.RelTypes;

public class GetNeighboursBenchmark extends Neo4jBenchmark {
		
	private int maxDepth;
	
	private RelTypes relType;	
	
	public GetNeighboursBenchmark(Neo4JImporter importer, int maxDepth,
			RelTypes relType) {
		this.importer = importer;
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
		int currentDepth = 0;
		q.add(startNode);

		Node v;

		while (q.size() > 0 && currentDepth <= maxDepth) {
			currentDepth++;
			v = q.remove();
			for (Relationship e : v.getRelationships(relType, Direction.BOTH)) {				
				// load neighbour node
				q.add(e.getEndNode());
			}				
		}
	}

	@Override
	public String getName() {
		return String.format("neo4j getNeighbours maxDepth = %d", maxDepth);
	}

}
