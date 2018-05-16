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
import java.util.concurrent.atomic.AtomicInteger;

import com.williamhaw.compression.execution.tasks.CompressionTaskRunner;
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
	private List<FileStructure> fileList = new ArrayList<>();
	private AtomicInteger fileCounter = new AtomicInteger(1);
	private CompressionTaskRunner taskRunner;
	
	public CompressionVisitor(Path rootDir, CompressionTaskRunner taskRunner){
		this.rootDir = rootDir;
		this.taskRunner = taskRunner;
	}
	
	public List<FileStructure> getFileList() {
		return fileList;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		
		for(Path p : stream) {
			if (Files.isDirectory(p)) {
				FileStructure d = new FileStructure();
				d.setName(p.getFileName().toString());
				d.setRelativePath(rootDir.relativize(p).toString());
				d.setType(Type.DIRECTORY);
				fileList.add(d);
			}else if (Files.isRegularFile(p) && !Files.isSymbolicLink(p) && Files.isReadable(p)) {//skip symbolic links
				FileStructure f = new FileStructure();
				f.setName(p.getFileName().toString());
				f.setRelativePath(rootDir.relativize(p).toString());
				f.setType(Type.FILE);
				f.setFileNumber(fileCounter.getAndIncrement());
				fileList.add(f);
				taskRunner.submit(f);
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
