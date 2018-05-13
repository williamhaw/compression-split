package com.williamhaw.compression.compress;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

/**
 * Handles splitting of files using given Compression class
 * @author williamhaw
 *
 */
public class CompressionHandler {
	public static final int MEGABYTE = 1024 * 1024;
	public static final int DEFAULT_BUFFER_SIZE = 16 * 1024;
	public static final String SEPARATOR = ".compressed.";
	private FileCompression compression;
	
	public CompressionHandler(FileCompression compression){
		this.compression = compression;
	}

	public void compress(int fileNumber, File toBeCompressed, File targetDirectory, int limitMB) {
		try {
			System.out.println("Compressing file at " + toBeCompressed);
			File tempFile = compression.compress(toBeCompressed, fileNumber);
			try(RandomAccessFile compressedFile = new RandomAccessFile(tempFile, "r")){
				long originalFileSizeBytes = Files.size(tempFile.toPath());
				long bytesPerOutputFile = limitMB * MEGABYTE;
				long numberOfSplits = originalFileSizeBytes / bytesPerOutputFile;
				long remainingBytes = originalFileSizeBytes % bytesPerOutputFile;
				
				
				FileChannel readChannel = compressedFile.getChannel();
				int sequenceNumber;
				for(sequenceNumber = 0; sequenceNumber < numberOfSplits; sequenceNumber++) {
					String truncatedFileName = String.valueOf(fileNumber) + SEPARATOR + String.valueOf(sequenceNumber);
					writePart(truncatedFileName, bytesPerOutputFile, sequenceNumber * bytesPerOutputFile, readChannel, targetDirectory);
				}
				
				if(remainingBytes > 0) {
					String truncatedFileName = String.valueOf(fileNumber) + SEPARATOR + String.valueOf(sequenceNumber);
					writePart(truncatedFileName, remainingBytes, sequenceNumber * bytesPerOutputFile, readChannel, targetDirectory);
				}
			}
			System.out.println("Deleting temp file at " + tempFile);
			tempFile.delete();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void writePart(String filename, long numberOfBytes, long readPosition, FileChannel readChannel, File targetDirectory) throws IOException {
		File partFile = new File(targetDirectory, filename);
		try(RandomAccessFile writeFile = new RandomAccessFile(partFile, "rw")){
			System.out.println("Writing to split file " + partFile);
			FileChannel writeChannel = writeFile.getChannel();
			readChannel.position(readPosition);
			writeChannel.transferFrom(readChannel, 0, numberOfBytes);
		}
	}
}
