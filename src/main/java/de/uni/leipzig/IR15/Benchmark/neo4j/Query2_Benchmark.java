package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.cypher.parser.CypherParser;

/**
 * Query 2 selects the wordID of all sentence co-occurrences of co-occurrences of a given word. It also
 * selects the significance and the frequency of the 2nd degree co-occurrences.
 * 
 * In SQL this is the following query:
 * 
 * START n=node:words(w_id = "137") MATCH n-[:CO_S]->t-[r:CO_S]-> m return t.w_id, m.w_id, r.sig, r.freq
 * 
 * @author s1ck
 *
 */
public class Query2_Benchmark extends CypherBenchmark {

	@Override
	public void setUp() {
		super.setUp();
		CYPHER_QUERY = new CypherParser().parse(String.format("START n=node(%d) MATCH n-[:CO_S]->t-[r:CO_S]-> m return t.w_id, m.w_id, r.sig, r.freq", startNode.getId()));
	}
	
	@Override
	public void beforeRun() {
		super.beforeRun();
		CYPHER_QUERY = new CypherParser().parse(String.format("START n=node(%d) MATCH n-[:CO_S]->t-[r:CO_S]-> m return t.w_id, m.w_id, r.sig, r.freq", startNode.getId()));
	}

	@Override
	public String getName() {		
		return "neo4j Query 2";
	}
}
