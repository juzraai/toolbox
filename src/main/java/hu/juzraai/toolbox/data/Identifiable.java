package hu.juzraai.toolbox.data;

import javax.annotation.Nonnull;

/**
 * @param <T>
 * @author Zsolt Jurányi
 * @since 16.07
 */
public interface Identifiable<T> { // TODO doc

	@Nonnull
	T getId();
}
