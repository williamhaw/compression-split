package com.williamhaw.compression.compress;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class TestFileUtils {
	public static File makeFile(File directory, String filename, int sizeKB) throws IOException{
		
		File ret = new File(directory, filename);
		
		FileOutputStream fos = new FileOutputStream(ret);

		for(int i = 0; i < sizeKB; i++) {
			byte[] randomBuffer = new byte[1024];
			Random random = new Random(System.currentTimeMillis());
			random.nextBytes(randomBuffer);
			fos.write(randomBuffer);
		}
		
		fos.close();
		
		return ret;
	}
	
	public static boolean isIdentical(File file1, File file2) throws IOException {
		if(!file1.exists())
			return false;
		if(!file2.exists())
			return false;
		if(file1.length() != file2.length())
			return false;

		try(
			BufferedInputStream f1is = new BufferedInputStream(new FileInputStream(file1), 16 * 1024);
			BufferedInputStream f2is = new BufferedInputStream(new FileInputStream(file2), 16 * 1024);
		){
			int comparef1;
			int comparef2;
			while((comparef1 = f1is.read()) != -1) {
				comparef2 = f2is.read();
				if(comparef1 != comparef2)
					return false;
			}
		}
		return true;
	}
}
