package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.Database;
import com.sparsity.dex.gdb.Graph;
import com.sparsity.dex.gdb.Session;
import com.sparsity.dex.gdb.TypeList;
import com.sparsity.dex.gdb.Value;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.DEXConnector;
import de.uni.leipzig.IR15.Connectors.Neo4JConnector;
import de.uni.leipzig.IR15.Importer.DEXImporter;

public abstract class DEXBenchmark extends Benchmark {
	protected Database dex;
	protected Session session;
	protected Graph graph;
	protected int startWordID;
	protected long startNode;

	@Override
	public void setUp() {
		dex = DEXConnector.getConnection();
		session = dex.newSession();
		session.begin();
		graph = session.getGraph();

		int wordNodeType = graph.findType("word");
		int wordIdAttribute = graph.findAttribute(wordNodeType, "w_id");

		startNode = graph.findObject(wordIdAttribute,
				new Value().setInteger(startWordID));
	}

	@Override
	public void tearDown() {
		DEXConnector.destroyConnection();
	}

}
