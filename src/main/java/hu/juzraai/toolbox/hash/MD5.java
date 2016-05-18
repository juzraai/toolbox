package hu.juzraai.toolbox.hash;

import hu.juzraai.toolbox.test.Check;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Functions to create MD5 hash from a String or a file.
 *
 * @author http://www.rgagnon.com/javadetails/java-0416.html
 * @author Zsolt Jurányi
 * @since 0.1.0
 */
public class MD5 { // TODO doc

	private static String byteArrToString(byte[] b) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return result.toString();
	}

	public static byte[] createChecksum(String filename) throws IOException, NoSuchAlgorithmException {
		Check.notNull(filename, "filename must not be null");
		MessageDigest complete = MessageDigest.getInstance("MD5");
		try (InputStream fis = new FileInputStream(filename)) {
			byte[] buffer = new byte[1024];
			int numRead;
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
		}
		return complete.digest();
	}

	public static String fromFile(String filename) throws Exception {
		Check.notNull(filename, "filename must not be null");
		byte[] b = createChecksum(filename);
		return byteArrToString(b);
	}

	public static String fromString(String s) throws NoSuchAlgorithmException {
		Check.notNull(s, "s must not be null");
		MessageDigest complete = MessageDigest.getInstance("MD5");
		byte[] b = complete.digest(s.getBytes());
		return byteArrToString(b);
	}
}
