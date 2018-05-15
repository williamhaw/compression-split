package com.williamhaw.compression.execution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.williamhaw.compression.compress.CompressionHandler;
import com.williamhaw.compression.compress.impl.GZipCompression;
import com.williamhaw.compression.file.CompressionVisitor;
import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.json.FileStructureJSONSerialization;

/**
 * 
 * @author williamhaw
 *
 */
public class GZIPCompressionExecutor {
	
	private final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private final BlockingQueue<FileStructure> workQueue = new ArrayBlockingQueue<>(1000);
	
	public void executeCompression(final String inputDirectory, final String outputDirectory, final int sizelimitMB) throws IOException, InterruptedException {
		
		final File inputRoot = new File(inputDirectory);
		final File targetRoot = new File(outputDirectory);
		
		final AtomicBoolean visitedAllFiles = new AtomicBoolean(false);

		Thread compressionTaskCreator = new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					while(visitedAllFiles.get() == false) {
						final FileStructure fileStructure = workQueue.poll(100, TimeUnit.MILLISECONDS);
						if(fileStructure != null) {
							threadPool.submit(new Runnable() {					
								@Override
								public void run() {								
									CompressionHandler handler = new CompressionHandler(new GZipCompression());
									handler.compress(fileStructure.getFileNumber(), new File(inputRoot, fileStructure.getRelativePath()), targetRoot, sizelimitMB);					
								}
							});
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}								
			}
		});
		
		compressionTaskCreator.start();
		
		CompressionVisitor visitor = new CompressionVisitor(Paths.get(inputRoot.toURI()), workQueue);
		Files.walkFileTree(Paths.get(inputRoot.toURI()), visitor);
		visitedAllFiles.set(true);
		List<FileStructure> fileList = visitor.getFileList();
		//serialise to JSON
		String filesJSON = FileStructureJSONSerialization.serialize(fileList);
		File jsonFile = new File(targetRoot, "metadata.json");
		Files.write(Paths.get(jsonFile.toURI()), filesJSON.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
		
		threadPool.shutdown();
		threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
	}
}
