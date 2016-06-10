package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.GzFileCacheForStrings;

/**
 * Testing the functionalities of {@link GzFileCacheForStrings}.
 *
 * @author Zsolt Jurányi
 * @see FileCacheTest
 * @see GzFileCacheForStrings
 * @since 16.06
 */
public class GzFileCacheForStringsTest extends FileCacheTest<String> {

	public GzFileCacheForStringsTest() {
		super(new GzFileCacheForStrings(FileCacheTest.DIRECTORY, EXPIRATION, EXPIRATION_TIME_UNIT));
	}

	@Override
	protected String provideUniqueTestData() {
		return Long.toString(System.nanoTime());
	}
}
