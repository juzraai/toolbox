package hu.juzraai.toolbox.data;

import javax.annotation.Nonnull;

/**
 * Indicates that the implementing class has a <code>getId()</code> method with
 * a return type of <code>T</code>.
 *
 * @param <T> Type of the record identifier
 * @author Zsolt Jurányi
 * @since 16.07
 */
public interface Identifiable<T> {

	/**
	 * @return The record identifier.
	 */
	@Nonnull
	T getId();
}
