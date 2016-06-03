package hu.juzraai.toolbox.data;


import hu.juzraai.toolbox.test.Check;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zsolt Jurányi
 * @since 16.07
 */
public class PaginationContext {
	// TODO doc

	private final int recordCount;
	private final int recordsPerPage;
	private final int pageCount;

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

	public int getOffset(int pageIndex) {
		Check.argument(0 <= pageIndex, "pageIndex must be non-negative");
		Check.argument(pageIndex < pageCount, "pageIndex must be less than pageCount");
		return pageIndex * recordsPerPage;
	}

	public int getPageCount() {
		return pageCount;
	}

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

	public int getRecordCount() {
		return recordCount;
	}

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
