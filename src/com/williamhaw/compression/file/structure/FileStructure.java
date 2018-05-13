package com.williamhaw.compression.file.structure;

/**
 * POJO to encapsulate file information
 * @author williamhaw
 *
 */
public class FileStructure {
	public static final int NOT_A_FILE = -1;
	public enum Type{FILE, DIRECTORY};
	private String name;
	private String relativePath;
	private Type type;
	//Valid file numbers must be positive
	private int fileNumber = NOT_A_FILE;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String path) {
		this.relativePath = path;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FileStructure [name=");
		sb.append(getName());
		sb.append(", path=");
		sb.append(getRelativePath());
		sb.append(", type=");
		sb.append(getType().name());
		sb.append(", fileNumber=");
		sb.append(getFileNumber());
		sb.append("]");
		return sb.toString();
	}
}
