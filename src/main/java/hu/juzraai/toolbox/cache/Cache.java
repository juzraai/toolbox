package hu.juzraai.toolbox.cache;

/**
 * This interface describes the common functionalities of a cache.
 *
 * @since 0.2.0
 * @author Zsolt Jurányi
 *
 * @param <T>
 *            Type of content which the cache handles.
 */
public interface Cache<T> {

	/**
	 * Checks if the given key is existing in the cache.
	 *
	 * @param key
	 *            Key to be checked
	 * @return <code>true</code> if the key exists, <code>false</code> otherwise
	 */
	boolean contains(String key);

	/**
	 * Returns the stored content identified by the given key.
	 *
	 * @param key
	 *            Key to identify the required content
	 * @return The content object or <code>null</code> if the key exists in the
	 *         cache
	 */
	T fetch(String key);

	/**
	 * Removes the content identified by the key from the cache.
	 * 
	 * @param key
	 */
	void remove(String key);

	/**
	 * Stores the given content identified by the given key.
	 *
	 * @param key
	 *            Key which identifies the content
	 * @param content
	 *            The content object to be stored
	 * @return <code>true</code> if the storing succeded, <code>false</code> if
	 *         it failed
	 */
	boolean store(String key, T content);

}
