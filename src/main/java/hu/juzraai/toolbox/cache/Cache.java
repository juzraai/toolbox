package hu.juzraai.toolbox.cache;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * This interface describes the common functionalities of a cache.
 *
 * @param <T> Type of content which the cache handles.
 * @author Zsolt Jurányi
 * @since 16.06
 */
public interface Cache<T> {

	/**
	 * Checks if the given key exists in the cache.
	 *
	 * @param key Key to be checked
	 * @return <code>true</code> if the key exists, <code>false</code> otherwise
	 */
	boolean contains(@Nonnull String key);

	/**
	 * Returns the stored content identified by the given key.
	 *
	 * @param key Key to identify the required content
	 * @return The content object or <code>null</code> if the key doesn't exist
	 * in the cache
	 */
	@CheckForNull
	T fetch(@Nonnull String key);

	/**
	 * Removes the content identified by the key from the cache.
	 *
	 * @param key Key to be removed from cache
	 */
	void remove(@Nonnull String key);

	/**
	 * Stores the given content identified by the given key.
	 *
	 * @param key     Key which identifies the content
	 * @param content The content object to be stored
	 * @return <code>true</code> if the storing succeded, <code>false</code> if
	 * it failed
	 */
	boolean store(@Nonnull String key, T content);

}
