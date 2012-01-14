package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Benchmark.ImportBenchmark;
import de.uni.leipzig.IR15.Importer.OrientDBImporter;

/**
 * OrientDB benchmark suite that runs all the configured benchmarks.
 * 
 * @author IR-Team
 * 
 */
public class OrientBenchmarkSuite extends AbstractBenchmarkSuite {

	public static Logger log = Logger.getLogger(OrientBenchmarkSuite.class);

	/**
	 * Initialize and run all benchmarks.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		List<Benchmark> benchmarks = new ArrayList<Benchmark>();

		boolean logToFile = true;
		boolean doWarmup = false;

		Benchmark OrientDBImportBench = new ImportBenchmark(
				new OrientDBImporter());
		OrientDBImportBench.setRuns(1);
		benchmarks.add(OrientDBImportBench);

		Benchmark Query1_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query1_LowL_Benchmark();
		Query1_LL.setRuns(100);
		benchmarks.add(Query1_LL);

		Benchmark Query2_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query2_LowL_Benchmark();
		Query2_LL.setRuns(100);
		benchmarks.add(Query2_LL);

		Benchmark Query3_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query3_LowL_Benchmark();
		Query3_LL.setRuns(100);
		benchmarks.add(Query3_LL);

		runBenchmarks(benchmarks, logToFile, doWarmup);
	}
}