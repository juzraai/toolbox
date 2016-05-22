package hu.juzraai.toolbox.cache;

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
	boolean contains(String key);

	/**
	 * Returns the stored content identified by the given key.
	 *
	 * @param key Key to identify the required content
	 * @return The content object or <code>null</code> if the key doesn't exist
	 * in the cache
	 */
	T fetch(String key);

	/**
	 * Removes the content identified by the key from the cache.
	 *
	 * @param key Key to be removed from cache
	 */
	void remove(String key);

	/**
	 * Stores the given content identified by the given key.
	 *
	 * @param key     Key which identifies the content
	 * @param content The content object to be stored
	 * @return <code>true</code> if the storing succeded, <code>false</code> if
	 * it failed
	 */
	boolean store(String key, T content);

}
