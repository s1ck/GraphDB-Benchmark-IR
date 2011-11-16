package de.uni.leipzig.IR15.Importer;

import java.io.File;

import de.uni.leipzig.IR15.Support.Configuration;

public abstract class Importer {
	protected static Configuration graphConfiguration;

	protected void reset() {
		File directory = new File(graphConfiguration.getPropertyAsString("location"));
		if (directory.exists()) {
			deleteGraphDatabaseStorageDirectory(directory);
		}
	}

	public abstract void setUp();
	public abstract void tearDown();
	public abstract void importData();
	
	private void deleteGraphDatabaseStorageDirectory(File path) {
		for (File file : path.listFiles()) {
			if (file.isDirectory())
				deleteGraphDatabaseStorageDirectory(file);
			file.delete();
	    }
	    path.delete();
	}
}
