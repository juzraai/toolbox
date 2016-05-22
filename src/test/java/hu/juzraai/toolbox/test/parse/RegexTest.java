package hu.juzraai.toolbox.test.parse;


import hu.juzraai.toolbox.parse.Regex;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Testing methods of {@link Regex}.
 *
 * @author Zsolt Jurányi
 * @see Regex
 * @since 16.06
 */
public class RegexTest {

	private final Regex REGEX_WITH_GROUPS = new Regex(Pattern.compile("Something (?<old>\\d+), something (?<new>\\d+)."));
	private final Regex REGEX_WITHOUT_GROUPS = new Regex(Pattern.compile("Something \\d+, something \\d+."));
	private final String MATCHING_INPUT = "Something 23, something 42. Something 0, something 1.";
	private final String NOT_MATCHING_INPUT = "Something else.";

	@Test
	public void groupNamesShouldReturnEmptySetIfNoGroups() {
		Set<String> groupNames = REGEX_WITHOUT_GROUPS.groupNames();
		assertNotNull(groupNames);
		assertTrue(groupNames.isEmpty());
	}

	@Test
	public void groupNamesShouldgroupNames() {
		Set<String> groupNames = REGEX_WITH_GROUPS.groupNames();
		assertTrue(groupNames.contains("old"));
		assertTrue(groupNames.contains("new"));
	}

	@Test
	public void groupsIntoMapShoudldReturnEmptyMapIfNoMatch() {
		Map<String, String> map = REGEX_WITH_GROUPS.groupsIntoMap(NOT_MATCHING_INPUT);
		assertNotNull(map);
		assertTrue(map.isEmpty());
	}

	@Test
	public void groupsIntoMapShouldReturnNamedGroupsFromFirstMatch() {
		Map<String, String> map = REGEX_WITH_GROUPS.groupsIntoMap(MATCHING_INPUT);
		assertEquals("23", map.get("old"));
		assertEquals("42", map.get("new"));
	}

	@Test
	public void groupsIntoMapShouldReturnNumberedGroupsFromFirstMatch() {
		Map<String, String> map = REGEX_WITH_GROUPS.groupsIntoMap(MATCHING_INPUT);
		assertEquals("Something 23, something 42.", map.get("0"));
		assertEquals("23", map.get("1"));
		assertEquals("42", map.get("2"));
	}

	@Test
	public void groupsIntoMapsShoudldReturnEmptyMapIfNoMatch() {
		List<Map<String, String>> list = REGEX_WITH_GROUPS.groupsIntoMaps(NOT_MATCHING_INPUT);
		assertNotNull(list);
		assertTrue(list.isEmpty());
	}

	@Test
	public void groupsIntoMapsShouldReturnAllMatches() {
		List<Map<String, String>> list = REGEX_WITH_GROUPS.groupsIntoMaps(MATCHING_INPUT);
		assertEquals(2, list.size());

		Map<String, String> map;

		map = list.get(0);
		assertEquals("23", map.get("old"));
		assertEquals("42", map.get("new"));
		assertEquals("23", map.get("1"));
		assertEquals("42", map.get("2"));

		map = list.get(1);
		assertEquals("0", map.get("old"));
		assertEquals("1", map.get("new"));
		assertEquals("0", map.get("1"));
		assertEquals("1", map.get("2"));
	}

}
