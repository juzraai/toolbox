package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.Cache;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Testing common functionalities of any {@link Cache} implementation.
 *
 * @author Zsolt Jurányi
 * @see Cache
 * @since 0.3.0
 */
public abstract class CacheTest<T> {

	protected static final String KEY_1 = "key-1";
	protected static final String KEY_2 = "key-2";
	protected static final String NON_EXISTENT_KEY = "non-existent-key";
	protected static final String REMOVED_KEY = "removed-key";

	private Cache<T> cache;

	@Test
	public void containsShouldReturnFalseForNonExistentKey() {
		assertFalse(cache.contains(NON_EXISTENT_KEY));
	}

	@Test
	public void containsShouldReturnFalseForRemovedKey() {
		cache.store(REMOVED_KEY, provideUniqueTestData());
		cache.remove(REMOVED_KEY);
		assertFalse(cache.contains(REMOVED_KEY));
	}

	@Test
	public void containsShouldReturnTrueForStoredKey() {
		cache.store(KEY_1, provideUniqueTestData());
		assertTrue(cache.contains(KEY_1));
	}

	@Test
	public void fetchShouldReturnAppropriateValues() {
		T value1 = provideUniqueTestData();
		T value2 = provideUniqueTestData();
		cache.store(KEY_1, value1);
		cache.store(KEY_2, value2);
		assertEquals(value1, cache.fetch(KEY_1));
		assertEquals(value2, cache.fetch(KEY_2));
	}

	@Test
	public void fetchShouldReturnNullForNonExistentKey() {
		assertNull(cache.fetch(NON_EXISTENT_KEY));
	}

	@Test
	public void fetchShouldReturnNullForRemovedKey() {
		cache.store(REMOVED_KEY, provideUniqueTestData());
		cache.remove(REMOVED_KEY);
		assertNull(cache.fetch(REMOVED_KEY));
	}

	protected abstract Cache provideCacheImplementation();

	@Test
	public void provideCacheImplementationShouldNotReturnNull() {
		assertNotNull(provideCacheImplementation());
	}

	protected abstract T provideUniqueTestData();

	@Test
	public void provideUniqueTestDataShouldNotReturnNull() {
		assertNotNull(provideUniqueTestData());
	}

	@Test
	public void provideUniqueTestDataShouldReturnUniqueValues() {
		Set<T> set = new HashSet<T>();
		for (int i = 0; i < 100; i++) {
			set.add(provideUniqueTestData());
		}
		assertEquals(100, set.size());
	}

	@Before
	public void receiveCacheImplementation() {
		cache = provideCacheImplementation();
	}

	@After
	public void removeUsedKeys() {
		cache.remove(KEY_1);
		cache.remove(KEY_2);
		cache.remove(NON_EXISTENT_KEY);
		cache.remove(REMOVED_KEY);
	}
}
