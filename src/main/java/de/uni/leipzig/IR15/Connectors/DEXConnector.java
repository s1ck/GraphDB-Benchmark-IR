package de.uni.leipzig.IR15.Connectors;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.sparsity.dex.gdb.Database;
import com.sparsity.dex.gdb.Dex;
import com.sparsity.dex.gdb.DexConfig;

import de.uni.leipzig.IR15.Support.Configuration;

/**
 * Class to handle connection to the dex database.
 *
 * @author IR-Team
 *
 */
public class DEXConnector {
	private static Logger log = Logger.getLogger(Neo4JConnector.class);
	private static Database dex;
	private static Dex dexConnector;

	/**
	 * Create and return a connection.
	 *
	 * @return database connection
	 */
	public static Database getConnection() {
		Configuration config = Configuration.getInstance("dex");

		DexConfig dexConfig = new DexConfig();
		dexConfig.setLicense(config.getPropertyAsString("license"));
		//dexConfig.setLogLevel(LogLevel.Debug);
		dexConnector = new Dex(dexConfig);

		try {
			File location = new File(config.getPropertyAsString("location"));
			if (location.exists()) {
				dex = dexConnector.open(config.getPropertyAsString("location"), false);
			} else {
				dex = dexConnector.create(config.getPropertyAsString("location"), "dex");
			}
			log.info("Create connection successful");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("Create connection failed");
		}

		return dex;
	}

	/**
	 * Destroy a database connection.
	 */
	public static void destroyConnection() {
		try {
			dex.close();
		    dexConnector.close();
			log.info("Destroy connection successful");
		} catch (Exception e) {
			log.error("Destroy connection failed");
			e.printStackTrace();
		}
	}
}
