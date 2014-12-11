package com.lelann.json;

import java.io.File;
import java.io.IOException;

import com.lelann.json.data.JSONObject;

public class Main {
	public static void main(String[] args){
		JSONObject object = JSON.load(new File("file.json"));
		int[][] a = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		object.set("values", a);
		try {
			object.save(new File("file2.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
