package de.uni.leipzig.IR15.Benchmark;

import org.apache.log4j.Logger;


public abstract class Benchmark {		
	
	protected static Logger log = Logger.getLogger(Benchmark.class);
	
	private int warmups = 10;
	
	private int runs = 100;	
	
	/**
	 * start timestamp (unix)
	 */
	private long start;
	
	/**
	 * stop timestamp (unix)
	 */
	private long diff;
	
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
	
	
	public abstract void setUp();
	/**
	 * runs the benchmark
	 */
	public abstract void run();
	/*
	 * 	
	 */
	public abstract void tearDown();
	/*
	 * 
	 */
	public abstract void reset();
	/**
	 * Benchmark name
	 * 
	 * @return The name of the benchmark
	 */
	public abstract String getName();
}
