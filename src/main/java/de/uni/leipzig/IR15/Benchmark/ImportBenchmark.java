package de.uni.leipzig.IR15.Benchmark;

import de.uni.leipzig.IR15.Importer.Importer;

public class ImportBenchmark extends Benchmark {

	private Importer importer;
	
	public ImportBenchmark(Importer importer) {
		this.importer = importer;
	}
	
	@Override
	public void setUp() {
		importer.setUp();
	}
	
	@Override
	public void run() {			
		importer.importData();					
	}
	
	@Override
	public void tearDown() {
		// close connection
		importer.tearDown();		
		// remove data
		importer.reset();
	}
	
	@Override 
	public void reset() {
		
	}

	@Override
	public String getName() {
		return String.format("Importer [%s]", importer.getName());
	}

}
