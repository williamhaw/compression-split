package com.williamhaw.compression.file;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.file.structure.FileStructure.Type;

/**
 * Reads List of FileStructures and puts files and directories in appropriate queues
 * @author williamhaw
 *
 */
public class DecompressingReader {
	
	private List<FileStructure> fileList;
	private BlockingQueue<FileStructure> directoryQueue;
	private BlockingQueue<FileStructure> fileQueue;
	public DecompressingReader(List<FileStructure> fileList, BlockingQueue<FileStructure> directoryQueue,
			BlockingQueue<FileStructure> fileQueue) {
		this.fileList = fileList;
		this.directoryQueue = directoryQueue;
		this.fileQueue = fileQueue;
	}
	
	public void readDirectories() throws InterruptedException {
		for(FileStructure fs : fileList) {
			if(fs.getType() == Type.DIRECTORY) {
				directoryQueue.put(fs);
			}
		}
	}
	
	public void readFiles() throws InterruptedException {
		for(FileStructure fs : fileList) {
			if(fs.getType() == Type.FILE) {
				fileQueue.put(fs);
			}
		}
	}
}
