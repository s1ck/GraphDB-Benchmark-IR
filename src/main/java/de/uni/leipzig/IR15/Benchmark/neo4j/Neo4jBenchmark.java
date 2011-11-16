package de.uni.leipzig.IR15.Benchmark.neo4j;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

import scala.Int;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Importer.Neo4JImporter;

public abstract class Neo4jBenchmark extends Benchmark {
	protected Neo4JImporter importer;
	
	protected Index<Node> index;
	
	protected GraphDatabaseService neo4j;
	
	protected Node startNode;
	/*
	 * number of nodes
	 */
	protected int n;	
	
	@Override
	public void setUp() {
		// open connection
		importer.setUp();
		// read data
		importer.importData();
		neo4j = (GraphDatabaseService) importer.getDatabaseInstance();
		index = neo4j.index().forNodes("words");
		
		n = 0;
		for (Node v : neo4j.getAllNodes()) {
			n++;
		}	
		
		startNode = getRandomNode();
		
//		HashMap<Long, Integer> nCount = new HashMap<Long, Integer>();
//		
//		for (Node v : neo4j.getAllNodes()) {
//			int k = 0;
//			for (Relationship e : v.getRelationships(Direction.OUTGOING)) {
//				k++;
//			}
//			nCount.put(v.getId(), k);
//		}
//		
//		int sumK = 0;
//		int countZero = 0;
//		for (Entry<Long, Integer> entry : nCount.entrySet()) {
//			if(entry.getValue() == 0) countZero++;
//			sumK += entry.getValue();
//		}
//		double avgk = new Double(sumK) / nCount.size();
				
	}
	
	@Override
	public void tearDown() {
		// close connection
		importer.tearDown();

		// remove data
		importer.reset();		
	}
	
	protected Node getRandomNode() {
		Node v = null;
		int id;
		Random r = new Random();
		
		while(v == null) {
			id = r.nextInt(n);
			v = neo4j.getNodeById(id);
		}
		
		return v;
	}
}
