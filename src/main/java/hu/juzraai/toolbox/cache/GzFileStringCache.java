package hu.juzraai.toolbox.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import hu.juzraai.toolbox.test.Check;

public class GzFileStringCache implements Cache<String> {
	// TODO test i/o
	// TODO log
	// TODO doc

	private final File directory;

	public GzFileStringCache(File directory) {
		Check.notNull(directory, "directory must not be null");
		this.directory = directory;
	}

	@Override
	public boolean contains(String id) {
		return id2File(id).exists(); // checks performed inside
	}

	@Override
	public String fetch(String id) {
		File file = id2File(id); // checks performed inside
		String content = null;
		if (file.exists()) {
			try (FileInputStream fis = new FileInputStream(file);
					InputStream gis = new GZIPInputStream(fis);
					Scanner s = new Scanner(gis, "UTF8")) {
				content = s.useDelimiter("\\Z").next();
			} catch (IOException e) {
				file.delete();
				// TODO log
				e.printStackTrace();
			}
		}
		return content;
	}

	public File getDirectory() {
		return directory;
	}

	protected File id2File(String id) {
		Check.notNull(id, "id must not be null");
		Check.argument(id.matches("[A-Za-z0-9_.\\-\\/\\\\]+"), "id must contain characters valid in a filename");
		// TODO test pattern!
		return new File(directory, id);
	}

	protected void mkdirsForFile(File file) {
		File parent = file.getParentFile();
		if (null != parent) {
			parent.mkdirs();
		}
	}

	@Override
	public void remove(String key) {
		id2File(key).delete();
	}

	@Override
	public boolean store(String id, String content) {
		File file = id2File(id);
		mkdirsForFile(file);
		try (FileOutputStream fos = new FileOutputStream(file);
				OutputStream gos = new GZIPOutputStream(fos);
				Writer w = new OutputStreamWriter(gos, "UTF-8")) {
			w.write(content);
			w.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
