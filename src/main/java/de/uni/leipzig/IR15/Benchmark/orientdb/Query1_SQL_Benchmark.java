package de.uni.leipzig.IR15.Benchmark.orientdb;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

/**
 * Query 1 selects the wordID of all the sentence co-occurrences of a given word.
 * In SQL this is the following query:
 * 
 * select w1.w2_id 
 * from co_s w1 
 * where w1.w1_id=137;
 *
 */

public class Query1_SQL_Benchmark extends OrientDBBenchmark{

	@Override
	public void run() {
		List<ODocument> result;
		
		
		OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>("select out.w_id from E where in.w_id = :wordid and type = 'co_s'");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("wordid", startWordID);

		result = orientdb.command(query).execute(params);
		
		//String q1 = "select out.w_id from E where in.w_id = ? and type = 'co_s'";
		//OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>(q1);
		//String s = String.valueOf( startWordID );
		//result = orientdb.command(query).execute(s);
		
		//String q1 = "select out.w_id from E where in.w_id = " + startWordID + " and type = 'co_s'";
		//log.info("Query: " + q1);
		//result = orientdb.query(new OSQLSynchQuery<ODocument>(q1));
		//log.info("Size: " + result.size());
		//for (ODocument v : result)
		//	log.info(v.toString());
	}	
	
	@Override
	public String getName() {
		return "OrientDB Query1 SQL";
	}
	
}
