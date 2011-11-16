package de.uni.leipzig.IR15.Benchmark;

import de.uni.leipzig.IR15.Importer.Importer;

public class ImportBenchmark extends Benchmark {

	private Importer importer;
	
	public ImportBenchmark(Importer importer) {
		this.importer = importer;
	}
	
	@Override
	public void run() {	
		log.info(String.format("Starting: %s", getName()));
		importer.setUp();
		
		startTimer();
		importer.importData();
		long duration = stopTimer();
		
		importer.tearDown();
		
		log.debug(String.format("Finished: %s Took %d [ms]", getName(), duration));
	}

	@Override
	public String getName() {
		return String.format("Import Benchmark using importer: %s", importer.toString());
	}

}
