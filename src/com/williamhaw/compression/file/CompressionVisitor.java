package com.williamhaw.compression.file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.file.structure.FileStructure.Type;

/**
 * Gathers directories and files into list of FileStructures while walking the directory tree
 * <p>
 * Not thread-safe!
 * @author williamhaw
 *
 */
public class CompressionVisitor implements FileVisitor<Path> {
	
	private Path rootDir;
	private BlockingQueue<FileStructure> fileQueue;
	private List<FileStructure> fileList = new ArrayList<>();
	private AtomicInteger fileCounter = new AtomicInteger(1);
	
	public CompressionVisitor(Path rootDir, BlockingQueue<FileStructure> fileQueue){
		this.rootDir = rootDir;
		this.fileQueue = fileQueue;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		
		for(Path p : stream) {
			if (Files.isDirectory(p)) {
				FileStructure d = new FileStructure();
				//TODO relativise path
				d.setName("");
				d.setType(Type.DIRECTORY);
				fileList.add(d);
			}else if (Files.isRegularFile(p) && !Files.isSymbolicLink(p) && Files.isReadable(p)) {//skip symbolic links
				FileStructure f = new FileStructure();
				//TODO relativise path
				f.setName("");
				f.setType(Type.FILE);
				f.setFileNumber(fileCounter.getAndIncrement());
				fileList.add(f);
				try {
					fileQueue.put(f);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
		throw e;
	}

}
