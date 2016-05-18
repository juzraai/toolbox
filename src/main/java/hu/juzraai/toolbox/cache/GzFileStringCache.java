package hu.juzraai.toolbox.cache;

import hu.juzraai.toolbox.log.LoggerFactory;
import hu.juzraai.toolbox.test.Check;
import org.slf4j.Logger;

import java.io.*;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Zsolt Jurányi
 * @see Cache
 * @since 0.3.0
 */
public class GzFileStringCache implements Cache<String> {
	// TODO doc

	public static final String VALID_KEY_PATTERN = "[A-Za-z0-9_.\\-/\\\\]*[^\\\\/]";

	private static final Logger L = LoggerFactory.getLogger(GzFileStringCache.class);

	private final File directory;

	public GzFileStringCache(File directory) {
		Check.notNull(directory, "directory must not be null");
		this.directory = directory;
	}

	@Override
	public boolean contains(String key) {
		return key2File(key).exists(); // checks performed inside
	}

	@Override
	public String fetch(String key) {
		File file = key2File(key); // checks performed inside
		String content = null;
		if (file.exists()) {
			try (FileInputStream fis = new FileInputStream(file);
				 InputStream gis = new GZIPInputStream(fis);
				 Scanner s = new Scanner(gis, "UTF8")) {
				content = s.useDelimiter("\\Z").next();
				L.info("'{}' fetched from cache, content length: {}", key, content.length());
				L.debug("'{}' <- {}", key, file.getAbsolutePath());
			} catch (IOException e) {
				L.error(String.format("IO error when fetching '%s'", key), e);
			}
		} else {
			L.warn("File for key '{}' not found: {}", key, file.getAbsolutePath());
		}
		return content;
	}

	public File getDirectory() {
		return directory;
	}

	protected File key2File(String key) {
		Check.notNull(key, "key must not be null");
		Check.argument(key.matches(VALID_KEY_PATTERN), "key must contain characters valid in a filename");
		return new File(directory, key);
	}

	protected void mkdirsForFile(File file) {
		File parent = file.getParentFile();
		if (null != parent && (!parent.exists() || !parent.isDirectory())) {
			L.info("Creating cache directory: {}", parent.getAbsolutePath());
			if (!parent.mkdirs()) {
				L.error("Could not create cache directory: {}", parent.getAbsolutePath());
			}
		}
	}

	@Override
	public void remove(String key) {
		File file = key2File(key);
		if (file.exists() && !file.delete()) {
			L.error("Failed to remove '{}': {}", key, file.getAbsolutePath());
		}
	}

	@Override
	public boolean store(String key, String content) {
		File file = key2File(key);
		mkdirsForFile(file);
		try (FileOutputStream fos = new FileOutputStream(file);
			 OutputStream gos = new GZIPOutputStream(fos);
			 Writer w = new OutputStreamWriter(gos, "UTF-8")) {
			w.write(content);
			w.flush();
			L.info("'{}' stored, content length: {}, file size: {}", key, content.length(), file.length());
			L.debug("'{}' -> {}", key, file.getAbsolutePath());
		} catch (IOException e) {
			L.error(String.format("Failed to store key '%s' to file: %s", key, file.getAbsolutePath()), e);
			return false;
		}
		return true;
	}
}
