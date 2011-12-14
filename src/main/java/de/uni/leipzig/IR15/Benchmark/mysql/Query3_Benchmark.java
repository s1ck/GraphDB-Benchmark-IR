package de.uni.leipzig.IR15.Benchmark.mysql;

public class Query3_Benchmark extends MySQLBenchmark {

	@Override
	public void setUp() {
		super.setUp();
		query = String
				.format("select w1.w1_id,w1.w2_id,w1.freq,w1.sig from co_s w1 where w1.w1_id in (select w2.w2_id from co_s w2 where w2.w1_id=%d) and w1.w2_id in (select w3.w2_id from co_s w3 where w3.w1_id=%d);",
						start_WordID, start_WordID);
	}

	@Override 
	public void beforeRun() {
		start_WordID = getRandomStartNode();
		query = String
				.format("select w1.w1_id,w1.w2_id,w1.freq,w1.sig from co_s w1 where w1.w1_id in (select w2.w2_id from co_s w2 where w2.w1_id=%d) and w1.w2_id in (select w3.w2_id from co_s w3 where w3.w1_id=%d);",
						start_WordID, start_WordID);
		super.beforeRun();
	}
	
	@Override
	public String getName() {
		return "MySQL query 3";
	}

}
