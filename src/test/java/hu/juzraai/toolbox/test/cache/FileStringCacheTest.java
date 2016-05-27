package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.FileStringCache;

/**
 * Testing the functionalities of {@link FileStringCache}.
 *
 * @author Zsolt Jurányi
 * @see FileCacheTest
 * @see FileStringCache
 * @since 16.06
 */
public class FileStringCacheTest extends FileCacheTest<String> {

	public FileStringCacheTest() {
		super(new FileStringCache(FileCacheTest.DIRECTORY));
	}

	@Override
	protected String provideUniqueTestData() {
		return Long.toString(System.nanoTime());
	}
}
