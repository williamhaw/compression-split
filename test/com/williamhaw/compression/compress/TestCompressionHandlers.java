package com.williamhaw.compression.compress;

import java.io.File;
import java.io.IOException;
import com.williamhaw.compression.compress.impl.GZipCompression;
import com.williamhaw.compression.decompress.DecompressionHandler;
import com.williamhaw.compression.decompress.impl.GZIPDecompression;
import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.file.structure.FileStructure.Type;

public class TestCompressionHandlers {
	public static void main(String[] args) {
		try {
			File sourceDirectory = new File(new File("testfiles"), "source");
			sourceDirectory.mkdirs();
			File testFile1 = TestFileUtils.makeFile(sourceDirectory, "1", 100000);
			
			GZipCompression compression = new GZipCompression();
			
			CompressionHandler compressionHandler = new CompressionHandler(compression);
			
			File compressedDirectory = new File(new File("testfiles"), "compressed");
			compressedDirectory.mkdirs();
			
			compressionHandler.compress(1, testFile1, compressedDirectory, 1);
			
			File targetDirectory = new File(new File("testfiles"), "target");
			targetDirectory.mkdirs();
			
			
			GZIPDecompression decompression = new GZIPDecompression();
			DecompressionHandler decompressionHandler = new DecompressionHandler(decompression);
			FileStructure fs = new FileStructure();
			fs.setFileNumber(1);
			fs.setName("1");
			fs.setRelativePath("");
			fs.setType(Type.FILE);
			
			decompressionHandler.decompress(fs, compressedDirectory, targetDirectory);
			File testFile1Decompressed = new File(targetDirectory, "1");
			System.out.println(TestFileUtils.isIdentical(testFile1, testFile1Decompressed));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
