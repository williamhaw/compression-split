package com.williamhaw.compression.compress;

import com.williamhaw.compression.compress.impl.GZipCompression;
import com.williamhaw.compression.decompress.FileDecompression;
import com.williamhaw.compression.decompress.impl.GZIPDecompression;
import com.williamhaw.compression.utils.CompressionType;

/**
 * Factory for compression and decompression instances given compression type
 * @author williamhaw
 *
 */
public class CompressionFactory {
	public FileCompression getCompression(CompressionType type) {
		switch (type) {
		case GZIP:
			return new GZipCompression();
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	public FileDecompression getFileDecompression(CompressionType type) {
		switch (type) {
		case GZIP:
			return new GZIPDecompression();
		default:
			throw new UnsupportedOperationException();
		}
	}
}
