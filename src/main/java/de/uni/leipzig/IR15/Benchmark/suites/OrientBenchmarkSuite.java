package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Benchmark.ImportBenchmark;
import de.uni.leipzig.IR15.Importer.OrientDBImporter;
import de.uni.leipzig.IR15.Support.Configuration;

/**
 * OrientDB benchmark suite that runs all the configured benchmarks.
 * 
 * @author IR-Team
 * 
 */
public class OrientBenchmarkSuite extends AbstractBenchmarkSuite {

	public static Logger log = Logger.getLogger(OrientBenchmarkSuite.class);
	public static Configuration OrientDBCfg = Configuration.getInstance("orientdb");
	
	/**
	 * Initialize and run all benchmarks.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// a list of all benchmarks that should be done
		// benchmarks are added depending on the orientdb-property-files
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();
		boolean logToFile = OrientDBCfg.getPropertyAsBoolean("log");
		boolean doWarmup = OrientDBCfg.getPropertyAsBoolean("warmup");
		
		/**
		 * Import
		 */
		if (OrientDBCfg.getPropertyAsBoolean("import")) {
			Benchmark OrientDBImportBench = new ImportBenchmark(
					new OrientDBImporter());
			OrientDBImportBench.setRuns(1);
			benchmarks.add(OrientDBImportBench);
		}
		
		/**
		 * native Benchmarks
		 */
		if (OrientDBCfg.getPropertyAsBoolean("query1_native")) {
			Benchmark Query1_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query1_LowL_Benchmark();
			Query1_LL.setRuns(100);
			benchmarks.add(Query1_LL);
		}
		if (OrientDBCfg.getPropertyAsBoolean("query2_native")) {
			Benchmark Query2_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query2_LowL_Benchmark();
			Query2_LL.setRuns(100);
			benchmarks.add(Query2_LL);
		}
		if (OrientDBCfg.getPropertyAsBoolean("query3_native")) {
			Benchmark Query3_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query3_LowL_Benchmark();
			Query3_LL.setRuns(100);
			benchmarks.add(Query3_LL);
		}
		
		runBenchmarks(benchmarks, logToFile, doWarmup);
	}
}