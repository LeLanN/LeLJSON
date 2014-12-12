package com.lelann.json;

import java.io.File;

import com.lelann.json.data.JSONObject;

public class Main {
	public static void main(String[] args){
		JSONObject object = JSON.load(new File("file.json"));
		
		for(JSONObject o : object.getObjectList("libraries")){
			System.out.println(o.getString("name"));
		}
	}
}
