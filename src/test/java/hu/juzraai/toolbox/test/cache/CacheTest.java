package hu.juzraai.toolbox.test.cache;

import hu.juzraai.toolbox.cache.Cache;
import org.junit.After;
import org.junit.Test;

import java.util.Date;
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
			System.out.printf("WARN: Expiration not enabled in cache: %s (test class: %s)%n", cache.getClass().getName(), getClass().getSimpleName());
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
		if (expirationEnabled()) {
			cache.store(KEY_1, provideUniqueTestData());
			Thread.sleep(EXPIRATION_TEST_SLEEP);
			assertNull(cache.fetchIfNotExpired(KEY_1));
		}
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

	@Test
	public void removeIfExpiredShouldNotRemoveIfNotExpired() {
		if (expirationEnabled()) {
			cache.store(KEY_1, provideUniqueTestData());
			cache.removeIfExpired(KEY_1);
			assertTrue(cache.contains(KEY_1));
		}
	}

	@Test
	public void removeIfExpiredShouldRemoveIfExpired() throws InterruptedException {
		if (expirationEnabled()) {
			cache.store(KEY_1, provideUniqueTestData());
			Thread.sleep(EXPIRATION_TEST_SLEEP);
			cache.removeIfExpired(KEY_1);
			assertFalse(cache.contains(KEY_1));
		}
	}

	@After
	public void removeUsedKeys() {
		cache.remove(KEY_1);
		cache.remove(KEY_2);
		cache.remove(NON_EXISTENT_KEY);
		cache.remove(REMOVED_KEY);
	}

	@Test
	public void storeShouldGenerateTimestamp() {
		if (expirationEnabled()) {
			long t1 = new Date().getTime();
			cache.store(KEY_1, provideUniqueTestData());
			long t2 = new Date().getTime();
			Date d = cache.timestampOf(KEY_1);
			long t = null == d ? 0 : d.getTime();

			// debugging Travis fail:
			System.out.println("d = " + d);
			System.out.println("t = " + t);
			System.out.println("t2= " + t2);

			assertTrue(t1 <= t && t <= t2);
		}
	}

	@Test
	public void storeShouldUpdateTimestamp() throws InterruptedException {
		if (expirationEnabled()) {
			cache.store(KEY_1, provideUniqueTestData());
			Date d1 = cache.timestampOf(KEY_1);
			long t1 = null == d1 ? 0 : d1.getTime();

			Thread.sleep(1000);

			cache.store(KEY_1, provideUniqueTestData());
			Date d2 = cache.timestampOf(KEY_1);
			long t2 = null == d2 ? 0 : d2.getTime();
			assertTrue(t1 < t2);
		}
	}
}
