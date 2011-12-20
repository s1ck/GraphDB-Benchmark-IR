package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.cypher.CypherParser;

/**
 * Query 2 selects the wordID of all sentence co-occurrences of co-occurrences
 * of a given word. It also selects the significance and the frequency of the
 * 2nd degree co-occurrences.
 * 
 * In SQL this is the following query:
 * 
 * select w1.w1_id,w1.w2_id,w1.freq,w1.sig from co_s w1 where w1.w1_id in
 * (select w2.w2_id from co_s w2 where w2.w1_id=137)
 * 
 * @author s1ck
 * 
 */
public class Query2_Cypher_Benchmark extends CypherBenchmark {

	@Override
	public void beforeRun() {
		super.beforeRun();
		CYPHER_QUERY = new CypherParser()
				.parse(String
						.format("START n=node(%d) MATCH n-[:CO_S]->t-[r:CO_S]-> m return t.w_id, m.w_id, r.sig, r.freq",
								startNode.getId()));
	}

	@Override
	public String getName() {
		return "neo4j Query 2 (Cypher)";
	}
}
