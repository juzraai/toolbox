package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.Cache;
import hu.juzraai.toolbox.cache.GzFileStringCache;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Testing the functionalities of {@link GzFileStringCache}.
 *
 * @author Zsolt Jurányi
 * @see Cache
 * @see CacheTest
 * @see GzFileStringCache
 * @since 0.3.0
 */
public class GzFileStringCacheTest extends CacheTest<String> {

	private static final File DIRECTORY = new File("test-data");
	private static final String KEY_WITH_SUBDIR = String.format("subdir%sfilename", File.separator);
	private final GzFileStringCache cache = new GzFileStringCache(DIRECTORY);

	// TODO test invalid key (store)

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

	@Test(expected = NullPointerException.class)
	public void fetchShouldThrowExceptionForNullArg() {
		cache.fetch(null);
	}

	@Test
	public void getDirectoryShouldReturnDirectory() {
		assertEquals(DIRECTORY, cache.getDirectory());
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
	public void keyPatternShouldNotAcceptDoubleQuote() {
		assertFalse("file\"name.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptEmptyString() {
		assertFalse("".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Test
	public void keyPatternShouldNotAcceptSingleQuote() {
		assertFalse("file'name.ext".matches(GzFileStringCache.VALID_KEY_PATTERN));
	}

	@Override
	public Cache<String> provideCacheImplementation() {
		return cache;
	}

	@Override
	protected String provideUniqueTestData() {
		return Long.toString(System.nanoTime());
	}

	@Override
	public void removeUsedKeys() {
		super.removeUsedKeys();
		cache.remove(KEY_WITH_SUBDIR);
	}

	@Test
	public void shouldWorkWithSubdirectories() {
		String value = provideUniqueTestData();
		cache.store(KEY_WITH_SUBDIR, value);
		assertTrue(cache.contains(KEY_WITH_SUBDIR));
		assertEquals(value, cache.fetch(KEY_WITH_SUBDIR));
		cache.remove(KEY_WITH_SUBDIR);
		assertFalse(cache.contains(KEY_WITH_SUBDIR));
		// TODO would b prettier separated? :)
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
}
