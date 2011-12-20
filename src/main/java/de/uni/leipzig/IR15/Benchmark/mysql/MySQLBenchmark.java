package de.uni.leipzig.IR15.Benchmark.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.uni.leipzig.IR15.Benchmark.Benchmark;
import de.uni.leipzig.IR15.Connectors.MySQLConnector;
import de.uni.leipzig.IR15.Support.Configuration;

public abstract class MySQLBenchmark extends Benchmark {

	private Connection mySQL;

	private int maxWordID;

	protected String query;

	protected PreparedStatement st;

	protected int start_WordID;

	private static Configuration mySQLConfig = Configuration
			.getInstance("mysql");

	protected int minOutDegree;

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
			while (rs.next()) { /* silence is golden */
			}
		} catch (SQLException ex) {
			log.error(ex.getMessage());
		}
	}

	/**
	 * creates random id until a matching db entity is found
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
