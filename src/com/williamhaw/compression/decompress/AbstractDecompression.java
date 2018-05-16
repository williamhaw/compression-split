package com.williamhaw.compression.decompress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.williamhaw.compression.file.structure.FileStructure;


/**
 * Performs the decompression from a temporary file to the final destination
 * <p>
 * Inheriting classes must use a buffered InputStream to avoid loading the entire file into memory
 * @author williamhaw
 *
 */
public abstract class AbstractDecompression implements FileDecompression{
	public void decompress(FileStructure fileStructure, File toBeDecompressed, File targetFile) throws IOException {
		if(fileStructure.getFileNumber() == FileStructure.NOT_A_FILE)
			throw new IllegalArgumentException("Illegal file number found for " + fileStructure);
		
		InputStream decompressedInputStream = getDecompressedInputStream(new FileInputStream(toBeDecompressed));
		ReadableByteChannel in = Channels.newChannel(decompressedInputStream);		

		System.out.println("Decompressing file to " + targetFile);
		targetFile.createNewFile();
		WritableByteChannel out = Channels.newChannel(new FileOutputStream(targetFile));
		ByteBuffer bb = ByteBuffer.allocate(16 * 1024);
		
		while(in.read(bb) != -1) {
			bb.flip();
			out.write(bb);
			bb.clear();
		}
		in.close();
		out.close();
	};
	
	protected abstract InputStream getDecompressedInputStream(FileInputStream fileInputStream) throws IOException;
}
