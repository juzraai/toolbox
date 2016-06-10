package hu.juzraai.toolbox.jdbc;

/**
 * @author Zsolt Jurányi
 * @since 16.07
 */
public abstract class ConnectionString { // TODO doc

	private String connectionString;

	public static MySqlConnectionString.Builder MYSQL() {
		return MySqlConnectionString.newBuilder();
	}

	protected abstract String generateConnectionString();

	@Override
	public String toString() {
		if (null == connectionString) {
			connectionString = generateConnectionString();
		}
		return connectionString;
	}

}
