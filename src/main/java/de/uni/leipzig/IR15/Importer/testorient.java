package de.uni.leipzig.IR15.Importer;

import java.util.List;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

public class testorient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<ODocument> result;
		OrientDBImporter OrientImport = new OrientDBImporter();
		
		//OrientImport.setUp();
		//OrientImport.importData();
		
		OGraphDatabase orientdb = OrientImport.onlyLoadDB();
		
		String q1 = "SELECT FROM OGraphVertex WHERE w_id = 4560";
		result = orientdb.query(new OSQLSynchQuery<ODocument>(q1));
		for (ODocument v : result)
			System.out.println(v.toString());
		/*
		String q2 = "SELECT FROM 5:1000 WHERE all()";
		result = orientdb.query(new OSQLSynchQuery<ODocument>(q2));
		for (ODocument v : result)
			System.out.println(v.toString());
		
		*/
		OrientImport.tearDown();
		
	}

}
