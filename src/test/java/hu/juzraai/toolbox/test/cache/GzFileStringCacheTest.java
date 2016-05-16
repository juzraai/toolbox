package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.Cache;
import hu.juzraai.toolbox.cache.GzFileStringCache;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;

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

	private final GzFileStringCache CACHE = new GzFileStringCache(DIRECTORY);

	// TODO test VALID_KEY_PATTERN
	// TODO test invalid key
	// TODO test stored key - file exists

	@AfterClass
	public static void deleteCacheDirectory() {
		DIRECTORY.delete();
	}

	@Test(expected = NullPointerException.class)
	public void constructorShouldThrowExceptionForNullArg() {
		new GzFileStringCache(null);
	}

	@Test
	public void getDirectoryShouldReturnDirectory() {
		assertEquals(DIRECTORY, CACHE.getDirectory());
	}

	@Override
	public Cache<String> provideCacheImplementation() {
		return CACHE;
	}

	@Override
	protected String provideUniqueTestData() {
		String s = Long.toString(System.nanoTime());
		return s;
	}

	@Test
	public void storeShouldCreateDirectoryAutomatically() {
		deleteCacheDirectory(); // should work, @After removes all files
		assertFalse(DIRECTORY.exists());
		CACHE.store(KEY_1, provideUniqueTestData());
		assertTrue(DIRECTORY.exists());
	}
}
