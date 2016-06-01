package hu.juzraai.toolbox.cache;

import hu.juzraai.toolbox.log.LoggerFactory;
import hu.juzraai.toolbox.test.Check;
import org.slf4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;

/**
 * Abstract {@link Cache} implementation which stores contents in files, in the
 * specified cache directory. The key is the filename. The 4 main methods are
 * all <code>synchronized</code> to avoid concurrent I/O operations. The
 * concrete file read and write operations must be implemented in child
 * classes.
 *
 * @param <T> Type of content which the cache handles.
 * @author Zsolt Jurányi
 * @since 16.06
 */
public abstract class FileCache<T> implements Cache<T> {

	/**
	 * The given key is matched against this pattern. A valid key can contain
	 * letters, numbers, underscore (_), dash (-), slash (/), backslash (/), and
	 * should not end with slash or backslash.
	 */
	public static final String VALID_KEY_PATTERN = "[A-Za-z0-9_.\\-/\\\\]*[^\\\\/]";
	private static final Logger L = LoggerFactory.getLogger(FileCache.class);

	private final File directory;

	/**
	 * Creates a new instance.
	 *
	 * @param directory The cache directory where files will be stored.
	 */
	public FileCache(@Nonnull File directory) {
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
	public synchronized T fetch(@Nonnull String key) {
		File file = key2File(key); // checks performed inside
		return file.exists() ? load(key, file) : null;
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
	 * This method is called from {@link #fetch(String)} and is responsible for
	 * loading the contents from the specified file. The caller ensures that the
	 * file exists when this method is called.
	 *
	 * @param key  The key {@link #fetch(String)} was called with, now can be
	 *             used in logs
	 * @param file {@link File} to be loaded
	 * @return The contents of the file or <code>null</code> on error
	 * @see #fetch(String)
	 */
	protected abstract T load(@Nonnull String key, @Nonnull File file);

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
		File file = key2File(key); // checks performed inside
		if (file.exists() && !file.delete()) {
			L.error("Failed to remove '{}': {}", key, file.getAbsolutePath());
		}
	}

	/**
	 * This method is called from {@link #store(String, Object)} and is
	 * responsible for saving the given content to the specified file.
	 *
	 * @param key     The key {@link #fetch(String)} was called with, now can be
	 *                used in logs
	 * @param file    {@link File} to write
	 * @param content Content to be saved
	 * @return <code>true</code> if the storing succeded, <code>false</code> if
	 * it failed
	 * @see #store(String, Object)
	 */
	protected abstract boolean save(@Nonnull String key, @Nonnull File file, T content);

	/**
	 * Stores the given contents into a file in the cache directory. Cache
	 * directory is created automatically.
	 *
	 * @param key     Filename inside cache directory
	 * @param content The content to be stored
	 * @return <code>true</code> if the storing succeded, <code>false</code> if
	 * it failed
	 */
	@Override
	public synchronized boolean store(@Nonnull String key, T content) {
		File file = key2File(key); // checks performed inside
		mkdirsForFile(file);
		return save(key, file, content);
	}
}
