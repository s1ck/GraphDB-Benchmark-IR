package de.uni.leipzig.IR15.Benchmark.suites;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Benchmark.ImportBenchmark;
import de.uni.leipzig.IR15.Importer.OrientDBImporter;

public class OrientBenchmarkSuite extends AbstractBenchmarkSuite {

	public static Logger log = Logger.getLogger(OrientBenchmarkSuite.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		List<Benchmark> benchmarks = new ArrayList<Benchmark>();
		
		Benchmark OrientDBImportBench = new ImportBenchmark(new OrientDBImporter());
		OrientDBImportBench.setWarmups(0);
		OrientDBImportBench.setRuns(1);
		// benchmarks.add(OrientDBImportBench);
		
		Benchmark getWordByID_Query = new de.uni.leipzig.IR15.Benchmark.orientdb.GetOneWordByID_Benchmark();
		getWordByID_Query.setWarmups(10);
		getWordByID_Query.setRuns(1000);
		// benchmarks.add(getWordByID_Query);
		
		Benchmark Query1_SQL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query1_SQL_Benchmark();
		Query1_SQL.setWarmups(5);
		Query1_SQL.setRuns(5);
		// benchmarks.add(Query1_SQL);
		
		Benchmark Query2_SQL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query2_SQL_Benchmark();
		Query2_SQL.setWarmups(0);
		Query2_SQL.setRuns(1);
		// benchmarks.add(Query2_SQL);
		
		Benchmark Query1_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query1_LowL_Benchmark();
		Query1_LL.setWarmups(0);
		Query1_LL.setRuns(1);
		// benchmarks.add(Query1_LL);
		
		Benchmark Query2_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query2_LowL_Benchmark();
		Query2_LL.setWarmups(0);
		Query2_LL.setRuns(1);
		// benchmarks.add(Query2_LL);
		
		Benchmark Query3_LL = new de.uni.leipzig.IR15.Benchmark.orientdb.Query3_LowL_Benchmark();
		Query3_LL.setWarmups(10);
		Query3_LL.setRuns(1000);
		benchmarks.add(Query3_LL);
		
		runBenchmarks(benchmarks, true);
		
	}
	
}


/*
for(int zzz = 0; zzz < 100000; zzz++)
{
	Random r2 = new Random(zzz);
	int startWordID = r2.nextInt(39334);
	if (startWordID == 137)
			log.info(zzz);
}
*/