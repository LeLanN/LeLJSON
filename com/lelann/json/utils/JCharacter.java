package com.lelann.json.utils;

public class JCharacter {
	private String value;
	private int column, line;
	
	public int getColumn(){
		return this.column;
	}
	public int getLine(){
		return this.line;
	}
	public String getValue(){
		return this.value;
	}
	
	public JCharacter(String value, int column, int line){
		this.value = value;
		this.column = column;
		this.line = line;
	}
}
