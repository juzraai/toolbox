package hu.juzraai.toolbox.data;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import hu.juzraai.toolbox.jdbc.ConnectionString;
import hu.juzraai.toolbox.jdbc.MySqlConnectionString;
import hu.juzraai.toolbox.log.LoggerFactory;
import hu.juzraai.toolbox.meta.Dependencies;
import hu.juzraai.toolbox.test.Check;
import org.slf4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static hu.juzraai.toolbox.meta.DependencyConstants.*;

/**
 * Utility class which aims to ease using of ORMLite by adding generic methods
 * and helper functions.
 *
 * @author Zsolt Jurányi
 * @since 16.07
 */
public class OrmLiteDatabase {
	// TODO test
	// TODO doc

	private static final Logger L = LoggerFactory.getLogger(OrmLiteDatabase.class);

	static {
		Dependencies.need(PERSISTENCE_API, ORMLITE_CORE, ORMLITE_JDBC);
	}

	private final ConnectionString connectionString;
	private final ConnectionSource connectionSource;

	/**
	 * Creates a new instance.
	 *
	 * @param connectionString
	 * @param connectionSource The database connection
	 */
	private OrmLiteDatabase(ConnectionString connectionString, @Nonnull ConnectionSource connectionSource) {
		this.connectionString = connectionString;
		this.connectionSource = connectionSource;
	}

	/**
	 * Builds a new <code>OrmLiteDatabase</code> instance.
	 *
	 * @param connectionString JDBC connection string builder
	 * @param username         Username for database connection
	 * @param password         Password for database connection
	 * @return An <code>OrmLiteDatabase</code> instance initialized with a
	 * {@link JdbcPooledConnectionSource}
	 * @throws SQLException If any error occurs during database connection
	 */
	@Nonnull
	public static OrmLiteDatabase build(@Nonnull ConnectionString connectionString, String username, String password) throws SQLException {
		JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(connectionString.toString(), username, password);
		connectionSource.setTestBeforeGet(true);
		return new OrmLiteDatabase(connectionString, connectionSource);
	}

	public void createTables(@Nonnull Class<?>... tableClasses) throws SQLException {
		L.info("Creating tables if necessary");
		for (Class<?> tableClass : tableClasses) {
			L.trace("Creating table if necessary for class: {}", tableClass.getName());
			TableUtils.createTableIfNotExists(connectionSource, tableClass);

			// LONGTEXT and CHANGE COLUMN are MySQL specific:
			if (connectionString instanceof MySqlConnectionString) {
				for (Field f : tableClass.getDeclaredFields()) {
					if (isDatabaseField(f) && f.isAnnotationPresent(Longtext.class)) {
						String fn = fetchColumnName(f);
						modifyMySqlColumn(tableClass, fn, "LONGTEXT NULL DEFAULT NULL");
					}
				}
			} else {
				L.trace("Annotation processing skipped, our database isn't MySQL");
			}
		}
	}

	public <T> Dao<T, ?> dao(Class<T> c) throws SQLException {
		return DaoManager.createDao(connectionSource, c);
	}

	@CheckForNull
	public <T extends Identifiable<I>, I> T fetch(@Nonnull Class<T> tableClass, @Nonnull I id) throws SQLException {
		Dao<T, I> dao = DaoManager.createDao(connectionSource, tableClass);
		return dao.queryForId(id);
	}

	@Nonnull // TODO here we dont need Identifiable
	public <T extends Identifiable<I>, I> CloseableIterator<T> fetchAll(@Nonnull Class<T> tableClass) throws SQLException {
		Dao<T, I> dao = DaoManager.createDao(connectionSource, tableClass);
		return dao.iterator();
	}

	@Nonnull
	private String fetchColumnName(@Nonnull Field f) {

		// @DatabaseField

		DatabaseField df = f.getAnnotation(DatabaseField.class);
		if (null != df) {
			String fn = df.columnName();
			if (null != fn && !"".equals(fn)) {
				return fn;
			}
		}

		// @Column

		Column c = f.getAnnotation(Column.class);
		if (null != c) {
			String fn = c.name();
			if (null != fn && !"".equals(fn)) {
				return fn;
			}
		}

		return f.getName();
	}

	@Nonnull
	private String fetchTableName(@Nonnull Class<?> c) {

		// @DatabaseTable

		DatabaseTable dt = c.getAnnotation(DatabaseTable.class);
		if (null != dt) {
			String tn = dt.tableName();
			if (null != tn && !"".equals(tn)) {
				return tn;
			}
		}

		// @Table

		Table t = c.getAnnotation(Table.class);
		if (null != t) {
			String tn = t.name();
			if (null != tn && !"".equals(tn)) {
				return tn;
			}
		}

		return c.getSimpleName().toLowerCase();
	}

	@Nonnull
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	public ConnectionString getConnectionString() {
		return connectionString;
	}

	private boolean isDatabaseField(@Nonnull Field f) {
		return f.isAnnotationPresent(DatabaseField.class) || f.isAnnotationPresent(Column.class);
	}

	public void modifyMySqlColumn(@Nonnull Class<?> tableClass, @Nonnull String column, @Nonnull String definition) throws SQLException {
		Check.state(connectionString instanceof MySqlConnectionString, "connectionString must be instanceof MySqlConnectionString");
		String tn = fetchTableName(tableClass);
		DaoManager.createDao(connectionSource, tableClass).executeRawNoArgs(
				String.format("ALTER TABLE `%s` CHANGE COLUMN `%s` `%s` %s ;", tn, column, column, definition));
	}

	public <T> void store(@Nonnull T entity) throws SQLException {
		Dao<T, ?> dao = dao((Class<T>) entity.getClass());
		dao.createOrUpdate(entity);
	}

}
