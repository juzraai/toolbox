package hu.juzraai.toolbox.parse;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extends the functionalities of {@link Pattern} with helper methods.
 *
 * @author Zsolt Jurányi
 * @see Pattern
 * @since 16.06
 */
public class Regex {

	private final Pattern pattern;

	/**
	 * Creates a new instance.
	 *
	 * @param pattern {@link Pattern} to use
	 */
	public Regex(@Nonnull Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Regex)) return false;
		Regex regex = (Regex) o;
		return pattern.equals(regex.pattern);
	}

	/**
	 * @return The pattern
	 */
	@Nonnull
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * Extracts names of named groups (<code>(?&lt;name&gt;pattern)</code>) from
	 * the regular expression.
	 *
	 * @return Names of named groups in a {@link Set}
	 */
	@Nonnull
	public Set<String> groupNames() {
		// http://stackoverflow.com/questions/15588903/get-group-names-in-java-regex
		Set<String> namedGroups = new TreeSet<>();
		Matcher m = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>").matcher(
				pattern.pattern());
		while (m.find()) {
			namedGroups.add(m.group(1));
		}
		return namedGroups;
	}

	/**
	 * Extract group values from the first match into a {@link Map} using {@link
	 * #groupsToMap(Matcher, Map)}.
	 *
	 * @param input Input <code>String</code> to parse
	 * @return Map of group values, keys will be the group indices and names
	 * @see #groupsToMap(Matcher, Map)
	 */
	@Nonnull
	public Map<String, String> groupsIntoMap(@Nonnull String input) {
		Map<String, String> map = new HashMap<>();
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			groupsToMap(matcher, map);
		}
		return map;
	}

	/**
	 * Extract group values from all matches into a {@link List} of {@link Map}s
	 * using {@link #groupsToMap(Matcher, Map)}. Each <code>Map</code> contains
	 * one match result.
	 *
	 * @param input Input <code>String</code> to parse
	 * @return List of maps which contain group values, keys will be the group
	 * indices and names
	 * @see #groupsToMap(Matcher, Map)
	 */
	@Nonnull
	public List<Map<String, String>> groupsIntoMaps(@Nonnull String input) {
		List<Map<String, String>> list = new ArrayList<>();
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			Map<String, String> map = new HashMap<>();
			groupsToMap(matcher, map);
			list.add(map);
		}
		return list;
	}

	/**
	 * Fetches matched group values from a {@link Matcher} into a {@link Map}.
	 * Both indexed groups and named groups are processed. The keys of the
	 * <code>Map</code> will be group indices (converted to <code>String</code>)
	 * and group names. The <code>Map</code> will be empty if no match were
	 * found.
	 * <p>
	 * Calls <code>groupNames()</code> to extract group names.
	 * <code>find()</code> should be called on the <code>Matcher</code> object
	 * before.
	 *
	 * @param matcher <code>Matcher</code> object to process
	 * @param map     Map of group values, keys will be the group indices and
	 *                names
	 * @see Matcher
	 * @see #groupNames()
	 */
	protected void groupsToMap(@Nonnull Matcher matcher, @Nonnull Map<String, String> map) {

		// anonymous groups - by index
		for (int groupIndex = 0; groupIndex <= matcher.groupCount(); groupIndex++) {
			String matchedValue = matcher.group(groupIndex);
			if (null != matchedValue) {
				map.put(Integer.toString(groupIndex), matchedValue);
			}
		}

		// named groups
		for (String groupName : groupNames()) {
			String matchedValue = matcher.group(groupName);
			if (null != matchedValue) {
				map.put(groupName, matchedValue);
			}
		}
	}

	@Override
	public int hashCode() {
		return pattern.hashCode();
	}

	@Override
	@Nonnull
	public String toString() {
		return "Regex{" +
				"pattern=" + pattern +
				'}';
	}
}
