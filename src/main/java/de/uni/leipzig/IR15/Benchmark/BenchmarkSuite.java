package de.uni.leipzig.IR15.Benchmark;

import de.uni.leipzig.IR15.Importer.Importer;
import de.uni.leipzig.IR15.Importer.Neo4JImporter;
import de.uni.leipzig.IR15.Importer.OrientDBImporter;

public class BenchmarkSuite {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Importer neo4jImport = new Neo4JImporter();
		// OrientDBImporter();
		Benchmark importBench = new ImportBenchmark(neo4jImport);
		
		importBench.run();
	}

}
