package hu.juzraai.toolbox.meta;

import hu.juzraai.toolbox.meta.Dependencies.Dependency;

/**
 * Predefined <code>Dependency</code> constants to be used with
 * <code>Dependencies</code>
 *
 * @since 0.2.0
 * @author Zsolt Jurányi
 *
 * @see Dependencies
 *
 */
public class DependencyConstants {

	public static final Dependency JSOUP = new Dependency("org.jsoup.Jsoup", "org.jsoup", "jsoup", "1.8.3");
	public static final Dependency SLF4J_IMPL = new Dependency("org.slf4j.impl.StaticLoggerBinder", "org.slf4j",
			"slf4j-log4j12 <!-- or any slf4j-* implementation -->", "1.7.21");

}