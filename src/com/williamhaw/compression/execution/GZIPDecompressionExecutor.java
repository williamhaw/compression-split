package com.williamhaw.compression.execution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;

import com.williamhaw.compression.file.DecompressingReader;
import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.json.FileStructureJSONSerialization;

/**
 * @author williamhaw
 *
 */
public class GZIPDecompressionExecutor {
	
	private final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	
	public void executeDecompression(final String inputDirectory, final String outputDirectory) throws InterruptedException, IOException, ParseException {
		final File inputRoot = new File(inputDirectory);
		final File outputRoot = new File(outputDirectory);
		
		//Deserialize JSON
		File jsonFile = new File(inputRoot, "metadata.json");
		List<String> jsonLines = Files.readAllLines(Paths.get(jsonFile.toURI()), StandardCharsets.UTF_8);
		String fileJSON = String.join("", jsonLines);
		List<FileStructure> fileList = FileStructureJSONSerialization.deserialize(fileJSON);
		
		final BlockingQueue<FileStructure> directoryQueue = new ArrayBlockingQueue<>(fileList.size());
		final BlockingQueue<FileStructure> fileQueue = new ArrayBlockingQueue<>(fileList.size());
		
		DecompressingReader reader = new DecompressingReader(fileList, directoryQueue, fileQueue);
		reader.readDirectories();
		
		FileStructure directory;
		//create directories
		while((directory = directoryQueue.poll(1000, TimeUnit.MILLISECONDS)) != null) {
			
		}
		
		
		
	}
}
