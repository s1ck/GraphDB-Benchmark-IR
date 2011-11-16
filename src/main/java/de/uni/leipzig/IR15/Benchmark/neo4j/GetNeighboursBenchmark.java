package de.uni.leipzig.IR15.Benchmark.neo4j;

import java.util.LinkedList;
import java.util.Queue;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

import de.uni.leipzig.IR15.Importer.Neo4JImporter;
import de.uni.leipzig.IR15.Importer.Neo4JImporter.RelTypes;

public class GetNeighboursBenchmark extends Neo4jBenchmark {
	
	private Index<Node> index;
	private int maxDepth;
	private RelTypes relType;
	private int startWordID; 
	private Node startNode;

	public GetNeighboursBenchmark(Neo4JImporter importer, int maxDepth,
			RelTypes relType,
			int startWID) {
		this.importer = importer;
		this.maxDepth = maxDepth;
		this.relType = relType;
		this.startWordID = startWID;
	}
	
	@Override
	public void setUp() {
		// open connection
		importer.setUp();
		// read data
		importer.importData();
		neo4j = (GraphDatabaseService) importer.getDatabaseInstance();
		index = neo4j.index().forNodes("words");	
		startNode = index.get("w_id", startWordID).getSingle();
	}

	@Override
	public void run() {	
		doBFS(startNode);		
	}
	
	@Override
	public void tearDown() {
		// close connection
		importer.tearDown();

		// remove data
		importer.reset();
	}

	@Override 
	public void reset() {
		
	}
	
	private void doBFS(Node startNode) {
		Queue<Node> q = new LinkedList<Node>();
		int currentDepth = 0;
		q.add(startNode);

		Node v;

		while (q.size() > 0 && currentDepth <= maxDepth) {
			currentDepth++;
			v = q.remove();
			for (Relationship e : v.getRelationships(relType, Direction.OUTGOING)) {
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
