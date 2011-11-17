package de.uni.leipzig.IR15.Benchmark.dex;

import java.util.LinkedList;
import java.util.Queue;

import de.uni.leipzig.IR15.Importer.DEXImporter;
import de.uni.leipzig.IR15.Importer.DEXImporter.RelTypes;

import com.sparsity.dex.gdb.Database;
import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;
import com.sparsity.dex.gdb.Value;

public class GetNeighboursBenchmark extends DEXBenchmark {
	
	private int maxDepth;
	private RelTypes relType;
	private int startWordID; 
	private long startNode;

	public GetNeighboursBenchmark(DEXImporter importer, int maxDepth,
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
		dex = (Database) importer.getDatabaseInstance();
	    session = dex.newSession();
	    session.begin();		
	    graph = session.getGraph();
	    
	    int wordNodeType = graph.findType("word");
	    int wordIdAttribute = graph.findAttribute(wordNodeType, "w_id");
	    
		startNode = graph.findObject(wordIdAttribute, new Value().setInteger(startWordID));
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
	
	private void doBFS(long startNode) {
		Queue<Long> q = new LinkedList<Long>();
		int currentDepth = 0;
		q.add(startNode);

		long v;
		int edgeType = graph.findType(relType.toString());
		
		while (q.size() > 0 && currentDepth <= maxDepth) {
			currentDepth++;
			v = q.remove();
			Objects neighbors = graph.neighbors(v, edgeType, EdgesDirection.Any);
			for (long h : neighbors) {
				q.add(h);
			}				
		}
	}

	@Override
	public String getName() {
		return String.format("neo4j getNeighbours maxDepth = %d", maxDepth);
	}

}
