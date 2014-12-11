package com.lelann.json.exceptions;

import com.lelann.json.utils.JCharacter;

public class JSyntaxError extends Exception{
	private static final long serialVersionUID = 8986722497491664926L;

	public JSyntaxError(JCharacter carac){
		super("Unexpected character '" + carac.getValue() + "' at line " + carac.getLine() + ", column " + carac.getColumn() + ".");
	}
}
