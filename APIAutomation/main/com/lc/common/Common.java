package com.lc.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Common {
	
	public static StringBuilder errorBuilder = new StringBuilder("<br><p align=\"center\"><b>Detailed Log Summary For Failed Tests<b></p><br><br><table border=\"1\" style=\"width:90%\"  bgcolor=\"#4169E1\"><th>S.No</th><th>Class Name</th><th>Test Case Name</th><th>Log</th>");
	public static ArrayList<Api> apiLogInfo = new ArrayList<Api>();
	public static int failedTestCount = 1;
	public static String logFileName = System.getProperty("user.dir") + File.separator + "log.html";
	
	public static void createLogFile(String fileName, String content) {
		try {
			File file = new File(fileName);

			// if file already exists, then delete it
			if (file.exists()) {
				file.delete();
			}

			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
