package de.uni.leipzig.IR15.Benchmark.dex;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import de.uni.leipzig.IR15.Importer.DEXImporter.RelTypes;

import com.sparsity.dex.gdb.EdgesDirection;
import com.sparsity.dex.gdb.Objects;

public class BFS_Benchmark extends DEXBenchmark {
	
	private int maxDepth;
	private RelTypes relType;

	public BFS_Benchmark(int maxDepth,
			RelTypes relType,
			int startWID) {
		this.maxDepth = maxDepth;
		this.relType = relType;
		this.startWordID = startWID;
	}
	
	@Override
	public void run() {	
		doBFS(startNode);		
	}
	
	private void doBFS(long startNode) {
		Queue<Long> queue = new LinkedList<Long>();
		Set<Long> visited = new HashSet<Long>();
		
		int currentDepth = 0;
		queue.add(startNode);
		visited.add(startNode);

		long v;
		int edgeType = graph.findType(relType.toString());
		
		while (queue.size() > 0 && currentDepth <= maxDepth) {
			currentDepth++;
			v = queue.remove();
			Objects neighbors = graph.neighbors(v, edgeType, EdgesDirection.Any);
			for (long h : neighbors) {
				if(!visited.contains(h)) {
					queue.add(h);
					visited.add(h);
				}
			}				
		}
	}

	@Override
	public String getName() {
		return String.format("DEX getNeighbours maxDepth = %d", maxDepth);
	}

	@Override
	public void beforeRun() {
		//startNode = getRandomNode();
	}

}
