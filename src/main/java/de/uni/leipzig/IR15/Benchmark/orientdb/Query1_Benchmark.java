package de.uni.leipzig.IR15.Benchmark.orientdb;

import java.util.List;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

public class Query1_Benchmark extends OrientDBBenchmark{

	@Override
	public void run() {
		List<ODocument> result;
		
		String q1 = "SELECT FROM OGraphVertex WHERE w_id = " + startWordID;
		result = orientdb.query(new OSQLSynchQuery<ODocument>(q1));
		//for (ODocument v : result)
		//	System.out.println(v.toString());
		/*
		 * String q2 = "SELECT FROM 5:1000 WHERE all()"; result =
		 * orientdb.query(new OSQLSynchQuery<ODocument>(q2)); for (ODocument v :
		 * result) System.out.println(v.toString());
		 */
	}	
	
	@Override
	public String getName() {
		return "OrientDB Query1";
	}
	
}
