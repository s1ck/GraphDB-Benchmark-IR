package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import de.uni.leipzig.IR15.Benchmark.Benchmark;

/**
 * Mysql benchmark suite that runs all the configured benchmarks.
 * 
 * @author IR-Team
 * 
 */
public class MySQLBenchmarkSuite extends AbstractBenchmarkSuite {

	/**
	 * Initialize and run all benchmarks.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();

		boolean doWarmup = true;
		boolean logToFile = true;

		// Benchmark query_1 = new
		// de.uni.leipzig.IR15.Benchmark.mysql.Query1_Benchmark();
		// query_1.setRuns(100);

		// benchmarks.add(query_1);

		Benchmark query_2 = new de.uni.leipzig.IR15.Benchmark.mysql.Query2_Benchmark();
		query_2.setRuns(100);
		benchmarks.add(query_2);

		Benchmark query_3 = new de.uni.leipzig.IR15.Benchmark.mysql.Query3_Benchmark();
		query_3.setRuns(100);
		benchmarks.add(query_3);

		runBenchmarks(benchmarks, logToFile, doWarmup);
	}
}