package hu.juzraai.toolbox.hash;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Functions to create MD5 hash from a String or a file.
 *
 * @since 0.1.0
 * @author http://www.rgagnon.com/javadetails/java-0416.html
 * @author Zsolt Jurányi
 *
 */
public class MD5 {

	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public static String fromFile(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	public static String fromString(String s) {
		if (null == s) {
			s = "null";
		}
		MessageDigest complete;
		try {
			complete = MessageDigest.getInstance("MD5");

			byte[] b = complete.digest(s.getBytes());
			String result = "";

			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			}
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
}
