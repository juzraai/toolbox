package hu.juzraai.toolbox.test.data;

import hu.juzraai.toolbox.data.PaginationContext;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class PaginationContextTest {

	// TODO test getPagesToShow
	// checks
	// 0 page - empty
	// 1 page - 0
	// 2 page - 0,1
	// 3 page width 1, current 0 - 0,null,2
	// 5 page width 1, current 2 - 0,null,2,null

	private static final List<PagesToShowTestCase> pagesToShowTestCases = new ArrayList<>();

	@BeforeClass
	public static void loadPagesToShowTestCases() {
		InputStream is = PaginationContextTest.class.getClassLoader().getResourceAsStream("pager-cases.txt");
		try (Scanner s = new Scanner(is, "UTF-8")) {
			while (s.hasNextLine()) {
				String line = s.nextLine().replaceAll(" +", " ").trim();
				if (!line.isEmpty() && !line.startsWith("#")) {
					String[] parts = line.split(" ");
					PagesToShowTestCase testCase = new PagesToShowTestCase(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
					for (String item : parts[3].split(",")) {
						testCase.pagesToShow.add(item.isEmpty() ? null : Integer.parseInt(item));
					}
					pagesToShowTestCases.add(testCase);
				}
			}
		}
	}

	@Test
	public void _pagesToShowShouldWorkFine() {
		for (PagesToShowTestCase testCase : pagesToShowTestCases) {
			PaginationContext p = new PaginationContext(testCase.pageCount, 1);
			try {
				assertEquals(testCase.pageCount, p.getPageCount());
				assertEquals(testCase.pagesToShow, p.getPagesToShow(testCase.width, testCase.currentPageIndex));
			} catch (AssertionError e) {
				System.out.println("Page count: " + testCase.pageCount);
				System.out.println("Current page: " + testCase.currentPageIndex);
				throw e;
			}
		}
	}

	@Test
	public void constructorMustAccept0RecordCount() {
		new PaginationContext(0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorMustThrowExceptionFor0RecordsPerPage() {
		new PaginationContext(1, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorMustThrowExceptionForNegativeRecordCount() {
		new PaginationContext(-1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorMustThrowExceptionForNegativeRecordsPerPage() {
		new PaginationContext(1, -1);
	}

	@Test
	public void getOffsetShouldAccept0Index() {
		new PaginationContext(1, 5).getOffset(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getOffsetShouldThrowExceptionForIndexGreaterThenPC() {
		new PaginationContext(1, 5).getOffset(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getOffsetShouldThrowExceptionForNegativeIndex() {
		new PaginationContext(1, 5).getOffset(-1);
	}

	@Test
	public void getRecordCountShouldReturnRecordCount() {
		assertEquals(42, new PaginationContext(42, 10).getRecordCount());
	}

	@Test
	public void getRecordsPerPageShouldReturnRecordsPerPage() {
		assertEquals(10, new PaginationContext(42, 10).getRecordsPerPage());
	}

	@Test
	public void offsetShouldBe0On1stPage() {
		assertEquals(0, new PaginationContext(1, 5).getOffset(0));
	}

	@Test
	public void offsetShouldBeIndexMulRPP() {
		assertEquals(5, new PaginationContext(20, 5).getOffset(1));
		assertEquals(10, new PaginationContext(20, 5).getOffset(2));
		assertEquals(15, new PaginationContext(20, 5).getOffset(3));
	}

	@Test
	public void pageCountShouldBe0WhenRCIs0() {
		assertEquals(0, new PaginationContext(0, 5).getPageCount());
	}

	@Test
	public void pageCountShouldBe1WhenRCEqualsRPP() {
		assertEquals(1, new PaginationContext(5, 5).getPageCount());
	}

	@Test
	public void pageCountShouldBe1WhenRCIsLessThanRPP() {
		assertEquals(1, new PaginationContext(1, 5).getPageCount());
		assertEquals(1, new PaginationContext(4, 5).getPageCount());
	}

	@Test
	public void pageCountShouldIncreaseWhenRCIsOverRPP() {
		assertEquals(2, new PaginationContext(6, 5).getPageCount());
		assertEquals(2, new PaginationContext(10, 5).getPageCount());
		assertEquals(3, new PaginationContext(11, 5).getPageCount());
	}

	private static class PagesToShowTestCase {
		public final List<Integer> pagesToShow = new ArrayList<>();
		public int pageCount, currentPageIndex, width;

		public PagesToShowTestCase(int pageCount, int currentPageIndex, int width) {
			this.pageCount = pageCount;
			this.currentPageIndex = currentPageIndex;
			this.width = width;
		}

		@Override
		public String toString() {
			return "PagesToShowTestCase{" +
					"pageCount=" + pageCount +
					", currentPageIndex=" + currentPageIndex +
					", width=" + width +
					", pagesToShow=" + pagesToShow +
					'}';
		}
	}
}
