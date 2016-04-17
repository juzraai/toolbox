package hu.juzraai.toolbox.test;

/**
 * Simple value validator methods. The idea taken from Guava's Preconditions but
 * I want to use these functions without including a 2 MB dependency. :)
 *
 * @since 0.2.0
 * @author Zsolt Jurányi
 *
 */
public class Check {

	/**
	 * If the first argument is <code>false</code>, throws
	 * <code>IllegalArgumentException</code> with a message given in the second
	 * argument.
	 *
	 * @param b
	 *            Expression to be tested
	 * @param m
	 *            Exception message
	 */
	public static void argument(boolean b, String m) {
		if (!b) {
			throw new IllegalArgumentException(m);
		}
	}

	/**
	 * If the first argument is <code>null</code>, throws
	 * <code>NullPointerException</code> with a message given in the second
	 * argument, otherwise returns the passed object.
	 *
	 * @param o
	 *            Reference to be tested
	 * @param m
	 *            Exception message
	 * @param <T>
	 *            Type of the tested reference
	 * @return The examined object
	 */
	public static <T> T notNull(T o, String m) {
		if (null == o) {
			throw new NullPointerException(m);
		}
		return o;
	}

	/**
	 * If the first argument is <code>false</code>, throws
	 * <code>IllegalStateException</code> with a message given in the second
	 * argument.
	 *
	 * @param b
	 *            Expression to be tested
	 * @param m
	 *            Exception message
	 */
	public static void state(boolean b, String m) {
		if (!b) {
			throw new IllegalStateException(m);
		}
	}
}
