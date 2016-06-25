package hu.juzraai.toolbox.jdbc;

import hu.juzraai.toolbox.meta.Dependencies;

import javax.annotation.Nonnull;

import static hu.juzraai.toolbox.meta.DependencyConstants.MYSQL;

/**
 * @author Zsolt Jurányi
 * @since 16.07
 */
public class MySqlConnectionString extends ConnectionString {
	// TODO test (getters, default values, generated cs)
	// TODO doc

	static {
		Dependencies.need(MYSQL);
	}

	private final String host;
	private final int port;
	private final String schema;
	private final boolean utf8;

	private MySqlConnectionString(Builder builder) {
		host = builder.host;
		port = builder.port;
		schema = builder.schema;
		utf8 = builder.utf8;
	}

	@Nonnull
	public static Builder newBuilder() {
		return new Builder();
	}

	@Override
	@Nonnull
	protected String generateConnectionString() {
		return String.format("jdbc:mysql://%s/%s%s", host, schema, utf8 ? "?useUnicode=true&characterEncoding=utf-8" : "");
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getSchema() {
		return schema;
	}

	public boolean isUtf8() {
		return utf8;
	}

	/**
	 * {@code MySqlConnectionString} builder static inner class.
	 */
	public static final class Builder {
		private String host = "localhost";
		private int port = 3306;
		private String schema = "test";
		private boolean utf8 = true;

		private Builder() {
		}

		/**
		 * Returns a {@code MySqlConnectionString} built from the parameters
		 * previously set.
		 *
		 * @return a {@code MySqlConnectionString} built with parameters of this
		 * {@code MySqlConnectionString.Builder}
		 */
		@Nonnull
		public MySqlConnectionString build() {
			return new MySqlConnectionString(this);
		}

		/**
		 * Sets the {@code host} and returns a reference to this Builder so that
		 * the methods can be chained together.
		 *
		 * @param val the {@code host} to set
		 * @return a reference to this Builder
		 */
		@Nonnull
		public Builder host(@Nonnull String val) {
			host = val;
			return this;
		}

		/**
		 * Sets the {@code port} and returns a reference to this Builder so that
		 * the methods can be chained together.
		 *
		 * @param val the {@code port} to set
		 * @return a reference to this Builder
		 */
		@Nonnull
		public Builder port(int val) {
			port = val;
			return this;
		}

		/**
		 * Sets the {@code schema} and returns a reference to this Builder so
		 * that the methods can be chained together.
		 *
		 * @param val the {@code schema} to set
		 * @return a reference to this Builder
		 */
		@Nonnull
		public Builder schema(@Nonnull String val) {
			schema = val;
			return this;
		}

		/**
		 * Sets the {@code utf8} and returns a reference to this Builder so that
		 * the methods can be chained together.
		 *
		 * @param val the {@code utf8} to set
		 * @return a reference to this Builder
		 */
		@Nonnull
		public Builder utf8(boolean val) {
			utf8 = val;
			return this;
		}
	}
}
