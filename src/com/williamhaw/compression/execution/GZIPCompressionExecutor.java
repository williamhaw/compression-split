package com.williamhaw.compression.execution;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import com.williamhaw.compression.execution.tasks.CompressionTaskRunner;
import com.williamhaw.compression.file.CompressionVisitor;
import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.json.FileStructureJSONSerialization;
import com.williamhaw.compression.utils.CompressionType;

/**
 * Sets up compression visitor to visit files and task runner to execute compression of files in parallel
 * @author williamhaw
 *
 */
public class GZIPCompressionExecutor {
	
	public void executeCompression(final String inputDirectory, final String outputDirectory, final int sizelimitMB) throws IOException {
		
		final File inputRoot = new File(inputDirectory);
		final File targetRoot = new File(outputDirectory);
		
		CompressionTaskRunner taskRunner = new CompressionTaskRunner(CompressionType.GZIP, inputRoot, targetRoot, sizelimitMB);
		
		CompressionVisitor visitor = new CompressionVisitor(Paths.get(inputRoot.toURI()), taskRunner);
		Files.walkFileTree(Paths.get(inputRoot.toURI()), visitor);
		
		List<FileStructure> fileList = visitor.getFileList();
		//serialise to JSON
		String filesJSON = FileStructureJSONSerialization.serialize(fileList);
		File jsonFile = new File(targetRoot, "metadata.json");
		Files.write(Paths.get(jsonFile.toURI()), filesJSON.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
		
		taskRunner.shutdown();
	}
}
