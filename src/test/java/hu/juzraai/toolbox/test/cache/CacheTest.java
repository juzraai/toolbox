package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.Cache;
import org.junit.After;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Testing common functionalities of any {@link Cache} implementation.
 *
 * @author Zsolt Jurányi
 * @see Cache
 * @since 16.06
 */
public abstract class CacheTest<T> {

	// TODO test expiration
	// - removeIfExpiredShouldNotRemoveNotExpired: store, remove, contains -> true
	// - removeIfExpiredShouldRemoveIfExpired: store, sleep, remove, contains -> false
	// - store should generate timestamp: now, store now, timestamp of between 2 now
	// - store should update timestamp: store, timestampOf, store, timestampOf

	protected static final long EXPIRATION = 3;
	protected static final TimeUnit EXPIRATION_TIME_UNIT = TimeUnit.SECONDS;
	protected static final long EXPIRATION_TEST_SLEEP = 3200;
	protected static final String KEY_1 = "key-1";
	protected static final String KEY_2 = "key-2";
	protected static final String NON_EXISTENT_KEY = "non-existent-key";
	protected static final String REMOVED_KEY = "removed-key";

	protected final Cache<T> cache;

	protected CacheTest(Cache<T> cache) {
		this.cache = cache;
	}

	@Test
	public void constructorShouldConvertTimeUnit() {
		if (expirationEnabled()) {
			assertEquals(EXPIRATION_TIME_UNIT.toMillis(EXPIRATION), (long) cache.getExpiration());
		}
	}

	@Test
	public void containsAndNotExpiredShouldReturnFalseForNonExistentKey() {
		assertFalse(cache.containsAndNotExpired(NON_EXISTENT_KEY));
	}

	@Test
	public void containsAndNotExpiredShouldReturnFalseWhenContainsAndExpired() throws InterruptedException {
		if (expirationEnabled()) {
			cache.store(KEY_1, provideUniqueTestData());
			Thread.sleep(EXPIRATION_TEST_SLEEP);
			assertFalse(cache.containsAndNotExpired(KEY_1));
		}
	}

	@Test
	public void containsAndNotExpiredShouldReturnTrueWhenContainsAndNotExpired() {
		if (expirationEnabled()) {
			cache.store(KEY_1, provideUniqueTestData());
			assertTrue(cache.containsAndNotExpired(KEY_1));
		}
	}

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

	private boolean expirationEnabled() {
		if (null == cache.getExpiration()) {
			System.out.printf("WARN: Expiration not enabled in cache%n\tTest class: %s%n\tCache class: %s%n", getClass().getName(), cache.getClass().getName());
			return false;
		}
		return true;
	}

	@Test
	public void fetchIfNotExpiredShouldReturnAppropriateValues() {
		T value1 = provideUniqueTestData();
		T value2 = provideUniqueTestData();
		cache.store(KEY_1, value1);
		cache.store(KEY_2, value2);
		assertEquals(value1, cache.fetchIfNotExpired(KEY_1));
		assertEquals(value2, cache.fetchIfNotExpired(KEY_2));
	}

	@Test
	public void fetchIfNotExpiredShouldReturnNullForExpiredKey() throws InterruptedException {
		cache.store(KEY_1, provideUniqueTestData());
		Thread.sleep(EXPIRATION_TEST_SLEEP);
		assertNull(cache.fetchIfNotExpired(KEY_1));
	}

	@Test
	public void fetchIfNotExpiredShouldReturnNullForNonExistentKey() {
		assertNull(cache.fetchIfNotExpired(NON_EXISTENT_KEY));
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

	@After
	public void removeUsedKeys() {
		cache.remove(KEY_1);
		cache.remove(KEY_2);
		cache.remove(NON_EXISTENT_KEY);
		cache.remove(REMOVED_KEY);
	}
}
