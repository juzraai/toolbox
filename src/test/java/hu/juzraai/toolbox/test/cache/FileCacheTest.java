package hu.juzraai.toolbox.test.cache;


import hu.juzraai.toolbox.cache.Cache;
import hu.juzraai.toolbox.cache.FileCache;
import hu.juzraai.toolbox.cache.GzFileStringCache;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Testing functionalities of any {@link FileCache} implementation.
 *
 * @param <T>
 * @author Zsolt Jurányi
 * @version 16.06
 * @see FileCache
 */
public abstract class FileCacheTest<T> extends CacheTest<T> {

	protected static final File DIRECTORY = new File("test-data");
	private static final String KEY_WITH_SUBDIR = String.format("subdir%sfilename", File.separator);

	public FileCacheTest(Cache<T> cache) {
		super(cache instanceof FileCache ? cache : null);
		if (null == super.cache) {
			throw new IllegalArgumentException();
		}
	}

	@AfterClass
	@BeforeClass
	public static void deleteCacheDirectory() throws IOException {
		FileUtils.deleteDirectory(DIRECTORY);
	}

	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowExceptionForNullArg() {
		new GzFileStringCache(null);
	}

	@Test(expected = NullPointerException.class)
	public void containsShouldThrowExceptionForNullArg() {
		cache.contains(null);
	}

	@Test
	public void containsShouldWorkWithSubdirectories() {
		cache.store(KEY_WITH_SUBDIR, provideUniqueTestData());
		assertTrue(cache.contains(KEY_WITH_SUBDIR));
	}

	@Test(expected = NullPointerException.class)
	public void fetchShouldThrowExceptionForNullArg() {
		cache.fetch(null);
	}

	@Test
	public void getDirectoryShouldReturnDirectory() {
		assertEquals(DIRECTORY, ((FileCache<T>) cache).getDirectory());
	}

	@Test
	public void keyPatternShouldAcceptBackslash() {
		assertTrue("directory\\filename.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldAcceptDash() {
		assertTrue("file-name.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldAcceptSlash() {
		assertTrue("directory/filename.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldAcceptUnderscore() {
		assertTrue("file_name.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldAcceptUsualFilemame() {
		assertTrue("filename.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptAsterisk() {
		assertFalse("file*name.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptBacklashSuffix() {
		assertFalse("something\\".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptDoubleQuote() {
		assertFalse("file\"name.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptEmptyString() {
		assertFalse("".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptNewline() {
		assertFalse("file\nname.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptSingleQuote() {
		assertFalse("file'name.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptSlashSuffix() {
		assertFalse("something/".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptSpace() {
		assertFalse("file name.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptTab() {
		assertFalse("file\tname.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}


	@Test
	public void removeShouldWorkWithSubdirectories() {
		cache.store(KEY_WITH_SUBDIR, provideUniqueTestData());
		cache.remove(KEY_WITH_SUBDIR);
		assertFalse(cache.contains(KEY_WITH_SUBDIR));
	}

	@Override
	public void removeUsedKeys() {
		super.removeUsedKeys();
		cache.remove(KEY_WITH_SUBDIR);
	}

	@Test
	public void storeShouldCreateCacheDirectoryAutomatically() throws IOException {
		deleteCacheDirectory();
		assertFalse(DIRECTORY.exists());
		cache.store(KEY_1, provideUniqueTestData());
		assertTrue(DIRECTORY.exists());
	}

	@Test(expected = IllegalArgumentException.class)
	public void storeShouldThrowExceptionForInvalidArg() {
		cache.store("file*name", provideUniqueTestData());
	}

	@Test(expected = NullPointerException.class)
	public void storeShouldThrowExceptionForNullArg() {
		cache.store(null, provideUniqueTestData());
	}

	@Test
	public void storeShouldWorkWithSubdirectories() {
		T value = provideUniqueTestData();
		cache.store(KEY_WITH_SUBDIR, value);
		assertEquals(value, cache.fetch(KEY_WITH_SUBDIR));
	}
}
