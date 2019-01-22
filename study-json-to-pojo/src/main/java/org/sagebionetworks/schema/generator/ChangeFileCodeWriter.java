package org.sagebionetworks.schema.generator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

public class ChangeFileCodeWriter extends CodeWriter {

	/** The target directory to put source code. */
	private final File target;
	private final StringBuilder log;

	public class WriteOnChangedOutputStream extends OutputStream {

		MessageDigest md5;
		ByteArrayOutputStream newdata;
		DigestOutputStream digestOutputStream;
		File file;

		public WriteOnChangedOutputStream(File file) throws IOException {
			this.file = file;
			try {
				this.md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new IOException(e.getMessage(), e);
			}
			this.newdata = new ByteArrayOutputStream(8000);
			this.digestOutputStream = new DigestOutputStream(newdata, md5);
		}

		@Override
		public void write(int b) throws IOException {
			this.digestOutputStream.write(b);
		}

		@Override
		public void close() throws IOException {
			super.close();
			// write to file only if there are changes
			if (file.exists()) {
				if (newdata.size() == file.length()) {
					byte[] md5new = digestOutputStream.getMessageDigest().digest();
					byte[] buffer = new byte[(int) file.length()];
					DigestInputStream digestOldFile = new DigestInputStream(new FileInputStream(file), md5);
					try {
						while (digestOldFile.read(buffer) >= 0) {
						}
					} finally {
						digestOldFile.close();
					}
					byte[] md5old = digestOldFile.getMessageDigest().digest();
					if (MessageDigest.isEqual(md5new, md5old)) {
						log.append("Not overwriting " + file.toString() + " because it did not change\n");
						return;
					}
				}
			}

			// if we get here, we need to write out our data to the file
			FileOutputStream out = new FileOutputStream(file);
			try {
				newdata.writeTo(out);
			} finally {
				out.close();
			}
		}
	}

	public ChangeFileCodeWriter(File target, StringBuilder log) throws IOException {
		this.target = target;
		this.log = log;
	}

	@Override
	public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
		return new WriteOnChangedOutputStream(getFile(pkg, fileName));
	}

	protected File getFile(JPackage pkg, String fileName) throws IOException {
		File dir;
		if (pkg.isUnnamed())
			dir = target;
		else
			dir = new File(target, toDirName(pkg));

		if (!dir.exists())
			dir.mkdirs();

		return new File(dir, fileName);
	}

	public void close() throws IOException {
	}

	/** Converts a package name to the directory name. */
	private static String toDirName(JPackage pkg) {
		return pkg.name().replace('.', File.separatorChar);
	}
}
