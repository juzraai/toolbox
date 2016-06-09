package hu.juzraai.toolbox.jdbc;

/**
 * @author Zsolt Jurányi
 * @since 16.07
 */
public abstract class ConnectionString { // TODO doc

	public static MySqlConnectionString.Builder MYSQL() {
		return MySqlConnectionString.newBuilder();
	}
}
