package de.uni.leipzig.IR15.Benchmark;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * This abstract base class must be extended by any benchmark in the suite. It
 * defines some simple methods which are used during benchmark processing.
 * 
 * @author s1ck
 * 
 */
public abstract class Benchmark {

	protected static Logger log = Logger.getLogger(Benchmark.class);

	/**
	 * Number of warmups runs
	 * 
	 * Warmup runs are not included in the final results.
	 */

	private int warmups = 10;
	/**
	 * Number of benchmark runs
	 */
	private int runs = 100;

	/**
	 * constant seed for same random IDs for each benchmark
	 */
	protected Random r = new Random(13374223);

	/**
	 * Sets the warmup runs for the benchmark
	 * 
	 * @param warmups
	 *            number of warmups
	 */
	public void setWarmups(int warmups) {
		this.warmups = warmups;
	}

	/**
	 * Returns the number of warmups
	 * 
	 * @return number of warmups
	 */
	public int getWarmups() {
		return this.warmups;
	}

	/**
	 * Sets the number of benchmarks to run
	 * 
	 * @param runs
	 *            number of benchmarks
	 */
	public void setRuns(int runs) {
		this.runs = runs;
	}

	/**
	 * Returns the number of benchmarks to run
	 * 
	 * @return number of benchmarks
	 */
	public int getRuns() {
		return this.runs;
	}

	/**
	 * This method is called once before <code>run()</code> is invoked for the
	 * first time.
	 */
	public abstract void setUp();

	/**
	 * This method runs the benchmark
	 */
	public abstract void run();

	/**
	 * This method is called once after the last call of <code>run()</code>
	 */
	public abstract void tearDown();

	/**
	 * This method is called before each single call of <code>run()</code>
	 */
	public abstract void beforeRun();

	/**
	 * Returns the Benchmarks name
	 * 
	 * @return name of the benchmark
	 */
	public abstract String getName();

	/**
	 * This method calculates some default measurement results: average,
	 * standard deviation, minimum and maximum value.
	 * 
	 * The method can be overriden and enhanced with custom benchmark results.
	 * 
	 * @param runtimes
	 *            an array with all benchmark results
	 * @return a map between measure name (e.g. "average") and its value (e.g.
	 *         23)
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
			if (runtimes[i] < min)
				min = runtimes[i];
			// max
			if (runtimes[i] > max)
				max = runtimes[i];
		}
		double stdev = Math.sqrt( new Double(tmp) / (runtimes.length) );

		results.put("Stdev [ms]", stdev);
		results.put("Min [ms]", min);
		results.put("Max [ms]", max);

		return results;
	}
}
