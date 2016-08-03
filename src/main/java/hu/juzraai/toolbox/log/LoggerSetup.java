package hu.juzraai.toolbox.log;

import org.apache.log4j.*;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.apache.log4j.Logger.getLogger;
import static org.apache.log4j.Logger.getRootLogger;

/**
 * @author Zsolt Jurányi
 * @since 16.08
 */
public class LoggerSetup { // TODO doc, since

	private static final String PATTERN_LAYOUT = "%d{yyyy-MM-dd HH:mm:ss} [%-5p] [%-10t] %c{1}:%L >> %m%n";

	public static void level(@Nonnull Level level) {
		getRootLogger().setLevel(level);
	}

	public static void level(@Nonnull Class<?> clazz, @Nonnull Level level) {
		getLogger(clazz).setLevel(level);
	}

	public static void level(@Nonnull String name, @Nonnull Level level) {
		getLogger(name).setLevel(level);
	}

	public static void output(boolean console, @Nonnull String filename) {

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
