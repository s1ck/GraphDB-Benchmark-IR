package de.uni.leipzig.IR15.Benchmark;

import de.uni.leipzig.IR15.Importer.*;

public class BenchmarkSuite {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Importer neo4jImport = new Neo4JImporter();
		Importer dexImport = new DEXImporter();
		
		Benchmark neo4jImportBench = new ImportBenchmark(neo4jImport);
		neo4jImportBench.run();
		
		Benchmark dexImportBench = new ImportBenchmark(dexImport);
		dexImportBench.run();
	}

}
