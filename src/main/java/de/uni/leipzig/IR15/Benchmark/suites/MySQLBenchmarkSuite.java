package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import de.uni.leipzig.IR15.Benchmark.Benchmark;

public class MySQLBenchmarkSuite extends AbstractBenchmarkSuite {
	
	public static void main(String[] args) {
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();
		
		Benchmark query_1 = new de.uni.leipzig.IR15.Benchmark.mysql.Query1_Benchmark();
		query_1.setRuns(100);
		query_1.setWarmups(10);
		benchmarks.add(query_1);
		
		Benchmark query_2 = new de.uni.leipzig.IR15.Benchmark.mysql.Query2_Benchmark();
		query_1.setRuns(100);
		query_1.setWarmups(10);
		benchmarks.add(query_2);
		
		Benchmark query_3 = new de.uni.leipzig.IR15.Benchmark.mysql.Query3_Benchmark();
		query_1.setRuns(100);
		query_1.setWarmups(10);
		benchmarks.add(query_3);
		
		runBenchmarks(benchmarks, true);
	}
}
