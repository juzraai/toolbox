package hu.juzraai.toolbox.log;

import hu.juzraai.toolbox.meta.Dependencies;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

import static hu.juzraai.toolbox.meta.DependencyConstants.SLF4J_IMPL;

/**
 * This is simply a bridge to SLF4J's LoggerFactory, but with a dependency
 * helper initialization.
 *
 * @author Zsolt Jurányi
 * @see Dependencies
 * @since 0.2.0
 */
public class LoggerFactory {

	static {
		Dependencies.need(SLF4J_IMPL);
	}

	/**
	 * Calls org.slf4j.LoggerFactory.getLogger()
	 *
	 * @param c The returned logger will be named after this class
	 * @return A logger implementation
	 */
	@Nonnull
	public static Logger getLogger(@Nonnull Class<?> c) {
		return org.slf4j.LoggerFactory.getLogger(c);
	}

}
