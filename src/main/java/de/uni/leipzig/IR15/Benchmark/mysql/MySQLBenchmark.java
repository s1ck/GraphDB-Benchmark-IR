package de.uni.leipzig.IR15.Benchmark.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.MySQLConnector;
import de.uni.leipzig.IR15.Support.Configuration;

/**
 * Abstract Base Class for all benchmarks running on sql database. It holds a
 * reference to the database it also cares about generating random (and
 * existing) node ids.
 * 
 * @author Martin 's1ck' Junghanns
 * 
 */
public abstract class MySQLBenchmark extends Benchmark {

	private Connection mySQL;
	private int maxWordID;
	protected String query;
	protected PreparedStatement st;
	protected int start_WordID;
	private static Configuration mySQLConfig = Configuration
			.getInstance("mysql");

	protected int minOutDegree;

	/**
	 * Setup the database connection.
	 */
	@Override
	public void setUp() {
		mySQL = MySQLConnector.getConnection();
		minOutDegree = mySQLConfig.getPropertyAsInteger("min_outdegree");
		// get count
		try {
			st = mySQL
					.prepareStatement("select w_id from words order by w_id desc limit 1");
			ResultSet res = st.executeQuery();
			res.next();
			maxWordID = res.getInt(1);
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}
	}

	/**
	 * Cleanup after benchmarking.
	 */
	@Override
	public void tearDown() {
		MySQLConnector.destroyConnection();
	}

	/**
	 * Prepare the statement before each run.
	 */
	@Override
	public void beforeRun() {
		try {
			st = mySQL.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterRun() {
	}

	/**
	 * Run and execute the prepared statement.
	 */
	@Override
	public void run() {
		try {
			ResultSet rs = st.executeQuery();
			while (rs.next()) { /* silence is golden */
			}
			rs.close();
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}
	}

	/**
	 * Returns a random start node.
	 * 
	 * Loops with a random w_id until a existing entity is found.
	 * 
	 * @return
	 */
	public int getRandomStartNode(int treshold) {
		int id = 0;
		String q;
		boolean found = false;

		PreparedStatement statement;
		ResultSet result;

		while (!found) {
			id = r.nextInt(maxWordID);
			q = "SELECT count(*) FROM words WHERE w_id = " + id;
			try {
				statement = mySQL.prepareStatement(q);
				result = statement.executeQuery();
				result.next();
				if (result.getInt(1) > 0) {
					// select out degree of the found node
					result = statement.executeQuery(String.format(
							"select count(w1_id) from co_s where w1_id = %d",
							id));
					result.next();
					if (result.getInt(1) >= treshold) {
						found = true;
					}
				}
			} catch (SQLException ex) {
				log.error(ex.getMessage());
			}
		}
		return id;
	}
}