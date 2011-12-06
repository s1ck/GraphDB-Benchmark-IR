package de.uni.leipzig.IR15.Benchmark.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.MySQLConnector;

public abstract class MySQLBenchmark extends Benchmark {
	
	private Connection mySQL;

	private int n;
	
	protected String query;
	
	protected int start_WordID;
	
	private Random r = new Random();

	@Override
	public void setUp() {
		mySQL = MySQLConnector.getConnection();

		// get count
		String q = "SELECT count(*) FROM words";
		try {
			Statement st = mySQL.createStatement();
			ResultSet res = st.executeQuery(q);
			res.next();
			n = res.getInt(1);
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}
		
		start_WordID = getRandomStartNode();
	}

	@Override
	public void tearDown() {
		MySQLConnector.destroyConnection();
	}
	
	@Override
	public void reset() {
		start_WordID = getRandomStartNode();
	}
	
	@Override
	public void run() {
		try
		{
			Statement st = mySQL.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) { /* silence is golden */ }
		} catch(SQLException ex)
		{
			log.error(ex.getMessage());
		}
	}
	
	public int getRandomStartNode() {
		return  r.nextInt(n);
	}
}
