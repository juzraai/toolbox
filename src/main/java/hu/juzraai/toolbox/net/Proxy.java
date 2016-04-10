package hu.juzraai.toolbox.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Methods to set the proxy to want to use in your application.
 *
 * @since 0.1.0
 * @author http://stackoverflow.com/questions/1626549/authenticated-http-proxy-with-java
 * @author Zsolt Jurányi
 *
 */
public class Proxy {

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
		use(host, port);
		Authenticator.setDefault(new Authenticator() {

			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication(user, pass.toCharArray()));
			}
		});
	}

}
