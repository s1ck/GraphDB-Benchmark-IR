package de.uni.leipzig.IR15.Benchmark.orientdb;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.OrientDBConnector;

public abstract class OrientDBBenchmark extends Benchmark {
	
	protected long startWordID;
	protected OGraphDatabase orientdb;
	protected int numVertices;
	
	@Override
	public void setUp() {
		orientdb = OrientDBConnector.getConnection();
		// numVertices
		numVertices = (int) orientdb.countVertexes();
	}
	
	@Override
	public void beforeRun() {
		startWordID = r.nextInt(numVertices);
	}

	@Override
	public void tearDown() {
		OrientDBConnector.destroyConnection();
	}	
	
	
}