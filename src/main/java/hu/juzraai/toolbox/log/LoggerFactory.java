package hu.juzraai.toolbox.log;

import static hu.juzraai.toolbox.meta.DependencyConstants.SLF4J_IMPL;

import org.slf4j.Logger;

import hu.juzraai.toolbox.meta.Dependencies;

/**
 * This is simply a bridge to SLF4J's LoggerFactory, but with a dependency
 * helper initialization.
 *
 * @since 0.2.0
 * @author Zsolt Jurányi
 *
 * @see Dependencies
 *
 */
public class LoggerFactory {

	static {
		Dependencies.need(SLF4J_IMPL);
	}

	/**
	 * Calls org.slf4j.LoggerFactory.getLogger()
	 *
	 * @param c
	 *            The returned logger will be named after this class
	 * @return A logger implementation
	 */
	public static Logger getLogger(Class<?> c) {
		return org.slf4j.LoggerFactory.getLogger(c);
	}

}
