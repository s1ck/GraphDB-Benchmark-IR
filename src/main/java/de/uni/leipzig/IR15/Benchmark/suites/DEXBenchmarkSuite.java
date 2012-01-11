package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Benchmark.ImportBenchmark;
import de.uni.leipzig.IR15.Importer.DEXImporter;

/**
 * DEX benchmark suite that runs all the configured benchmarks.
 * 
 * @author IR-Team
 * 
 */
public class DEXBenchmarkSuite extends AbstractBenchmarkSuite {

	public static Logger log = Logger.getLogger(DEXBenchmarkSuite.class);

	/**
	 * Initialize and run all benchmarks.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();

		boolean logToFile = true;
		boolean doWarmup = true;

		Benchmark dexImportBench = new ImportBenchmark(new DEXImporter());
		dexImportBench.setRuns(1);
		benchmarks.add(dexImportBench);

		// Benchmark query_1 = new
		// de.uni.leipzig.IR15.Benchmark.dex.Query1_Benchmark();
		// query_1.setRuns(100);
		// benchmarks.add(query_1);
		//
		Benchmark query_2 = new de.uni.leipzig.IR15.Benchmark.dex.Query2_Benchmark();
		query_2.setRuns(100);
		benchmarks.add(query_2);

		Benchmark query_3 = new de.uni.leipzig.IR15.Benchmark.dex.Query3_Benchmark();
		query_3.setRuns(100);
		benchmarks.add(query_3);

		runBenchmarks(benchmarks, logToFile, doWarmup);
	}
}