package de.uni.leipzig.IR15.Benchmark.orientdb;

import java.util.List;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

public class GetOneWordByID_Benchmark extends OrientDBBenchmark{

	@Override
	public void run() {
		List<ODocument> result;
		
		//String q1 = "SELECT FROM V WHERE w_id = " + startWordID;
		String q1 = "SELECT FROM index:word_id_Index WHERE key = " + startWordID ;
		
		//log.info("Query: " + q1);
		result = orientdb.query(new OSQLSynchQuery<ODocument>(q1));
		//for (ODocument v : result)
		//	log.info(v.toString());
	}	
	
	@Override
	public String getName() {
		return "OrientDB GetOneWordByID SQL Benchmark";
	}
	
}
