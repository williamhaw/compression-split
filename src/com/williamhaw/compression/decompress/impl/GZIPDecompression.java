package com.williamhaw.compression.decompress.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import com.williamhaw.compression.decompress.AbstractDecompression;
import com.williamhaw.compression.utils.CompressionType;

/**
 * Specifies that GZIPInputStream is to be used
 * @author williamhaw
 *
 */
public class GZIPDecompression extends AbstractDecompression {

	@Override
	protected InputStream getDecompressedInputStream(FileInputStream fileInputStream) throws IOException{
		return new GZIPInputStream(fileInputStream);
	}
	
	@Override
	public CompressionType type() {
		return CompressionType.GZIP;
	}

}
