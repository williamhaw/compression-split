package com.williamhaw.compression.utils;

import java.io.File;

/**
 * Utility validation methods
 * @author williamhaw
 *
 */
public class Validation {
	public static boolean isValidPath(String testPath) {
		File testFile = new File(testPath);
		return testFile.exists();
	}
	
	public static boolean isInteger(String testInteger) {
		try {
			Integer.valueOf(testInteger);
			return true;
		}catch (NumberFormatException e) {
			return false;
		}
	}
}
