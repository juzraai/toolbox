package hu.juzraai.toolbox.net;

import hu.juzraai.toolbox.log.LoggerFactory;
import hu.juzraai.toolbox.test.Check;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * Methods to set the proxy to want to use in your application.
 *
 * @author http://stackoverflow.com/questions/1626549/authenticated-http-proxy-
 *         with-java
 * @author Zsolt Jurányi
 * @since 0.1.0
 */
public class Proxy {

	private static final Logger L = LoggerFactory.getLogger(Proxy.class);

	/**
	 * Sets <code>http.proxyHost</code> and <code>http.proxyPort</code> system
	 * properties.
	 *
	 * @param host Proxy host (domain or IP)
	 * @param port Proxy port
	 */
	public static void use(@Nonnull final String host, final int port) {
		Check.notNull(host, "host must not be null");
		L.info("Using proxy: {}:{}", host, port);
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", Integer.toString(port));
	}

	/**
	 * Sets <code>http.proxyHost</code> and <code>http.proxyPort</code> system
	 * properties and also sets up an <code>Authenticator</code>.
	 *
	 * @param host Proxy host (domain or IP)
	 * @param port Proxy port
	 * @param user Proxy user
	 * @param pass Proxy password
	 */
	public static void use(@Nonnull final String host, final int port, @Nonnull final String user, @Nonnull final String pass) {
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
