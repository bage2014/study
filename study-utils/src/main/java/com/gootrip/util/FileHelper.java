package com.gootrip.util;

/*
 * Static File routines.
 * Copyright (C) 2002 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Java+Utilities
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */

import java.io.*;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Locale;

/**
 * Utilities for File manipulation.
 * More information about this class is available from <a target="_top" href=
 * "http://ostermiller.org/utils/FileHelper.html">ostermiller.org</a>.
 *
 * @author Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 * @since ostermillerutils 1.00.00
 */
public class FileHelper {

	/**
	 * Locale specific strings displayed to the user.
	 *
	 * @since ostermillerutils 1.00.00
	 */
	protected static ResourceBundle labels = ResourceBundle.getBundle("com.Ostermiller.util.FileHelper",  Locale.getDefault());


	/**
	 * Move a file from one location to another.  An attempt is made to rename
	 * the file and if that fails, the file is copied and the old file deleted.
	 *
	 * If the destination file already exists, an exception will be thrown.
	 *
	 * @param from file which should be moved.
	 * @param to desired destination of the file.
	 * @throws IOException if an error occurs.
	 *
	 * @since ostermillerutils 1.00.00
	 */
	public static void move(File from, File to) throws IOException {
		move(from, to, false);
	}

	/**
	 * Move a file from one location to another.  An attempt is made to rename
	 * the file and if that fails, the file is copied and the old file deleted.
	 *
	 * @param from file which should be moved.
	 * @param to desired destination of the file.
	 * @param overwrite If false, an exception will be thrown rather than overwrite a file.
	 * @throws IOException if an error occurs.
	 *
	 * @since ostermillerutils 1.00.00
	 */
	public static void move(File from, File to, boolean overwrite) throws IOException {
		if (to.exists()){
			if (overwrite){
				if (!to.delete()){
					throw new IOException(
						MessageFormat.format(
							labels.getString("deleteerror"),
							(Object[])new String[] {
								to.toString()
							}
						)
					);
				}
			} else {
				throw new IOException(
					MessageFormat.format(
						labels.getString("alreadyexistserror"),
						(Object[])new String[] {
							to.toString()
						}
					)
				);
			}
		}

		if (from.renameTo(to)) return;

		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			copy(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
			if (!from.delete()){
				throw new IOException(
					MessageFormat.format(
						labels.getString("deleteoriginalerror"),
						(Object[])new String[] {
							from.toString(),
							to.toString()
						}
					)
				);
			}
		} finally {
			if (in != null){
				in.close();
				in = null;
			}
			if (out != null){
				out.flush();
				out.close();
				out = null;
			}
		}
	}

	/**
	 * Buffer size when reading from input stream.
	 *
	 * @since ostermillerutils 1.00.00
	 */
	private final static int BUFFER_SIZE = 1024;

	/**
	 * Copy the data from the input stream to the output stream.
	 *
	 * @param in data source
	 * @param out data destination
	 * @throws IOException in an input or output error occurs
	 *
	 * @since ostermillerutils 1.00.00
	 */
	private static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}
}
