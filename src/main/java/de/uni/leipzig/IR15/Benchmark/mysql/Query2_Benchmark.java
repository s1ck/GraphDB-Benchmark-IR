package de.uni.leipzig.IR15.Benchmark.mysql;

/**
 * Query 2 selects all sentence co-occurrences of sentence co-occurrences (co_s)
 * of a given word.
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public class Query2_Benchmark extends MySQLBenchmark {

	/**
	 * Find a random start node and prepare the sql statement.
	 */
	@Override
	public void beforeRun() {
		start_WordID = getRandomStartNode(minOutDegree);
		log.info(String.format("Got random start node %d", start_WordID));
		query = String
				.format("select w1.w1_id,w1.w2_id,w1.freq,w1.sig from co_s w1 where w1.w1_id in (select w2.w2_id from co_s w2 where w2.w1_id=%d)",
						start_WordID);
		super.beforeRun();
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "MySQL query 2";
	}
}