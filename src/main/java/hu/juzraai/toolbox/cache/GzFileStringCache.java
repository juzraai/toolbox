package hu.juzraai.toolbox.cache;

import hu.juzraai.toolbox.log.LoggerFactory;
import hu.juzraai.toolbox.test.Check;
import org.slf4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.*;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * {@link Cache} implementation which stores {@link String} objects in GZipped
 * text files, in the specified cache directory. In this cache the key is the
 * filename. The 4 main methods are all <code>synchronized</code> to avoid
 * concurrent I/O operations.
 *
 * @author Zsolt Jurányi
 * @see Cache
 * @since 16.06
 */
public class GzFileStringCache implements Cache<String> {

	/**
	 * The given key is matched against this pattern. A valid key can contain
	 * letters, numbers, underscore (_), dash (-), slash (/), backslash (/), and
	 * should not end with slash or backslash.
	 */
	public static final String VALID_KEY_PATTERN = "[A-Za-z0-9_.\\-/\\\\]*[^\\\\/]";

	private static final Logger L = LoggerFactory.getLogger(GzFileStringCache.class);

	private final File directory;

	/**
	 * Creates a new instance.
	 *
	 * @param directory The cache directory where files will be stored.
	 */
	public GzFileStringCache(@Nonnull File directory) {
		this.directory = Check.notNull(directory, "directory must not be null");
	}

	/**
	 * Checks if the specified file exists in the cache directory.
	 *
	 * @param key Filename inside cache directory to be checked
	 * @return <code>true</code> if the key exists, <code>false</code> otherwise
	 */
	@Override
	public synchronized boolean contains(@Nonnull String key) {
		return key2File(key).exists(); // checks performed inside
	}

	/**
	 * Reads the contents of the specified file from cache directory.
	 *
	 * @param key Filename inside cache directory to be loaded
	 * @return The content or <code>null</code> if the key doesn't exist in the
	 * cache
	 */
	@Override
	@CheckForNull
	public synchronized String fetch(@Nonnull String key) {
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

	/**
	 * @return The cache directory
	 */
	@Nonnull
	public File getDirectory() {
		return directory;
	}

	/**
	 * Validates the given key then generates a {@link File} object from it. It
	 * will throw an {@link IllegalArgumentException} when the key is invalid.
	 *
	 * @param key Key to be transformed into a {@link File}
	 * @return A {@link File} object which points to a file having the key as
	 * its name, inside the cache directory
	 */
	@Nonnull
	protected File key2File(@Nonnull String key) {
		Check.notNull(key, "key must not be null");
		Check.argument(key.matches(VALID_KEY_PATTERN), "key must contain characters valid in a filename");
		return new File(directory, key);
	}

	/**
	 * Creates the parent directory tree for the given file.
	 *
	 * @param file File which needs its parent directories to be created
	 */
	protected void mkdirsForFile(@Nonnull File file) {
		File parent = file.getParentFile();
		if (null != parent && (!parent.exists() || !parent.isDirectory())) {
			L.info("Creating cache directory: {}", parent.getAbsolutePath());
			if (!parent.mkdirs()) {
				L.error("Could not create cache directory: {}", parent.getAbsolutePath());
			}
		}
	}

	/**
	 * Deletes the specified file from cache directory.
	 *
	 * @param key Filename inside cache directory to be removed
	 */
	@Override
	public synchronized void remove(@Nonnull String key) {
		File file = key2File(key);
		if (file.exists() && !file.delete()) {
			L.error("Failed to remove '{}': {}", key, file.getAbsolutePath());
		}
	}

	/**
	 * Stores the given contents into a GZip file in the cache directory.
	 *
	 * @param key     Filename inside cache directory
	 * @param content The content to be stored
	 * @return <code>true</code> if the storing succeded, <code>false</code> if
	 * it failed
	 */
	@Override
	public synchronized boolean store(@Nonnull String key, String content) {
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
