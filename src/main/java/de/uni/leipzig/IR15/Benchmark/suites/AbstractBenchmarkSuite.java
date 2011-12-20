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

public class AbstractBenchmarkSuite {

	public static Logger log = Logger.getLogger(Neo4JBenchmarkSuite.class);

	public static void runBenchmarks(List<Benchmark> benchmarks) {
		runBenchmarks(benchmarks, false);
	}

	public static void runBenchmarks(List<Benchmark> benchmarks, boolean log2file) {
		for(Benchmark bm : benchmarks) {
			runBenchmark(bm, log2file);
		}
	}

	public static void runBenchmark(Benchmark benchmark, boolean log2file) {
		int warmups = benchmark.getWarmups();
		int runs = benchmark.getRuns();

		long[] totalResults = new long[warmups + runs];

		long start;
		long diff;

		long[] results = new long[runs];

		log.info(String.format("Starting Benchmark: %s with %d warmups and %d runs", benchmark.getName(), warmups, runs));

		benchmark.setUp();

		// do warmup
		for (int i = 0; i < warmups; i++) {
			benchmark.beforeRun();
			benchmark.setCurrentRun(i);
			start = System.currentTimeMillis();
			benchmark.run();
			diff = System.currentTimeMillis() - start;
			totalResults[i] = diff;
		}

		// do measurement
		for (int i = 0; i < runs; i++) {
			benchmark.beforeRun();
			benchmark.setCurrentRun(i);
			start = System.currentTimeMillis();
			benchmark.run();
			diff = System.currentTimeMillis() - start;
			benchmark.afterRun();
			results[i] = diff;
			totalResults[warmups + i] = diff;
		}
		benchmark.tearDown();

		for(Entry<String, Object> e : benchmark.getResults(results).entrySet()) {
			log.info(String.format("%s = %s", e.getKey(), e.getValue().toString()));
		}

		if(log2file) {
			storeResults(totalResults, benchmark);
		}

		log.info(String.format("Finished Benchmark: %s", benchmark.getName()));
	}

	private static void storeResults(long[] results, Benchmark benchmark) {

		String dirString = "out/benchmarks/" + benchmark.getName().toLowerCase() + "/" + new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(new Date());
		File dir = new File(dirString);
		if(dir.mkdirs()) {
			String fileString = dirString + "/" + benchmark.getClass().getSimpleName();
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileString)));

				for (int i = 0; i < results.length; i++) {
					writer.write(String.format("%d\n", results[i]));
				}
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
