package com.williamhaw.compression.compress;

import java.io.File;
import java.io.IOException;

import com.williamhaw.compression.utils.CompressionType;

/**
 * Common interface so that different compression algorithms can be used by CompressionHandler
 * @author williamhaw
 *
 */
public interface FileCompression {	
	/**
	 * Performs compression of entire file into a temporary file with a unique fileNumber
	 * @param toBeCompressed
	 * @param fileNumber unique across all files
	 * @return temporary file with compressed contents of toBeCompressed
	 * @throws IOException
	 */
	public File compress(File toBeCompressed, int fileNumber) throws IOException;
	
	/**
	 * @return type of compression
	 */
	public CompressionType type();
}
