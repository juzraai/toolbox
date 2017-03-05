package hu.juzraai.toolbox.log;

import org.apache.log4j.*;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.apache.log4j.Logger.getLogger;
import static org.apache.log4j.Logger.getRootLogger;

/**
 * Utility class for configuring Log4j logger - the appenders and levels, via
 * simple static methods.
 *
 * @author Zsolt Jurányi
 * @since 16.08
 */
public class LoggerSetup {

	private static final String PATTERN_LAYOUT = "%d{yyyy-MM-dd HH:mm:ss} [%-5p] [%-10t] %c{1}:%L >> %m%n";

	/**
	 * Sets the log level for the root logger.
	 *
	 * @param level Log level to be set for root logger
	 */
	public static void level(@Nonnull Level level) {
		getRootLogger().setLevel(level);
	}

	/**
	 * Sets the log level for the given class' logger.
	 *
	 * @param clazz Logger of this class will be modified
	 * @param level Log level to be set for the logger of the specified class
	 */
	public static void level(@Nonnull Class<?> clazz, @Nonnull Level level) {
		getLogger(clazz).setLevel(level);
	}

	/**
	 * Sets the log level for the logger identified by the given name.
	 *
	 * @param name  Name of the logger which has to be modified
	 * @param level Log level to be set for the logger identified by the given
	 *              name
	 */
	public static void level(@Nonnull String name, @Nonnull Level level) {
		getLogger(name).setLevel(level);
	}

	/**
	 * Simply calls {@link #output(boolean, String)} with arguments
	 * <code>(true,null)</code>.
	 */
	public static void outputOnlyToConsole() {
		output(true, null);
	}

	/**
	 * Removes all appenders, then adds a {@link ConsoleAppender} if
	 * <code>console</code> is <code>true</code>, and a {@link FileAppender} if
	 * <code>filename</code> is not <code>null</code>.
	 *
	 * @param console  Whether to add a console appender or not
	 * @param filename The filename to be used by the file appender or
	 *                 <code>null</code> to skip adding file appender
	 */
	public static void output(boolean console, String filename) {

		// reset
		getRootLogger().removeAllAppenders();

		// layout
		Layout layout = new PatternLayout(PATTERN_LAYOUT);

		// console
		if (console) {
			getRootLogger().addAppender(new ConsoleAppender(layout, "System.err"));
		}

		// file
		if (null != filename) {
			try {
				getRootLogger().addAppender(new FileAppender(layout, filename, true, true, 1024 * 8));
			} catch (IOException e) {
				System.err.println("Failed to construct FileAppender: " + e.getMessage());
			}
		}
	}

}
