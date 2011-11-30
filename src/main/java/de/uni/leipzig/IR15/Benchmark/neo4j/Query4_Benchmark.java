package de.uni.leipzig.IR15.Benchmark.neo4j;

import org.neo4j.cypher.parser.CypherParser;

public class Query4_Benchmark extends CypherBenchmark {

	@Override
	public void setUp() {
		super.setUp();
		CYPHER_QUERY = new CypherParser().parse("START n=node(119) MATCH n-[:CO_S]->m, n-[:CO_S]-> t, m-[r:CO_S]-> t return m.w_id, t.w_id, r.sig, r.freq");
	}
	
	@Override
	public String getName() {		
		return "neo4j Query 4";
	}
}
