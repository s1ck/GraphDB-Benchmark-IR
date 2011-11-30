package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;

import de.uni.leipzig.IR15.Benchmark.AbstractBenchmarkSuite;
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

		Benchmark neo4jImportBench = new ImportBenchmark(importer);
		neo4jImportBench.setWarmups(0);
		neo4jImportBench.setRuns(1);
		benchmarks.add(neo4jImportBench);

//		Benchmark neo4jNeighbours = new de.uni.leipzig.IR15.Benchmark.neo4j.GetNeighboursBenchmark(
//				5, Neo4JImporter.RelTypes.CO_S);
//		neo4jNeighbours.setRuns(10000);
//		neo4jNeighbours.setWarmups(10);
//		benchmarks.add(neo4jNeighbours);

		runBenchmarks(benchmarks);		
	}
}
