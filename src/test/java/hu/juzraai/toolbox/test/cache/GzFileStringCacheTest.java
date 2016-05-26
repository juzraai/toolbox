package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.Cache;
import hu.juzraai.toolbox.cache.GzFileStringCache;

/**
 * Testing the functionalities of {@link GzFileStringCache}.
 *
 * @author Zsolt Jurányi
 * @see Cache
 * @see CacheTest
 * @see GzFileStringCache
 * @since 16.06
 */
public class GzFileStringCacheTest extends FileCacheTest<String> {

	public GzFileStringCacheTest() {
		super(new GzFileStringCache(FileCacheTest.DIRECTORY));
	}

	@Override
	protected String provideUniqueTestData() {
		return Long.toString(System.nanoTime());
	}
}
