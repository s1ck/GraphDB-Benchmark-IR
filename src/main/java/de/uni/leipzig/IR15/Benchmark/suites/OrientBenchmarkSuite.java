package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Benchmark.ImportBenchmark;
import de.uni.leipzig.IR15.Importer.OrientDBImporter;

public class OrientBenchmarkSuite extends AbstractBenchmarkSuite {

	public static Logger log = Logger.getLogger(OrientBenchmarkSuite.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();
		
		Benchmark OrientDBImportBench = new ImportBenchmark(new OrientDBImporter());
		OrientDBImportBench.setWarmups(0);
		OrientDBImportBench.setRuns(1);
		benchmarks.add(OrientDBImportBench);
		
		Benchmark query_1 = new de.uni.leipzig.IR15.Benchmark.orientdb.Query1_Benchmark();
		query_1.setRuns(100);
		query_1.setWarmups(10);
		benchmarks.add(query_1);
		
		runBenchmarks(benchmarks);
	}
	
}
