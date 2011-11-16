package de.uni.leipzig.IR15.Importer;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;

public class testorient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Importer OrientImport = new OrientDBImporter();
		
		OrientImport.setUp();
		OrientImport.importData();
		
		//OGraphDatabase orientdb = OrientImport.getDB();
		
		/*
		List<ODocument> result = database.query(new OSQLSynchQuery<ODocument>("select from GraphVehicle"));
		  Assert.assertEquals(result.size(), 2);
		  for (ODocument v : result) {
		    Assert.assertTrue(v.getSchemaClass().isSubClassOf(vehicleClass));
		  }
*/
		
		
		
		
		
		OrientImport.tearDown();
		
	}

}
