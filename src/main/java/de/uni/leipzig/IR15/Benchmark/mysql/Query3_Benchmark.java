package de.uni.leipzig.IR15.Benchmark.mysql;

/**
 * Query 3 selects all sentence co-occurences (co_s) between two words which are
 * sentence co-occurences of a given word.
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public class Query3_Benchmark extends MySQLBenchmark {

	/**
	 * Find a random start node and prepare the sql statement.
	 */
	@Override
	public void beforeRun() {
		start_WordID = getRandomStartNode(minOutDegree);
		query = String
				.format("select w1.w1_id,w1.w2_id,w1.freq,w1.sig from co_s w1 where w1.w1_id in (select w2.w2_id from co_s w2 where w2.w1_id=%d) and w1.w2_id in (select w3.w2_id from co_s w3 where w3.w1_id=%d);",
						start_WordID, start_WordID);
		super.beforeRun();
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "MySQL query 3";
	}
}