package de.uni.leipzig.IR15.Benchmark.dex;

import com.sparsity.dex.gdb.Database;
import com.sparsity.dex.gdb.Graph;
import com.sparsity.dex.gdb.Session;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Importer.DEXImporter;

public abstract class DEXBenchmark extends Benchmark {
	protected DEXImporter importer;
	protected Database dex;
	protected Session session;
	protected Graph graph;
}
