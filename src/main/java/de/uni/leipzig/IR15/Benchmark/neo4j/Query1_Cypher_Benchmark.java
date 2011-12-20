package de.uni.leipzig.IR15.Benchmark.neo4j;


/**
 * Query 1 selects the wordID of all the sentence co-occurrences of a given word.
 * 
 * In SQL this is the following query:
 * 
 * select w1.w2_id from co_s w1 where w1.w1_id=137;
 * 
 * @author Martin 's1ck' Junghanns
 *
 */
public class Query1_Cypher_Benchmark extends CypherBenchmark {
	
	@Override
	public void beforeRun() {
		super.beforeRun();
		CYPHER_QUERY = String.format("START n=node(%d) MATCH n-[:CO_S]->m return m.w_id", startNode.getId());
	}

	@Override
	public String getName() {		
		return "neo4j Query 1 (Cypher)";
	}
}
