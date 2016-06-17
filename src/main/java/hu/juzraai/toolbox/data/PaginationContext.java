package hu.juzraai.toolbox.data;


import hu.juzraai.toolbox.test.Check;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility which helps you in calculations related to pagination links. The
 * input is the record count and the number of records per page, {@link
 * PaginationContext} can calculate record offset for a page index, the page
 * count, and can generate pager links too.
 *
 * @author Zsolt Jurányi
 * @since 16.07
 */
public class PaginationContext {

	private final int recordCount;
	private final int recordsPerPage;
	private final int pageCount;

	/**
	 * Creates a new instance.
	 *
	 * @param recordCount    Total number of records
	 * @param recordsPerPage Number of records displayed per page
	 */
	public PaginationContext(int recordCount, int recordsPerPage) {
		Check.argument(0 <= recordCount, "recordCount must be non-negative");
		Check.argument(0 < recordsPerPage, "recordsPerPage must be positive");
		this.recordCount = recordCount;
		this.recordsPerPage = recordsPerPage;
		this.pageCount = (int) Math.ceil((double) recordCount / (double) recordsPerPage);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PaginationContext)) return false;
		PaginationContext that = (PaginationContext) o;
		if (recordCount != that.recordCount) return false;
		return recordsPerPage == that.recordsPerPage;
	}

	/**
	 * For a given page index this method calculates the offset you can use e.g.
	 * in an SQL query to fetch records of the given page. It's simply
	 * calculated by the formula <code>pageIndex * recordsPerPage</code>.
	 *
	 * @param pageIndex Index of the page you need offset for
	 * @return Offset to be used in query to fetch records for the page
	 */
	public int getOffset(int pageIndex) {
		Check.argument(0 <= pageIndex, "pageIndex must be non-negative");
		Check.argument(pageIndex < pageCount, "pageIndex must be less than pageCount");
		return pageIndex * recordsPerPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	/**
	 * Generates a list of page indexes to be shown. The list will contain at
	 * most 3 sections: <ol> <li>The first page indexes from 0 till
	 * <code>width-1</code></li> <li>A sequence of page indexes with a length of
	 * <code>2*width-1</code>, where the element in the middle is
	 * <code>currentPageIndex</code></li> <li>The last page indexes from
	 * <code>pageCount-width</code> till <code>pageCount-1</code></li></ol>
	 * <p>
	 * It also generates a <code>null</code> element between sections, if the
	 * two section doesn't have any common element. At these places you can
	 * generate a "..." in your view.
	 *
	 * @param width            Width of each pagination link section
	 * @param currentPageIndex Index of the current page
	 * @return A list of page indexes to be displayed
	 */
	@Nonnull
	public List<Integer> getPagesToShow(int width, int currentPageIndex) {
		Check.argument(0 < width, "width must be positive");
		Check.argument(0 <= currentPageIndex, "currentPageIndex must be non-negative");
		Check.argument(currentPageIndex < pageCount, "currentPageIndex must be less than pageCount");
		List<Integer> pages = new ArrayList<>();
		int prev = -1;
		for (int p = 0; p < pageCount; p++) {
			if (p < width || Math.abs(p - currentPageIndex) < width || p >= pageCount - width) {
				if (p - prev > 1) {
					pages.add(null);
				}
				pages.add(p);
				prev = p;
			}
		}
		return pages;
	}

	/**
	 * @return Total number of records
	 */
	public int getRecordCount() {
		return recordCount;
	}

	/**
	 * @return Number of records displayed per page
	 */
	public int getRecordsPerPage() {
		return recordsPerPage;
	}

	@Override
	public int hashCode() {
		int result = recordCount;
		result = 31 * result + recordsPerPage;
		return result;
	}

	@Override
	@Nonnull
	public String toString() {
		return "PaginationContext{" +
				"recordCount=" + recordCount +
				", recordsPerPage=" + recordsPerPage +
				'}';
	}
}
