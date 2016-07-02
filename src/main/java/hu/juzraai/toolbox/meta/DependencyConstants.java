package hu.juzraai.toolbox.meta;

import hu.juzraai.toolbox.meta.Dependencies.Dependency;

/**
 * Predefined <code>Dependency</code> constants to be used with
 * <code>Dependencies</code>
 *
 * @author Zsolt Jurányi
 * @see Dependencies
 * @since 0.2.0
 */
public class DependencyConstants {

	public static final Dependency JSOUP = new Dependency("org.jsoup.Jsoup", "org.jsoup", "jsoup", "1.8.3");
	public static final Dependency MYSQL = new Dependency("com.mysql.jdbc.Driver", "mysql", "mysql-connector", "5.1.36");
	public static final Dependency ORMLITE_CORE = new Dependency("com.j256.ormlite.dao.DaoManager", "com.j256.ormlite", "ormlite-core", "4.48");
	public static final Dependency ORMLITE_JDBC = new Dependency("com.j256.ormlite.jdbc.JdbcPooledConnectionSource", "com.j256.ormlite", "ormlite-jdbc", "4.48");
	public static final Dependency PERSISTENCE_API = new Dependency("javax.persistence.Table", "javax.persistence", "persistence-api", "1.0.2");
	public static final Dependency SLF4J_IMPL = new Dependency("org.slf4j.impl.StaticLoggerBinder", "org.slf4j",
			"slf4j-log4j12 <!-- or any slf4j-* implementation -->", "1.7.21");
	public static final Dependency SQLITE = new Dependency("org.sqlite.JDBC", "org.xerial", "sqlite-jdbc", "3.7.2");

	// sqlite-jdbc version set to 3.7.2 because this issue:
	// http://stackoverflow.com/questions/31041045/warning-you-seem-to-not-be-using-the-xerial-sqlite-driver

}
