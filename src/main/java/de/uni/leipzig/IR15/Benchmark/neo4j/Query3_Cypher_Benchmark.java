package de.uni.leipzig.IR15.Benchmark.neo4j;


/**
 * Query 3 selects all sentence co-occurrences of a given word which are
 * co-occurences of themselves.
 *
 * A -> B , A -> C, B -> C - The relationship B -> C is selected
 *
 * In SQL this is the following query:
 *
 * select w1.w1_id,w1.w2_id,w1.freq,w1.sig from co_s w1 where w1.w1_id in
 * (select w2.w2_id from co_s w2 where w2.w1_id=137) and w1.w2_id in (select
 * w3.w2_id from co_s w3 where w3.w1_id=137);
 *
 * @author Martin 's1ck' Junghanns
 *
 */
public class Query3_Cypher_Benchmark extends CypherBenchmark {

	/**
	 * Prepare statement before each run.
	 */
	@Override
	public void beforeRun() {
		super.beforeRun();
		CYPHER_QUERY = String
				.format("START n=node(%d) MATCH n-[:CO_S]->m, n-[:CO_S]-> t, m-[r:CO_S]-> t return m.w_id, t.w_id, r.sig, r.freq",
						startNode.getId());
	}

	/**
	 * Returns the name of the benchmark.
	 */
	@Override
	public String getName() {
		return "neo4j Query 3 (Cypher)";
	}
}