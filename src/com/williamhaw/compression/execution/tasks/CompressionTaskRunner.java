package com.williamhaw.compression.execution.tasks;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.williamhaw.compression.compress.CompressionFactory;
import com.williamhaw.compression.compress.CompressionHandler;
import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.utils.CompressionType;

/**
 * Sets up thread pool and runs compression on files described by fileStructure
 * @author williamhaw
 *
 */
public class CompressionTaskRunner {
	private final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private final CompressionFactory factory = new CompressionFactory();
	private final CompressionType type;
	private final File inputRoot;
	private final File targetRoot;
	private final int sizelimitMB;
	
	public CompressionTaskRunner(CompressionType type, File inputRoot, File targetRoot, int sizelimitMB) {
		this.type = type;
		this.inputRoot = inputRoot;
		this.targetRoot = targetRoot;
		this.sizelimitMB = sizelimitMB;
	}

	public void submit(final FileStructure fileStructure) {
		if(fileStructure != null) {
			threadPool.submit(new Runnable() {					
				@Override
				public void run() {								
					CompressionHandler handler = new CompressionHandler(factory.getCompression(type));
					handler.compress(fileStructure.getFileNumber(), new File(inputRoot, fileStructure.getRelativePath()), targetRoot, sizelimitMB);					
				}
			});
		}
	}
	
	public void shutdown() {
		threadPool.shutdown();
	}
}
