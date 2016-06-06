package hu.juzraai.toolbox.cache;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This interface describes the common functionalities of a cache.
 *
 * @param <T> Type of content which the cache handles.
 * @author Zsolt Jurányi
 * @since 16.06
 */
public abstract class Cache<T> {

	private final Long expiration;

	/**
	 * Creates a new instance.
	 *
	 * @param expiration Expiration in milliseconds. Elements in cache will be
	 *                   handled as expired if this amount of time have elapsed
	 *                   since their timestamp. If it's <code>null</code>,
	 *                   cached elements will never expire.
	 */
	protected Cache(Long expiration) {
		this.expiration = expiration;
	}

	/**
	 * Creates a new instance.
	 *
	 * @param expiration Expiration in the time unit of your choice. Elements in
	 *                   cache will be handled as expired if this amount of time
	 *                   have elapsed since their timestamp.
	 * @param timeUnit   {@link TimeUnit} object representing the time unit you
	 *                   specified <code>expiration</code> in. It's used to
	 *                   convert <code>expiration</code> to milliseconds.
	 */
	protected Cache(long expiration, @Nonnull TimeUnit timeUnit) {
		this(timeUnit.toMillis(expiration));
	}

	/**
	 * Checks if the given key exists in the cache.
	 *
	 * @param key Key to be checked
	 * @return <code>true</code> if the key exists, <code>false</code> otherwise
	 */
	public abstract boolean contains(@Nonnull String key);

	/**
	 * Checks if the given key exists and not expired.
	 *
	 * @param key Key to be checked
	 * @return <code>true</code> if the key exists and not expired,
	 * <code>false</code> otherwise
	 * @see #contains(String)
	 * @see #isExpired(String)
	 */
	public boolean containsAndNotExpired(@Nonnull String key) {
		return contains(key) && !isExpired(key);
	}

	/**
	 * Returns the stored content identified by the given key.
	 *
	 * @param key Key to identify the required content
	 * @return The content object or <code>null</code> if the key doesn't exist
	 * in the cache
	 */
	@CheckForNull
	public abstract T fetch(@Nonnull String key);

	/**
	 * @return Expiration in milliseconds. If it's <code>null</code>, cached
	 * elements will never expire.
	 */
	public Long getExpiration() {
		return expiration;
	}

	/**
	 * Determines whether the element identified by the given key is expired or
	 * not. If <code>expiration</code> is <code>null</code> it will return
	 * <code>false</code> for any element.
	 *
	 * @param key Key which identifies the element
	 * @return <code>true</code> if the elements timestamp is expired,
	 * <code>false</code> otherwise an in case <code>expiration</code> is
	 * <code>null</code>.
	 * @see #isExpired(Date)
	 * @see #timestampOf(String)
	 */
	public boolean isExpired(@Nonnull String key) {
		return isExpired(timestampOf(key));
	}

	/**
	 * Determines whether the given timestamp is expired or not based on
	 * <code>expiration</code> field. If the timestamp or
	 * <code>expiration</code> is <code>null</code> it will return
	 * <code>false</code>.
	 *
	 * @param timestamp Timestamp to test
	 * @return <code>true</code> if the <code>timestamp + expiration <
	 * now</code>, <code>false</code> otherwise and in case timestamp or
	 * <code>expiration</code> is <code>null</code>.
	 */
	private boolean isExpired(Date timestamp) { // arg can be null
		return null != expiration && null != timestamp && (timestamp.getTime() + expiration) < new Date().getTime();
	}

	/**
	 * Removes the content identified by the key from the cache.
	 *
	 * @param key Key to be removed from cache
	 */
	public abstract void remove(@Nonnull String key);

	/**
	 * Stores the given content identified by the given key.
	 *
	 * @param key     Key which identifies the content
	 * @param content The content object to be stored
	 * @return <code>true</code> if the storing succeded, <code>false</code> if
	 * it failed
	 */
	public abstract boolean store(@Nonnull String key, @Nonnull T content);

	/**
	 * Should return the timestamp of the element identified by the given key.
	 * It can return <code>null</code> if expiration feature is not applicable.
	 *
	 * @param key Key which identifies the element
	 * @return Timestamp of the element or <code>null</code> if it's not
	 * applicable.
	 */
	@CheckForNull
	public abstract Date timestampOf(@Nonnull String key);

}
