package com.kunlun.poker.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HTTPUtil {
	public static String readFromURL(String strURL) throws IOException {
		StringBuilder sb = new StringBuilder();
		URL url = new URL(strURL);
		try (InputStream is = url.openStream();
				InputStreamReader isr = new InputStreamReader(is);) {
			int chr;
			while ((chr = isr.read()) != -1)
				sb.append((char) chr);

		}

		return sb.toString();
	}
}
