package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.cypher.parser.CypherParser;

/**
 * This query selects all sentence co-occurrences of a given word which
 * are co-occurences themselves. 
 * A -> B , A -> C, B -> C
 * 
 * In SQL this is the following query:
 * 
 * select w1.w1_id,w1.w2_id,w1.freq,w1.sig from co_s w1 
 * 	where 
 *		w1.w1_id in (select w2.w2_id from co_s w2 where w2.w1_id=137) 
 * 	and 
 *		w1.w2_id in (select w3.w2_id from co_s w3 where w3.w1_id=137);
 *
 * @author s1ck
 *
 */
public class Query3_Benchmark extends CypherBenchmark {

	@Override
	public void setUp() {
		super.setUp();
		CYPHER_QUERY = new CypherParser().parse(String.format("START n=node(%d) MATCH n-[:CO_S]->m, n-[:CO_S]-> t, m-[r:CO_S]-> t return m.w_id, t.w_id, r.sig, r.freq", startNode.getId()));
	}
	
	@Override
	public void beforeRun() {
		super.beforeRun();
		CYPHER_QUERY = new CypherParser().parse(String.format("START n=node(%d) MATCH n-[:CO_S]->m, n-[:CO_S]-> t, m-[r:CO_S]-> t return m.w_id, t.w_id, r.sig, r.freq", startNode.getId()));
	}
	
	@Override
	public String getName() {		
		return "neo4j Query 3";
	}

}
