package hu.juzraai.toolbox.jdbc;

import javax.annotation.Nonnull;

/**
 * @author Zsolt Jurányi
 * @since 16.07
 */
public abstract class ConnectionString {
	// TODO doc

	private String connectionString;

	public static MySqlConnectionString.Builder MYSQL() {
		return MySqlConnectionString.newBuilder();
	}

	public static SqliteConnectionString.Builder SQLITE() {
		return SqliteConnectionString.newBuilder();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ConnectionString)) return false;
		ConnectionString connectionString = (ConnectionString) o;
		return toString().equals(connectionString.toString());
	}

	@Nonnull
	protected abstract String generateConnectionString();

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	@Nonnull
	public String toString() {
		if (null == connectionString) {
			connectionString = generateConnectionString();
		}
		return connectionString;
	}

}
