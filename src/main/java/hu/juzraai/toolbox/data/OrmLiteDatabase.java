package hu.juzraai.toolbox.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;
import hu.juzraai.toolbox.jdbc.ConnectionString;
import hu.juzraai.toolbox.meta.Dependencies;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static hu.juzraai.toolbox.meta.DependencyConstants.*;

/**
 * @author Zsolt Jurányi
 */
public class OrmLiteDatabase {
	// TODO since
	// TODO test
	// TODO doc

	static {
		Dependencies.need(PERSISTENCE_API, ORMLITE_CORE, ORMLITE_JDBC);
	}

	private final ConnectionSource connectionSource;

	private OrmLiteDatabase(@Nonnull ConnectionSource connectionSource) {
		this.connectionSource = connectionSource;
	}

	@Nonnull
	public static OrmLiteDatabase build(@Nonnull ConnectionString connectionString, String username, String password) throws SQLException {
		JdbcPooledConnectionSource connectionSource = new JdbcPooledConnectionSource(connectionString.toString(), username, password);
		connectionSource.setTestBeforeGet(true);
		return new OrmLiteDatabase(connectionSource);
	}

	public void createTables(@Nonnull Class<?>... tableClasses) throws SQLException {
		for (Class<?> tableClass : tableClasses) {
			TableUtils.createTableIfNotExists(connectionSource, tableClass);
			for (Field f : tableClass.getDeclaredFields()) {
				if (isDatabaseField(f) && f.isAnnotationPresent(Longtext.class)) {
					String fn = fetchColumnName(f);
					modifyColumnDefinition(tableClass, fn, "LONGTEXT NULL DEFAULT NULL");
				}
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

	private boolean isDatabaseField(@Nonnull Field f) {
		return f.isAnnotationPresent(DatabaseField.class) || f.isAnnotationPresent(Column.class);
	}

	public void modifyColumnDefinition(@Nonnull Class<?> tableClass, @Nonnull String column, @Nonnull String definition) throws SQLException {
		String tn = fetchTableName(tableClass);
		DaoManager.createDao(connectionSource, tableClass).executeRawNoArgs(
				String.format("ALTER TABLE `%s` CHANGE COLUMN `%s` `%s` %s ;", tn, column, column, definition));
	}

	public <T> void store(@Nonnull T entity) throws SQLException {
		Dao<T, ?> dao = dao((Class<T>) entity.getClass());
		dao.createOrUpdate(entity);
	}

}
