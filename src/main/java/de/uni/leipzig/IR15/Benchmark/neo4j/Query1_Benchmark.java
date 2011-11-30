package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.cypher.parser.CypherParser;

/**
 * Query 1 selects the wordID of all the sentence co-occurrences of a given word.
 * 
 * In SQL this is the following query:
 * 
 * select w1.w2_id from co_s w1 where w1.w1_id=137;
 * 
 * @author s1ck
 *
 */
public class Query1_Benchmark extends CypherBenchmark {

	@Override
	public void setUp() {
		super.setUp();
		CYPHER_QUERY = new CypherParser().parse("START n=node:words(w_id = '137') MATCH n-[:CO_S]->m return m.w_id");
	}

	@Override
	public String getName() {		
		return "neo4j Query 1";
	}
}
