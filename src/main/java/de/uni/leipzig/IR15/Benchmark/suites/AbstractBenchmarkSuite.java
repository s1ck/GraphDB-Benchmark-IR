package de.uni.leipzig.IR15.Benchmark.suites;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Support.Configuration;

/**
 * Abstract base class for all benchmark suites.
 * 
 * @author all
 * 
 */
public class AbstractBenchmarkSuite {

	public static Logger log = Logger.getLogger(Neo4JBenchmarkSuite.class);

	/**
	 * Run all benchmarks.
	 * 
	 * @param benchmarks
	 *            a list of benchmarks
	 */
	public static void runBenchmarks(List<Benchmark> benchmarks) {
		runBenchmarks(benchmarks, false, false);
	}

	/**
	 * Run all benchmarks.
	 * 
	 * @param benchmarks
	 *            a list of benchmarks
	 * @param log2file
	 *            true to turn on logging.
	 */
	public static void runBenchmarks(List<Benchmark> benchmarks,
			boolean log2file, boolean doWarmup) {
		for (Benchmark bm : benchmarks) {
			runBenchmark(bm, log2file, doWarmup);
		}
	}

	/**
	 * Run a benchmark.
	 * 
	 * @param benchmark
	 *            a benchmark
	 * @param log2file
	 *            true to turn on logging.
	 */
	public static void runBenchmark(Benchmark benchmark, boolean log2file,
			boolean doWarmup) {
		int runs = benchmark.getRuns();

		long[] totalResults = new long[runs];

		long start;
		long diff;

		long[] results = new long[runs];

		log.info(String.format("Starting Benchmark: %s with %d runs",
				benchmark.getName(), runs));

		benchmark.setUp();

		if (doWarmup) {
			log.info("starting warmup...");
			benchmark.warmup();
			log.info("done");
		}

		// do measurement
		for (int i = 0; i < runs; i++) {
			log.info(String.format("Starting run %d ...", i));
			benchmark.beforeRun();
			benchmark.setCurrentRun(i);
			start = System.currentTimeMillis();
			benchmark.run();
			diff = System.currentTimeMillis() - start;
			benchmark.afterRun();
			results[i] = diff;
			totalResults[i] = diff;
			log.info(String.format("Done with run %d", i));
			logMemoryInfo();
		}
		System.out.println();
		benchmark.tearDown();

		for (Entry<String, Object> e : benchmark.getResults(results).entrySet()) {
			log.info(String.format("%s = %s", e.getKey(), e.getValue()
					.toString()));
		}

		if (log2file) {
			storeResults(totalResults, benchmark);
		}

		log.info(String.format("Finished Benchmark: %s", benchmark.getName()));
	}

	/**
	 * Save the results to file.
	 * 
	 * @param results
	 * @param benchmark
	 */
	private static void storeResults(long[] results, Benchmark benchmark) {
		Configuration mysqlConf = Configuration.getInstance(benchmark
				.getDatabaseName().toLowerCase());

		String dirString = "out/benchmarks/"
				+ mysqlConf.getPropertyAsString("database")
				+ "/"
				+ benchmark.getName().toLowerCase()
				+ "/"
				+ new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss")
						.format(new Date());

		File dir = new File(dirString);
		if (dir.mkdirs()) {
			String fileString = dirString + "/"
					+ benchmark.getClass().getSimpleName();
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						new File(fileString)));

				for (int i = 0; i < results.length; i++) {
					writer.write(String.format("%d\n", results[i]));
				}
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void logMemoryInfo() {
		log.info(String.format(
				"total / max / free heap memory [MB]: %d / %d / %d", (Runtime
						.getRuntime().totalMemory() / 1024 / 1024), (Runtime
						.getRuntime().maxMemory() / 1024 / 1024), (Runtime
						.getRuntime().freeMemory() / 1024 / 1024)));
	}
}
