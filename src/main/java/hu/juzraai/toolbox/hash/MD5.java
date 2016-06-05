package hu.juzraai.toolbox.hash;

import javax.annotation.Nonnull;
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

	@Nonnull
	private static String byteArrToString(@Nonnull byte[] b) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return result.toString();
	}

	@Nonnull
	public static byte[] createChecksum(@Nonnull String filename) throws IOException, NoSuchAlgorithmException {
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

	@Nonnull
	public static String fromFile(@Nonnull String filename) throws Exception {
		byte[] b = createChecksum(filename);
		return byteArrToString(b);
	}

	@Nonnull
	public static String fromString(@Nonnull String s) throws NoSuchAlgorithmException {
		MessageDigest complete = MessageDigest.getInstance("MD5");
		byte[] b = complete.digest(s.getBytes());
		return byteArrToString(b);
	}
}
