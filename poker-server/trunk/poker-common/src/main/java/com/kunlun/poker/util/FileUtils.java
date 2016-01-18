package com.kunlun.poker.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	private static final Logger logger = LoggerFactory
			.getLogger(FileUtils.class);

	public static String getFileContent(File file, String charset) {
		byte[] bytes = getFileBytes(file);

		if (bytes.length >= 3) {
			if (bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB
					&& bytes[2] == (byte) 0xBF) {
				byte[] newBytes = new byte[bytes.length - 3];
				System.arraycopy(bytes, 3, newBytes, 0, newBytes.length);
				bytes = newBytes;
			}
		}

		if (bytes.length == 0) {
			return "";
		}

		try {
			return new String(bytes, charset);
		} catch (IOException e) {
			return null;
		}
	}

	public static String getFileContent(File file) {
		return getFileContent(file, "utf-8");
	}

	public static byte[] getFileBytes(File file) {
		byte[] bytes = null;
		try (FileInputStream input = new FileInputStream(file)) {
			bytes = new byte[input.available()];
			input.read(bytes);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return bytes;
	}

	public static void writeFileBytes(File file, byte[] bytes) {
		try (FileOutputStream output = new FileOutputStream(file)) {
			output.write(bytes);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void copy(File src, File dist) {
		if (src.isDirectory()) {
			if (!dist.exists()) {
				dist.mkdirs();
			}

			File[] subFiles = src.listFiles();
			for (File subFile : subFiles) {
				copy(subFile, new File(dist.getAbsolutePath() + File.separator
						+ subFile.getName()));
			}
		} else {
			writeFileBytes(dist, getFileBytes(src));
		}
	}

	public static void writeFileContent(File file, String content) {
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		try (OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(file), "utf-8")) {
			writer.write(content);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static String getBaseName(File file) {
		String filename = file.getName();
		int p = filename.indexOf('.');
		if (p == -1) {
			return filename;
		}

		return filename.substring(0, p);
	}

	public static String getExtensionName(File file) {
		String filename = file.getName();
		int p = filename.indexOf('.');
		if (p == -1) {
			return "";
		}

		return filename.substring(p + 1);
	}

	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		byte buffer[] = new byte[1024];
		int len;
		try (FileInputStream in = new FileInputStream(file)) {
			digest = MessageDigest.getInstance("MD5");
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

}
