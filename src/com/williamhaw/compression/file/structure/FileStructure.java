package com.williamhaw.compression.file.structure;

/**
 * POJO to encapsulate file information
 * @author williamhaw
 *
 */
public class FileStructure {
	public enum Type{FILE, DIRECTORY};
	private String name;
	private Type type;
	//Valid file numbers must be positive
	private int fileNumber = -1;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		sb.append(", type=");
		sb.append(getType().name());
		sb.append(", fileNumber=");
		sb.append(getFileNumber());
		sb.append("]");
		return sb.toString();
	}
}
