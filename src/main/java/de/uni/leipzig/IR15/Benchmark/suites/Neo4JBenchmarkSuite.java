package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import de.uni.leipzig.IR15.Benchmark.Benchmark;

/**
 * Neo4j benchmark suite that runs all the configured benchmarks.
 * 
 * @author IR-Team
 * 
 */
public class Neo4JBenchmarkSuite extends AbstractBenchmarkSuite {

	/**
	 * Initialize and run all benchmarks.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();

		boolean logToFile = true;
		boolean doWarmup = false;

		// Benchmark neo4jImportBench = new ImportBenchmark(new
		// Neo4JImporter());
		// neo4jImportBench.setRuns(1);
		// benchmarks.add(neo4jImportBench);

		/**
		 * Cypher
		 */

		// Benchmark query_1 = new
		// de.uni.leipzig.IR15.Benchmark.neo4j.Query1_Cypher_Benchmark();
		// query_1.setRuns(100);
		// benchmarks.add(query_1);
		//
		// Benchmark query_2 = new
		// de.uni.leipzig.IR15.Benchmark.neo4j.Query2_Cypher_Benchmark();
		// query_2.setRuns(100);
		// benchmarks.add(query_2);
		//
		// Benchmark query_3 = new
		// de.uni.leipzig.IR15.Benchmark.neo4j.Query3_Cypher_Benchmark();
		// query_3.setRuns(100);
		// benchmarks.add(query_3);

		/**
		 * TraversalAPI
		 */

		// Benchmark query_1_traveral = new
		// de.uni.leipzig.IR15.Benchmark.neo4j.Query1_Traverser_Benchmark();
		// query_1_traveral.setRuns(100);
		// benchmarks.add(query_1_traveral);
		//
		// Benchmark query_2_traversal = new
		// de.uni.leipzig.IR15.Benchmark.neo4j.Query2_Traverser_Benchmark();
		// query_2_traversal.setRuns(100);
		// benchmarks.add(query_2_traversal);
		//
		// Benchmark query_3_traversal = new
		// de.uni.leipzig.IR15.Benchmark.neo4j.Query3_Traverser_Benchmark();
		// query_3_traversal.setRuns(100);
		// benchmarks.add(query_3_traversal);

		/**
		 * Native API
		 */

		// Benchmark query_1_native = new
		// de.uni.leipzig.IR15.Benchmark.neo4j.Query1_Native_Benchmark();
		// query_1_native.setRuns(100);
		// benchmarks.add(query_1_native);

		Benchmark query_2_native = new de.uni.leipzig.IR15.Benchmark.neo4j.Query2_Native_Benchmark();
		query_2_native.setRuns(100);
		benchmarks.add(query_2_native);

		Benchmark query_3_native = new de.uni.leipzig.IR15.Benchmark.neo4j.Query3_Native_Benchmark();
		query_3_native.setRuns(100);
		benchmarks.add(query_3_native);

		runBenchmarks(benchmarks, logToFile, doWarmup);
	}
}