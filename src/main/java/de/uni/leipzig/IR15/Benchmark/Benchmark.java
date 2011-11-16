package de.uni.leipzig.IR15.Benchmark;

import org.apache.log4j.Logger;


public abstract class Benchmark {
	
	protected static Logger log = Logger.getLogger(Benchmark.class);
	
	/**
	 * start timestamp (unix)
	 */
	private long start;
	
	/**
	 * stop timestamp (unix)
	 */
	private long diff;
	
	/**
	 * starts the timer
	 * @return unix timestamp
	 */
	protected long startTimer() {		
		start = System.currentTimeMillis();
		return start;
	}
	
	/**
	 * stops the timer
	 * @return unix timestamp
	 */
	protected long stopTimer() {
		diff = System.currentTimeMillis() - start;	
		return diff;
	}
	
	/**
	 * logs some information about the running benchmark
	 */
	protected void logStart() {
		log.info(String.format("Starting Benchmark: %s", getName()));
	}
	
	/**
	 * logs some information about the running benchmark
	 */
	protected void logStop() {
		log.info(String.format("Finished Benchmark: %s", getName()));
	}
	
	/**
	 * runs the benchmark
	 */
	public abstract void run();
	
	/**
	 * Benchmark name
	 * 
	 * @return The name of the benchmark
	 */
	public abstract String getName();
}
