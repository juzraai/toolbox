package hu.juzraai.toolbox.data;

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
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static hu.juzraai.toolbox.meta.DependencyConstants.*;

/**
 * Utility class which aims to ease using of ORMLite by adding generic methods
 * and helper functions. It's implemented as a {@link Closeable} so you can use
 * it in a try-with-resources block.
 *
 * @author Zsolt Jurányi
 * @since 16.07
 */
public class OrmLiteDatabase implements Closeable {

	private static final Logger L = LoggerFactory.getLogger(OrmLiteDatabase.class);

	static {
		Dependencies.need(PERSISTENCE_API, ORMLITE_CORE, ORMLITE_JDBC);
	}

	private final ConnectionString connectionString;
	private final ConnectionSource connectionSource;

	/**
	 * Creates a new instance.
	 *
	 * @param connectionString The connection string
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
		L.debug("Opening database connection - {}", connectionString);
		JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(connectionString.toString(), username, password);
		connectionSource.setTestBeforeGet(true);
		return new OrmLiteDatabase(connectionString, connectionSource);
	}

	/**
	 * Calls <code>closeQuietly</code> method of the {@link ConnectionSource}
	 * object.
	 *
	 * @throws IOException
	 * @see ConnectionSource
	 */
	@Override
	public void close() throws IOException {
		L.debug("Closing database connection - {}", connectionString);
		this.connectionSource.closeQuietly();
	}

	/**
	 * Creates the tables (if they not exist) using {@link TableUtils}. If the
	 * database is MySQL it also processes {@link Longtext} annotations and
	 * modifies annotated columns to <code>LONGTEXT</code>.
	 *
	 * @param tableClasses Table classes
	 * @throws SQLException
	 * @see Longtext
	 * @see TableUtils
	 * @see #modifyMySqlColumn(Class, String, String)
	 */
	public void createTables(@Nonnull Class<?>... tableClasses) throws SQLException {
		L.info("Creating tables if necessary");
		for (Class<?> tableClass : tableClasses) {
			L.debug("Creating table if necessary for class: {}", tableClass.getName());
			TableUtils.createTableIfNotExists(connectionSource, tableClass);

			// LONGTEXT and CHANGE COLUMN are MySQL specific:
			if (connectionString instanceof MySqlConnectionString) {
				for (Field f : tableClass.getDeclaredFields()) {
					if (isDatabaseField(f) && f.isAnnotationPresent(Longtext.class)) {
						L.debug("Modifying field to LONGTEXT: {}.{}", tableClass.getSimpleName(), f.getName());
						String fn = fetchColumnName(f);
						modifyMySqlColumn(tableClass, fn, "LONGTEXT NULL DEFAULT NULL");
						// TODO test longtext
					}
				}
			} else {
				L.debug("Annotation processing skipped, our database isn't MySQL");
			}
			// TODO maybe we can create a MySqlOrmLiteDatabase version, hmm?
		}
	}

	/**
	 * Returns with the {@link Dao} object for the given table class.
	 *
	 * @param tableClass Table class to fetch DAO for
	 * @param <T>        Table class
	 * @return DAO object for the given table class
	 * @throws SQLException
	 * @see DaoManager
	 */
	public <T> Dao<T, ?> dao(@Nonnull Class<T> tableClass) throws SQLException {
		return DaoManager.createDao(connectionSource, tableClass);
	}

	/**
	 * Fetches a record by ID from the given table.
	 *
	 * @param tableClass Table class
	 * @param id         ID
	 * @param <T>        Table class
	 * @param <I>        ID type
	 * @return The record from the given table having the given ID - or
	 * <code>null</code> if there's no such record in the table
	 * @throws SQLException
	 * @see DaoManager
	 */
	@CheckForNull
	public <T extends Identifiable<I>, I> T fetch(@Nonnull Class<T> tableClass, @Nonnull I id) throws SQLException {
		L.debug("Fetching ID '{}' using table class: {}", id.toString(), tableClass.getSimpleName());
		Dao<T, I> dao = DaoManager.createDao(connectionSource, tableClass);
		return dao.queryForId(id);
	}

	/**
	 * Determines the database column name for the given {@link Field}. Returns
	 * the column name specified using {@link DatabaseField} or {@link Column}
	 * annotation, or returns the field name if none of them presents.
	 *
	 * @param f Field to be examined
	 * @return Database column name for the given field
	 * @see Column
	 * @see DatabaseField
	 */
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

	/**
	 * Determines the database table name for a given class. Returns the table
	 * name specified using {@link DatabaseTable} or {@link Table} annotation,
	 * or returns the simple class name lower cased if none of them presents.
	 *
	 * @param c Class to be examined
	 * @return Database table name for the given class
	 * @see DatabaseTable
	 * @see Table
	 */
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

	/**
	 * @return The connection source
	 */
	@Nonnull
	public ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	/**
	 * @return The connection string
	 */
	public ConnectionString getConnectionString() {
		return connectionString;
	}

	/**
	 * Determines whether the given {@link Field} is a configured database field
	 * or not. A field is a database field if it's annotated with {@link
	 * DatabaseField} or {@link Column}.
	 *
	 * @param f Field to be tested
	 * @return Whether the given field is a configured database field
	 * @see Column
	 * @see DatabaseField
	 */
	private boolean isDatabaseField(@Nonnull Field f) {
		return f.isAnnotationPresent(DatabaseField.class) || f.isAnnotationPresent(Column.class);
	}

	/**
	 * Runs an <code>ALTER TABLE ... CHANGE COLUMN</code> MySQL command with the
	 * given parameters. This method will throw {@link IllegalStateException} if
	 * the database is not MySQL.
	 *
	 * @param tableClass The table class used to get DAO object and determine
	 *                   database table name
	 * @param column     Name of database column which needs to be modified
	 * @param definition The new definition of the column
	 * @throws SQLException
	 * @see #fetchTableName(Class)
	 */
	public void modifyMySqlColumn(@Nonnull Class<?> tableClass, @Nonnull String column, @Nonnull String definition) throws SQLException {
		Check.state(connectionString instanceof MySqlConnectionString, "connectionString must be instanceof MySqlConnectionString");
		String tn = fetchTableName(tableClass);
		String sql = String.format("ALTER TABLE `%s` CHANGE COLUMN `%s` `%s` %s ;", tn, column, column, definition);
		L.debug("Using table class {} to execute raw SQL: {}", tableClass.getSimpleName(), sql);
		DaoManager.createDao(connectionSource, tableClass).executeRawNoArgs(sql);
	}

	/**
	 * Fetches the appropriate DAO object then stores the specified entity using
	 * <code>Dao.createOrUpdate</code>.
	 *
	 * @param entity Entity to be stored
	 * @param <T>    Table class
	 * @throws SQLException
	 * @see #dao
	 * @see Dao
	 */
	public <T> void store(@Nonnull T entity) throws SQLException {
		L.debug("Storing ID '{}' using table class: {}", entity.getClass().getSimpleName());
		Dao<T, ?> dao = dao((Class<T>) entity.getClass());
		dao.createOrUpdate(entity);
	}

}
