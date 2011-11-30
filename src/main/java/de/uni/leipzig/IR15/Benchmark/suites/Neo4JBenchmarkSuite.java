package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Benchmark.ImportBenchmark;
import de.uni.leipzig.IR15.Importer.Neo4JImporter;

public class Neo4JBenchmarkSuite extends AbstractBenchmarkSuite {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();

		// importer
		Neo4JImporter importer = new Neo4JImporter();

//		Benchmark neo4jImportBench = new ImportBenchmark(importer);
//		neo4jImportBench.setWarmups(0);
//		neo4jImportBench.setRuns(1);
//		benchmarks.add(neo4jImportBench);

		Benchmark neo4jNeighbours = new de.uni.leipzig.IR15.Benchmark.neo4j.BFS_Benchmark(
				5, Neo4JImporter.RelTypes.CO_S);
		neo4jNeighbours.setRuns(10000);
		neo4jNeighbours.setWarmups(10);
		benchmarks.add(neo4jNeighbours);
		
		Benchmark query_1 = new de.uni.leipzig.IR15.Benchmark.neo4j.Query1_Benchmark();
		query_1.setRuns(100);
		query_1.setWarmups(10);
		benchmarks.add(query_1);
		
		Benchmark query_2 = new de.uni.leipzig.IR15.Benchmark.neo4j.Query2_Benchmark();
		query_2.setRuns(100);
		query_2.setWarmups(10);
		benchmarks.add(query_2);
		
		Benchmark query_3 = new de.uni.leipzig.IR15.Benchmark.neo4j.Query3_Benchmark();
		query_3.setRuns(100);
		query_3.setWarmups(10);
		benchmarks.add(query_3);
		
		Benchmark query_4 = new de.uni.leipzig.IR15.Benchmark.neo4j.Query4_Benchmark();
		query_4.setRuns(100);
		query_4.setWarmups(10);
		benchmarks.add(query_4);
		
		runBenchmarks(benchmarks);		
	}
}
