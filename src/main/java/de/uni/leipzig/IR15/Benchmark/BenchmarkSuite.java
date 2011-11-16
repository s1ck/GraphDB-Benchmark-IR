package de.uni.leipzig.IR15.Benchmark;

import de.uni.leipzig.IR15.Importer.Importer;
import de.uni.leipzig.IR15.Importer.Neo4JImporter;

public class BenchmarkSuite {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Importer neo4jImport = new Neo4JImporter();
		
		Benchmark importBench = new ImportBenchmark(neo4jImport);
		
		importBench.run();
	}

}
