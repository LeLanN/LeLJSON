package com.lelann.json.data;

/**
 * Represent a JSON value
 *
 */
public class JSONElement {
	private Double numberValue;
	private String stringValue;
	private Boolean boolValue;
	
	/**
	 * Parse a JSONElement with a String
	 * @param value
	 * 		   The JSONElement value
	 * 
	 */
	public JSONElement(String value){
		try {
			numberValue = Double.parseDouble(value);
		} catch(Exception e){
			numberValue = 0.0;
		}
		stringValue = value.replace("\\\"", "\"");
		try {
			boolValue = Boolean.parseBoolean(value);
		} catch(Exception e){
			boolValue = false;
		}
	}
	/**
	 * 
	 * @return A string value of the JSONElement, which can be save
	 */
	public String getStorableValue(){
		try {
			numberValue = Double.parseDouble(stringValue);
			return stringValue;
		} catch(Exception e){
			return "\"" + stringValue.replace("\"", "\\\"") + "\"";
		}
	}
	/**
	 * @return Get a String value of the JSONElement. Can not return null
	 */
	public String getStringValue(){
		return stringValue;
	}
	/**
	 * @return Get an Integer value of the JSONElement. If it isn't a number, return 0
	 */
	public int getIntValue(){
		return numberValue.intValue();
	}
	/**
	 * @return Get a Double value of the JSONElement. If it isn't a number, return 0
	 */
	public double getDoubleValue(){
		return numberValue.doubleValue();
	}
	/**
	 * @return Get a Float value of the JSONElement. If it isn't a number, return 0
	 */
	public float getFloatValue(){
		return numberValue.floatValue();
	}
	/**
	 * @return Get a Short value of the JSONElement. If it isn't a number, return 0
	 */
	public short getShortValue(){
		return numberValue.shortValue();
	}
	/**
	 * @return Get a Long value of the JSONElement. If it isn't a number, return 0
	 */
	public long getLongValue(){
		return numberValue.longValue();
	}
	/**
	 * @return Get a Boolean value of the JSONElement. If it isn't a boolean, return false
	 */
	public boolean getBooleanValue(){
		return boolValue;
	}
}
