package de.uni.leipzig.IR15.Benchmark.neo4j;

public class GetNeighboursBenchmark extends Neo4jBenchmark {

	@Override
	public void run() {
		logStart();
		
		startTimer();
		
		
		// do something
		
		
		long diff = stopTimer();
		
		log.info(String.format("Took %d [ms]", diff));
		logStop();		
	}

	@Override
	public String getName() {		
		return "Neo4J GetNeighbours Benchmark";
	}

}
