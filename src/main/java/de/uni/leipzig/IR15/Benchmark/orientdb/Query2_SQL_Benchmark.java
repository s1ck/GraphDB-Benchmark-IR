package de.uni.leipzig.IR15.Benchmark.orientdb;

import java.util.List;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

/**
 * Query 1 selects the wordID of all the sentence co-occurrences of a given word.
 * In SQL this is the following query:
 *
 * select 	w1.w1_id, w1.w2_id, w1.freq, w1.sig
 * from 	co_s w1
 * where 	w1.w1_id in
 *	(select 	w2.w2_id
 *	 from 		co_s w2
 *	 where 		w2.w1_id=137 )
 *
 */

public class Query2_SQL_Benchmark extends OrientDBBenchmark{

	@Override
	public void setUp() {
		String q2 = "select in from WORD where w_id = 137";
		prep_query2 = new OSQLSynchQuery<ODocument>(q2);

		super.setUp();
	}

	@Override
	public void run() {
		List<ODocument> result;
		result = orientdb.command(prep_query2).execute(startWordID);



		for (ODocument v : result) {
			log.info(v.toString());
		}
	}

	@Override
	public String getName() {
		return "OrientDB Query2 SQL";
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
