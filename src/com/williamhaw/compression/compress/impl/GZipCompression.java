package com.williamhaw.compression.compress.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import com.williamhaw.compression.compress.AbstractCompression;
import com.williamhaw.compression.compress.FileCompression;
import com.williamhaw.compression.utils.CompressionType;

/**
 * Specifies that GZIPOutputStream is to be used
 * @author williamhaw
 *
 */
public class GZipCompression extends AbstractCompression implements FileCompression{
	
	public static int GZIP_BUFFER_SIZE = 64 * 1024;
	
	@Override
	protected OutputStream getCompressedOutputStream(FileOutputStream fileOutputStream) throws IOException{
		return new GZIPOutputStream(fileOutputStream, GZIP_BUFFER_SIZE);
	}	

	@Override
	public CompressionType type() {
		return CompressionType.GZIP;
	}

}
