package hu.juzraai.toolbox.data;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import hu.juzraai.toolbox.jdbc.ConnectionString;
import hu.juzraai.toolbox.meta.Dependencies;

import javax.annotation.Nonnull;
import java.sql.SQLException;

import static hu.juzraai.toolbox.meta.DependencyConstants.ORMLITE_CORE;
import static hu.juzraai.toolbox.meta.DependencyConstants.ORMLITE_JDBC;

/**
 * @author Zsolt Jurányi
 */
public class OrmLiteDatabase {

	static {
		Dependencies.need(ORMLITE_CORE, ORMLITE_JDBC);
	}

	private final ConnectionSource connectionSource;

	private OrmLiteDatabase(@Nonnull ConnectionSource connectionSource) {
		this.connectionSource = connectionSource;
	}

	@Nonnull
	public static OrmLiteDatabase build(@Nonnull ConnectionString connectionString, String username, String password) throws SQLException {
		JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(connectionString.toString(), username, password);
		connectionSource.setTestBeforeGet(true);
		return new OrmLiteDatabase(connectionSource); // TODO test w/ null user+pass (in memory sqlite?)
	}
}
