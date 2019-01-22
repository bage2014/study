package org.sagebionetworks.schema.generator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Simple helper for basic file operations.
 * 
 * @author John
 * 
 */
public class FileUtils {

	/**
	 * The recursive iterator can be used to walk all files in a directory tree.
	 * Children will appear before parent directories so this can be used for
	 * recursive deletes.
	 * 
	 * @param root
	 * @param filter
	 * @return
	 */
	public static Iterator<File> getRecursiveIterator(File root,
			FileFilter filter) {
		// Start with a list
		List<File> list = new LinkedList<File>();
		// Build up the list
		addAllChildren(root, filter, list);
		return list.iterator();
	}

	/**
	 * Helper recursive method to build up a list of file from.
	 * 
	 * @param root
	 * @param filter
	 * @param list
	 */
	private static void addAllChildren(File root, FileFilter filter,
			List<File> list) {
		if (root.isDirectory()) {
			// List all files and directories without filtering.
			File[] array = root.listFiles();
			for (File child : array) {
				addAllChildren(child, filter, list);
			}
		}
		// Add this to the list if accepted.
		if (filter == null || filter.accept(root)) {
			list.add(root);
		}
	}

	/**
	 * Read a file into a string.
	 * 
	 * @param toLoad
	 * @return
	 * @throws IOException
	 */
	public static String readToString(File toLoad) throws IOException {
		if (toLoad == null)
			throw new IllegalArgumentException("File cannot be null");
		InputStream in = new FileInputStream(toLoad);
		return readToString(in);
	}

	/**
	 * Read an input stream into a string.
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String readToString(InputStream in) throws IOException {
		try {
			BufferedInputStream bufferd = new BufferedInputStream(in);
			byte[] buffer = new byte[1024];
			StringBuilder builder = new StringBuilder();
			int index = -1;
			while ((index = bufferd.read(buffer, 0, buffer.length)) > 0) {
				builder.append(new String(buffer, 0, index, "UTF-8"));
			}
			return builder.toString();
		} finally {
			in.close();
		}
	}

	/**
	 * Recursively delete a directory and all sub-directories.
	 * 
	 * @param directory
	 */
	public static void recursivelyDeleteDirectory(File directory) {
		// Get the recursive iterator
		Iterator<File> it = getRecursiveIterator(directory, null);
		while (it.hasNext()) {
			it.next().delete();
		}
	}

	/**
	 * Create a temporary directory using the given name
	 * 
	 * @return
	 * @throws IOException
	 */
	public static File createTempDirectory(String name) throws IOException {
		// Start will a temp file
		File temp = File.createTempFile(name, "");
		// Delete the file
		temp.delete();
		// Convert it a directory
		temp.mkdir();
		return temp;
	}

}
