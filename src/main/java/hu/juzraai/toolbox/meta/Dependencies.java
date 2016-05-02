package hu.juzraai.toolbox.meta;

import hu.juzraai.toolbox.test.Check;

/**
 * Toolbox has many dependencies, but almost all of them are marked 'optional'
 * to keep the JAR thin. However, when you want to use a module of Toolbox which
 * depends on 'A', you have to declare 'A' too as a dependency.<br/>
 * <br/>
 *
 * I coded into modules of Toolbox that when they are called at the first time,
 * they look for the required dependencies and print out the appropriate Maven
 * dependency XML code when a needed class cannot be found, to help you solve
 * the problem.<br/>
 * <br/>
 *
 * This class is responsible for the checking and printing, and I use them in
 * Toolbox modules this way:
 *
 * <pre>
 * public class A {
 * 	static {
 * 		Dependencies.need(new Dependency("class1", "group1", "id1", "version1"),
 * 				new Dependency("class2", "group2", "id2", "version2"));
 * 	}
 * }
 * </pre>
 *
 * @since 0.2.0
 * @author Zsolt Jurányi
 *
 * @see DependencyConstants
 *
 */
public class Dependencies {

	/**
	 * Simple POJO to describe a Maven dependency which is needed by a class.
	 *
	 * @since 0.2.0
	 * @author Zsolt Jurányi
	 *
	 */
	public static final class Dependency {

		public final String c, g, a, v;

		/**
		 * Creates a new instance.
		 *
		 * @param c
		 *            Class name which needs this dependency
		 * @param g
		 *            groupId
		 * @param a
		 *            artifactId
		 * @param v
		 *            version
		 */
		public Dependency(String c, String g, String a, String v) {
			this.c = c;
			this.g = g;
			this.a = a;
			this.v = v;
		}

		/**
		 * Generates a <code>&lt;dependency&gt;</code> code from the field
		 * values.
		 */
		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append("    <dependency>\n");
			s.append("        <groupId>").append(g).append("</groupId>\n");
			s.append("        <artifactId>").append(a).append("</artifactId>\n");
			s.append("        <version>").append(v).append("</version>\n");
			s.append("    </dependency>\n");
			return s.toString();
		}
	}

	/**
	 * Calls <code>Class.forName(dependency.c)</code> for every
	 * <code>Dependency</code> argument, catches
	 * <code>ClassNotFoundException</code> and prints out required dependencies
	 * in a form you can copy-paste it into your Maven POM file. If there were
	 * at least one missing dependency, it calls a <code>System.exit(1)</code>
	 * at the end, to terminate the application.
	 *
	 * @param dependencies
	 *            The dependencies to check.
	 */
	public static void need(Dependency... dependencies) {
		Check.notNull(dependencies, "dependencies must not be null");
		boolean h = false;
		StringBuilder s = new StringBuilder();
		s.append("*** DEPENDENCIES REQUIRED:\n\n");
		for (Dependency d : dependencies) {
			try {
				Class.forName(Check.notNull(d, "dependency must not be null").c);
			} catch (ClassNotFoundException e) {
				h = true;
				s.append(d.toString());
			}
		}
		if (h) {
			System.err.println(s.toString());
			System.exit(1);
		}
	}

}
