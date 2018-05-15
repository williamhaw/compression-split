package com.williamhaw.compression.compress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Paths;

/**
 * Performs the compression to a temporary file to be split up later.
 * <p>
 * Inheriting classes must use a buffered OutputStream to avoid loading the entire file into memory
 * @author williamhaw
 *
 */
public abstract class AbstractCompression implements FileCompression {	
	
	public File compress(File toBeCompressed, long fileNumber) throws IOException{
		
		File tempFile = File.createTempFile(String.valueOf(fileNumber) + ".compressed", ".tmp");
		System.out.println("Opening temp file at " + tempFile);
		OutputStream compressedOutputStream = getCompressedOutputStream(new FileOutputStream(tempFile));
		WritableByteChannel out = Channels.newChannel(compressedOutputStream);
		
		FileChannel fc = FileChannel.open(Paths.get(toBeCompressed.toURI()));
		
		
		ByteBuffer bb = ByteBuffer.allocate(2048);
		
		while(fc.read(bb) != -1) {
			bb.flip();
			out.write(bb);
			bb.clear();
		}
		out.close();
		System.out.println("Wrote compressed contents to temp file at " + tempFile);
		return tempFile;
	}
	
	protected abstract OutputStream getCompressedOutputStream(FileOutputStream fileOutputStream) throws IOException;

}
