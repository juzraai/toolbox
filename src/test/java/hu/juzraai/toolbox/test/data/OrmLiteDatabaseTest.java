package hu.juzraai.toolbox.test.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import hu.juzraai.toolbox.data.Identifiable;
import hu.juzraai.toolbox.data.OrmLiteDatabase;
import hu.juzraai.toolbox.jdbc.ConnectionString;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Testing {@link OrmLiteDatabase} with in-memory SQLite database.
 *
 * @author Zsolt Jurányi
 * @see OrmLiteDatabase
 * @since 16.07
 */
public class OrmLiteDatabaseTest {

	private OrmLiteDatabase DB;

	@Before
	public void createDatabase() throws SQLException {
		DB = OrmLiteDatabase.build(ConnectionString.SQLITE().build(), null, null);
	}

	@After
	public void destroyDatabase() throws Exception {
		DB.close();
	}

	@Test
	public void fetchShouldReturnNullForNonExistentId() throws SQLException {
		DB.createTables(MyEntity.class);
		assertNull(DB.fetch(MyEntity.class, 1));
	}

	@Test
	public void fetchShouldReturnStoredValue() throws SQLException {
		DB.createTables(MyEntity.class);
		MyEntity e = new MyEntity(1, "test");
		DB.store(e);
		assertEquals(e, DB.fetch(MyEntity.class, 1));
	}

	@Test(expected = SQLException.class)
	public void fetchShouldThrowExceptionForNonExistentTable() throws SQLException {
		DB.fetch(MyEntity.class, 1);
	}

	@DatabaseTable
	public static class MyEntity implements Identifiable<Integer> {

		@DatabaseField(id = true)
		private Integer id;
		@DatabaseField
		private String s;

		public MyEntity() {
			// needed by ORMlite
		}

		public MyEntity(Integer id, String s) {
			this.id = id;
			this.s = s;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof MyEntity)) return false;

			MyEntity myEntity = (MyEntity) o;

			if (id != null ? !id.equals(myEntity.id) : myEntity.id != null)
				return false;
			return s != null ? s.equals(myEntity.s) : myEntity.s == null;

		}

		public Integer getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getS() {
			return s;
		}

		public void setS(String s) {
			this.s = s;
		}

		@Override
		public int hashCode() {
			int result = id != null ? id.hashCode() : 0;
			result = 31 * result + (s != null ? s.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "MyEntity{" +
					"id=" + id +
					", s='" + s + '\'' +
					'}';
		}
	}
}
