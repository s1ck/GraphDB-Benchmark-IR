package de.uni.leipzig.IR15.Benchmark.mysql;


import java.sql.Connection;
import java.sql.PreparedStatement;
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
	
	protected PreparedStatement st;

	protected int start_WordID;

	private Random r = new Random();

	@Override
	public void setUp() {
		mySQL = MySQLConnector.getConnection();

		// get count		
		try {
			st = mySQL.prepareStatement("SELECT count(*) FROM words");
			ResultSet res = st.executeQuery();
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
	public void beforeRun() {
		try {
			st = mySQL.prepareStatement(query);
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {			
			ResultSet rs = st.executeQuery();
			while (rs.next()) { /* silence is golden */ }			
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}
	}	
	
	public int getRandomStartNode() {
		return r.nextInt(n);
	}
}
