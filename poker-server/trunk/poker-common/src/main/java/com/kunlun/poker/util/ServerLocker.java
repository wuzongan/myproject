package com.kunlun.poker.util;

import java.io.File;
import java.lang.management.ManagementFactory;

public class ServerLocker {
	public static boolean lock(String fileName, int port)
	{
		if(fileName == null) return true;
		
		File file = new File(System.getProperty("user.dir") + File.separator
				+ fileName);
		
		if(file.exists())
			return false;

		String pidId = ManagementFactory.getRuntimeMXBean().getName()
				.split("@")[0];
		StringBuilder sb = new StringBuilder();
		sb.append("PID=").append(pidId).append("\n").append("PORT=" + port);
		FileUtils.writeFileContent(file, sb.toString());
		file.deleteOnExit();
		
		return true;
	}
}
