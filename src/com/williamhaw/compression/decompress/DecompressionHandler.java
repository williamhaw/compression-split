package com.williamhaw.compression.decompress;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.williamhaw.compression.compress.CompressionHandler;
import com.williamhaw.compression.file.structure.FileStructure;

/**
 * Handles concatenation of split files and passes to FileCompression class to be decompressed
 * @author williamhaw
 *
 */
public class DecompressionHandler {
	
	FileDecompression decompression;
	
	public DecompressionHandler(FileDecompression decompression) {
		this.decompression = decompression;
	}
	
	public void decompress(FileStructure fileStructure, File sourceDirectory, File targetRootDirectory) {
		try {
			File concatenatedFile = File.createTempFile(String.valueOf(fileStructure.getFileNumber()) + ".compressed", ".tmp");
			
			int sequenceNumber = 0;
			
			FileChannel concatenatedChannel = FileChannel.open(Paths.get(concatenatedFile.toURI()), StandardOpenOption.WRITE);
			
			File part = new File(sourceDirectory, String.valueOf(fileStructure.getFileNumber()) + CompressionHandler.SEPARATOR + String.valueOf(sequenceNumber));
			
			long bytesTransferred = 0;
			
			while(part.exists()) {
				FileChannel partChannel = FileChannel.open(Paths.get(part.toURI()));
				
				concatenatedChannel.transferFrom(partChannel, bytesTransferred, partChannel.size());
				
				bytesTransferred += partChannel.size();
				sequenceNumber++;
				partChannel.close();
				part = new File(sourceDirectory, String.valueOf(fileStructure.getFileNumber()) + CompressionHandler.SEPARATOR + String.valueOf(sequenceNumber));
			}
			concatenatedChannel.close();
			
			decompression.decompress(fileStructure, concatenatedFile, new File(targetRootDirectory, fileStructure.getRelativePath()));
			
			concatenatedFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
