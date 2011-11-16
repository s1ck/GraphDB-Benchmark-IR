package de.uni.leipzig.IR15.Benchmark;

import org.apache.log4j.Logger;


public abstract class Benchmark {
	
	protected static Logger log = Logger.getLogger(Benchmark.class);
	
	abstract void run();
}
