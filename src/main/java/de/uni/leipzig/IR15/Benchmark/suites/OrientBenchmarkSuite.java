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
		// benchmarks.add(OrientDBImportBench);
		
//		Benchmark getWordByID_Query = new de.uni.leipzig.IR15.Benchmark.orientdb.GetOneWordByID_Benchmark();
//		getWordByID_Query.setRuns(5);
//		getWordByID_Query.setWarmups(1);
//		benchmarks.add(getWordByID_Query);
		
		Benchmark Query1_SQL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query1_SQL_Benchmark();
		Query1_SQL.setRuns(5);
		Query1_SQL.setWarmups(1);
		benchmarks.add(Query1_SQL);
		
		runBenchmarks(benchmarks, true);
	}
	
}
