package de.uni.leipzig.IR15.Benchmark.orientdb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

/**
 * Query 1 selects the wordID of all the sentence co-occurrences of a given
 * word. In SQL this is the following query:
 * 
 * select w1.w2_id from co_s w1 where w1.w1_id=137;
 * 
 */

public class Query1_SQL_Benchmark extends OrientDBBenchmark {

	private OSQLSynchQuery<ODocument> preparedQuery;

	private Map<String, Object> queryParams;

	@Override
	public void setUp() {
		super.setUp();
		preparedQuery = new OSQLSynchQuery<ODocument>(
				"select out.w_id from E where in.w_id = :wordid and type = 'co_s'");
	}

	@Override
	public void beforeRun() {
		super.beforeRun();
		queryParams = new HashMap<String, Object>();
		queryParams.put("wordid", startWordID);
	}

	@Override
	public void run() {
		List<ODocument> result;
		result = orientdb.command(prep_query1).execute(startWordID);
		
		//for (ODocument v : result)
		//	log.info(v.toString());
	}	
	

	@Override
	public String getName() {
		return "OrientDB Query1 SQL";
	}

}



/*
OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>("select out.w_id from E where in.w_id = :wordid and type = 'co_s'");
Map<String,Object> params = new HashMap<String,Object>();
params.put("wordid", startWordID);

result = orientdb.command(query).execute(params);
*/


//String q1 = "select out.w_id from co_s where in.w_id = " + startWordID;
		//log.info("Query: " + q1);
		//result = orientdb.query(new OSQLSynchQuery<ODocument>(q1));
		//log.info("Size: " + result.size());
