package com.williamhaw.compression.json;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.williamhaw.compression.file.structure.FileStructure;
import com.williamhaw.compression.file.structure.FileStructure.Type;

/**
 * Handles serialization of FileStructures to JSON
 * @author williamhaw
 *
 */
public class FileStructureJSONSerialization {
	
	private final static String KEY_NAME = "name";
	private final static String KEY_TYPE = "type";
	private final static String KEY_RELATIVE_PATH = "relativePath";
	private final static String KEY_FILE_NUMBER = "fileNumber";
	
	@SuppressWarnings("unchecked")
	public static String serialize(List<FileStructure> files) {
		JSONArray array = new JSONArray();
		for(FileStructure file : files) {
			JSONObject obj = new JSONObject();
			obj.put(KEY_NAME, file.getName());
			obj.put(KEY_TYPE, file.getType().toString());
			obj.put(KEY_RELATIVE_PATH, file.getRelativePath());
			if(file.getType() == Type.FILE)
				obj.put(KEY_FILE_NUMBER, file.getFileNumber());
			
			array.add(obj);
		}		
		
		return array.toJSONString();
	}
	
	public static List<FileStructure> deserialize(String fileJSON) throws ParseException{
		JSONParser parser = new JSONParser();
		JSONArray array = (JSONArray)parser.parse(fileJSON);
		
		List<FileStructure> ret = new ArrayList<>();
		
		while(array.iterator().hasNext()) {
			JSONObject obj = (JSONObject) array.iterator().next();
			FileStructure file = new FileStructure();
			file.setName((String) obj.get(KEY_NAME));
			file.setType(Type.valueOf((String)obj.get(KEY_TYPE)));
			file.setRelativePath((String) obj.get(KEY_RELATIVE_PATH));
			if(file.getType() == Type.FILE)
				file.setFileNumber((int) obj.get(KEY_FILE_NUMBER));
			
			ret.add(file);
		}		
		
		return ret;
	}
}
