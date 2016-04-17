package hu.juzraai.toolbox.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.slf4j.Logger;

import hu.juzraai.toolbox.log.LoggerFactory;
import hu.juzraai.toolbox.test.Check;

/**
 * Methods to set the proxy to want to use in your application.
 *
 * @since 0.1.0
 * @author http://stackoverflow.com/questions/1626549/authenticated-http-proxy-
 *         with-java
 * @author Zsolt Jurányi
 *
 */
public class Proxy {

	private static final Logger L = LoggerFactory.getLogger(Proxy.class);

	/**
	 * Sets <code>http.proxyHost</code> and <code>http.proxyPort</code> system
	 * properties.
	 *
	 * @param host
	 *            Proxy host (domain or IP)
	 * @param port
	 *            Proxy port
	 */
	public static void use(final String host, final int port) {
		Check.notNull(host, "host must not be null");
		L.info("Using proxy: {}:{}", host, port);
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", Integer.toString(port));
	}

	/**
	 * Sets <code>http.proxyHost</code> and <code>http.proxyPort</code> system
	 * properties and also sets up an <code>Authenticator</code>.
	 *
	 * @param host
	 *            Proxy host (domain or IP)
	 * @param port
	 *            Proxy port
	 * @param user
	 *            Proxy user
	 * @param pass
	 *            Proxy password
	 */
	public static void use(final String host, final int port, final String user, final String pass) {
		Check.notNull(user, "user must not be null");
		Check.notNull(pass, "pass must not be null");
		use(host, port);
		L.info("Using proxy authentication: {}:***", user);
		Authenticator.setDefault(new Authenticator() {

			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication(user, pass.toCharArray()));
			}
		});
	}

}
