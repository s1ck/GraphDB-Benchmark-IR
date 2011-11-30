package de.uni.leipzig.IR15.Benchmark.suites;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Benchmark.Benchmark;

public class OrientBenchmarkSuite extends AbstractBenchmarkSuite  {
	
	public static Logger log = Logger.getLogger(OrientBenchmarkSuite.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();
				
		/**
		 * Place your benchmarks here
		 */
		
		runBenchmarks(benchmarks);
	}	
}
