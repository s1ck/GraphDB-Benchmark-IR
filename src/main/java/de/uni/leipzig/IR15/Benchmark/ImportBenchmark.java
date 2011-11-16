package de.uni.leipzig.IR15.Benchmark;

import de.uni.leipzig.IR15.Importer.Importer;

public class ImportBenchmark extends Benchmark {

	private Importer importer;
	
	public ImportBenchmark(Importer importer) {
		this.importer = importer;
	}
	
	@Override
	public void run() {		
		importer.setUp();
		
		long start = System.currentTimeMillis();
		importer.importData();
		long diff = System.currentTimeMillis() - start;
		
		importer.tearDown();
		
		System.out.printf("took %d [ms]", diff);
	}

}
