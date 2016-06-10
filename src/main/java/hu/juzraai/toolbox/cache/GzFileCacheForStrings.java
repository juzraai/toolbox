package hu.juzraai.toolbox.cache;

import hu.juzraai.toolbox.log.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * {@link FileCache} implementation which stores {@link String} objects in
 * GZipped text files.
 *
 * @author Zsolt Jurányi
 * @see FileCache
 * @since 16.06
 */
public class GzFileCacheForStrings extends FileCache<String> {

	private static final Logger L = LoggerFactory.getLogger(GzFileCacheForStrings.class);

	/**
	 * Creates a new instance.
	 *
	 * @param directory The cache directory where files will be stored.
	 */
	public GzFileCacheForStrings(@Nonnull File directory) {
		super(directory);
	}

	/**
	 * Creates a new instance.
	 *
	 * @param directory  The cache directory where files will be stored.
	 * @param expiration Expiration in milliseconds. Elements in cache will be
	 *                   handled as expired if this amount of time have elapsed
	 *                   since their timestamp. If it's <code>null</code>,
	 *                   cached elements will never expire.
	 */
	public GzFileCacheForStrings(@Nonnull File directory, Long expiration) {
		super(directory, expiration);
	}

	/**
	 * Creates a new instance.
	 *
	 * @param directory  The cache directory where files will be stored.
	 * @param expiration Expiration in the time unit of your choice. Elements in
	 *                   cache will be handled as expired if this amount of time
	 *                   have elapsed since their timestamp.
	 * @param timeUnit   {@link TimeUnit} object representing the time unit you
	 *                   specified <code>duration</code> in. It's used to
	 *                   convert <code>duration</code> to milliseconds.
	 */
	public GzFileCacheForStrings(@Nonnull File directory, long expiration, @Nonnull TimeUnit timeUnit) {
		super(directory, expiration, timeUnit);
	}

	/**
	 * Reads the specified {@link File} as a GZipped text file.
	 *
	 * @param key  The key {@link #fetch(String)} was called with, now can be
	 *             used in logs
	 * @param file {@link File} to be loaded
	 * @return The contents of the Gzipped text file
	 */
	@Override
	@CheckForNull
	protected String load(@Nonnull String key, @Nonnull File file) {
		String content = null;
		try (FileInputStream fis = new FileInputStream(file);
			 InputStream gis = new GZIPInputStream(fis);
			 Scanner s = new Scanner(gis, "UTF8")) {
			content = s.useDelimiter("\\Z").next();
			L.info("'{}' fetched from cache, content length: {}", key, content.length());
			L.debug("'{}' <- {}", key, file.getAbsolutePath());
		} catch (IOException e) {
			L.error(String.format("IO error when fetching '%s'", key), e);
		}
		return content;
	}

	/**
	 * Saves the given content in a Gzipped text file.
	 *
	 * @param key     The key {@link #fetch(String)} was called with, now can be
	 *                used in logs
	 * @param file    {@link File} to write
	 * @param content Content to be saved
	 * @return <code>true</code> if the storing succeded, <code>false</code> if
	 * it failed
	 */
	@Override
	protected boolean save(@Nonnull String key, @Nonnull File file, @Nonnull String content) {
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
