package de.uni.leipzig.IR15.Benchmark;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public abstract class Benchmark {

	protected static Logger log = Logger.getLogger(Benchmark.class);

	/*
	 * number of warmups runs
	 */
	private int warmups = 10;
	/*
	 * number of test runs
	 */
	private int runs = 100;

	/*
	 * constant seed for same random IDs for each benchmark
	 */
	protected Random r = new Random(13374223);
	
	public void setWarmups(int warmups) {
		this.warmups = warmups;
	}

	public int getWarmups() {
		return this.warmups;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getRuns() {
		return this.runs;
	}

	/*
	 * Preparation before benchmark
	 */
	public abstract void setUp();

	/**
	 * runs the benchmark
	 */
	public abstract void run();

	/*
	 * Cleanup after benchmark
	 */
	public abstract void tearDown();

	/*
	 * Reset the benchmark state (in case of multiple run calls)
	 */
	public abstract void beforeRun();

	/**
	 * Benchmark name
	 * 
	 * @return The name of the benchmark
	 */
	public abstract String getName();

	/**
	 * Builds a result string with. This method can be overriden to print
	 * benchmark specific results.
	 * 
	 * @param runtimes
	 * @return hashtable wih results
	 */
	public Map<String, Object> getResults(long[] runtimes) {
		Map<String, Object> results = new TreeMap<String, Object>();

		// average
		long sum = 0L;
		for (int i = 0; i < runtimes.length; i++) {
			sum += runtimes[i];
		}
		double avg = new Double(sum) / runtimes.length;

		results.put("Average [ms]", avg);

		// stdev, min, max
		long tmp = 0;
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		for (int i = 0; i < runtimes.length; i++) {
			tmp += Math.pow((runtimes[i] - avg), 2);
			// min
			if(runtimes[i] < min) min = runtimes[i];
			// max
			if(runtimes[i] > max) max = runtimes[i];
		}
		double stdev = new Double(tmp) / (runtimes.length);

		results.put("Stdev [ms]", stdev);
		results.put("Min [ms]", min);
		results.put("Max [ms]", max);
		
		return results;
	}
}
