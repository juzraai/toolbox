package hu.juzraai.toolbox.jdbc;

import hu.juzraai.toolbox.meta.Dependencies;

import javax.annotation.Nonnull;
import java.io.File;

import static hu.juzraai.toolbox.meta.DependencyConstants.SQLITE;

/**
 * @author Zsolt Jurányi
 * @since 16.07
 */
public class SqliteConnectionString extends ConnectionString {
	// TODO test
	// TODO doc (null == file means in-memory db)

	static {
		Dependencies.need(SQLITE);
	}

	private final File databaseFile;

	public SqliteConnectionString(File databaseFile) {
		this.databaseFile = databaseFile;
	}

	private SqliteConnectionString(Builder builder) {
		databaseFile = builder.databaseFile;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@Override
	@Nonnull
	protected String generateConnectionString() {
		return String.format("jdbc:sqlite:%s", null == databaseFile ? "" : databaseFile.getPath());
	}

	public File getDatabaseFile() {
		return databaseFile;
	}

	/**
	 * {@code SqliteConnectionString} builder static inner class.
	 */
	public static final class Builder {
		private File databaseFile;

		private Builder() {
		}

		/**
		 * Returns a {@code SqliteConnectionString} built from the parameters
		 * previously set.
		 *
		 * @return a {@code SqliteConnectionString} built with parameters of
		 * this {@code SqliteConnectionString.Builder}
		 */
		@Nonnull
		public SqliteConnectionString build() {
			return new SqliteConnectionString(this);
		}

		/**
		 * Sets the {@code databaseFile} and returns a reference to this Builder
		 * so that the methods can be chained together.
		 *
		 * @param val the {@code databaseFile} to set
		 * @return a reference to this Builder
		 */
		@Nonnull
		public Builder databaseFile(@Nonnull File val) {
			databaseFile = val;
			return this;
		}
	}
}
