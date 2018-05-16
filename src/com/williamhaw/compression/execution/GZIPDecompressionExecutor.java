package com.williamhaw.compression.execution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.simple.parser.ParseException;

import com.williamhaw.compression.compress.CompressionFactory;
import com.williamhaw.compression.decompress.DecompressionHandler;
import com.williamhaw.compression.file.DecompressingReader;
import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.json.FileStructureJSONSerialization;
import com.williamhaw.compression.utils.CompressionType;

/**
 * Reads metadata json file, makes directories and submits files to excecutor to decompress in parallel
 * @author williamhaw
 *
 */
public class GZIPDecompressionExecutor {
	
	private final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private final CompressionFactory factory = new CompressionFactory();
	
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
		reader.readDirectories(); //load directories into directoryQueue
		
		FileStructure directory;
		//create directories
		while((directory = directoryQueue.poll(10, TimeUnit.MILLISECONDS)) != null) {
			File newDirectory = new File(outputDirectory, directory.getRelativePath());
			System.out.println("Creating directory: " + newDirectory.getAbsolutePath());
			System.out.println(newDirectory.mkdirs() ? "created" : "not created; already exists");
		}
		
		reader.readFiles(); //load files into fileQueue
		//decompress files
		List<FileStructure> filesToDecompress = new ArrayList<>();
		fileQueue.drainTo(filesToDecompress);
		for(final FileStructure toSubmit : filesToDecompress) {
			threadPool.execute(new Runnable() {				
				@Override
				public void run() {
					DecompressionHandler handler = new DecompressionHandler(factory.getFileDecompression(CompressionType.GZIP));
					handler.decompress(toSubmit, inputRoot, outputRoot);
				}
			});
		}
		
		threadPool.shutdown();
	}
}
