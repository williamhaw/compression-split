package com.williamhaw.compression.decompress;

import java.io.File;
import java.io.IOException;

import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.utils.CompressionType;

/**
 * Common interface so that different decompression algorithms can be used by DecompressionHandler
 * @author williamhaw
 *
 */
public interface FileDecompression {
	
	/**
	 * @param fileStructure required to get fileNumber of compressed files
	 * @param toBeDecompressed file concatenated from parts to be decompressed
	 * @param targetDirectory directory to write decompressed file to
	 */
	public void decompress(FileStructure fileStructure, File toBeDecompressed, File targetDirectory) throws IOException;
	
	public CompressionType type();
}
