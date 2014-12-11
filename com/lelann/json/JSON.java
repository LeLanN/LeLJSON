package com.lelann.json;

import java.io.File;
import java.io.IOException;

import com.lelann.json.data.JSONObject;
import com.lelann.json.exceptions.JSyntaxError;
import com.lelann.json.utils.FileUtils;
import com.lelann.json.utils.JCharacter;
import com.lelann.json.utils.Reader;
import com.lelann.json.utils.ReaderUtils;

/**
 * This Class can be use to load JSONObject
 *
 */
public class JSON {
	/**
     * Load JSON from String
     * 
     * @param file
     *            The JSON string
     * 
     * @return A new JSONObject
     */
	public static JSONObject loadFromString(String content){
		JCharacter[] ca = new Reader(content).parse();
		try {
			return new JSONObject(ReaderUtils.getFullObject(ca, 0));
		} catch (JSyntaxError e) {
			e.printStackTrace();
			return loadFromString("{}");
		}
	}
	/**
     * Load JSON from File
     * 
     * @param file
     *            The JSON file
     * 
     * @return A new JSONObject
     */
	public static JSONObject load(File file){
		String content = null;
		try {
			content = FileUtils.getContent(new File("file.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loadFromString(content);
	}
}
