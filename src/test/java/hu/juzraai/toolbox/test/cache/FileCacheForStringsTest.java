package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.FileCacheForStrings;

/**
 * Testing the functionalities of {@link FileCacheForStrings}.
 *
 * @author Zsolt Jurányi
 * @see FileCacheTest
 * @see FileCacheForStrings
 * @since 16.06
 */
public class FileCacheForStringsTest extends FileCacheTest<String> {

	public FileCacheForStringsTest() {
		super(new FileCacheForStrings(FileCacheTest.DIRECTORY, EXPIRATION, EXPIRATION_TIME_UNIT));
	}

	@Override
	protected String provideUniqueTestData() {
		return Long.toString(System.nanoTime());
	}
}
