package com.lelann.json.data;

/**
 * Represent a JSON value
 *
 */
public class JSONElement {
	private Double numberValue;
	private String stringValue;
	private Boolean boolValue;
	
	public JSONElement(String value){
		try {
			numberValue = Double.parseDouble(value);
		} catch(Exception e){
			numberValue = 0.0;
		}
		stringValue = value;
		try {
			boolValue = Boolean.parseBoolean(value);
		} catch(Exception e){
			boolValue = false;
		}
	}
	public String getStorableValue(){
		try {
			numberValue = Double.parseDouble(stringValue);
			return stringValue;
		} catch(Exception e){
			return "\"" + stringValue + "\"";
		}
	}
	public String getStringValue(){
		return stringValue;
	}
	public int getIntValue(){
		return numberValue.intValue();
	}
	public double getDoubleValue(){
		return numberValue.doubleValue();
	}
	public float getFloatValue(){
		return numberValue.floatValue();
	}
	public short getShortValue(){
		return numberValue.shortValue();
	}
	public long getLongValue(){
		return numberValue.longValue();
	}
	public boolean getBooleanValue(){
		return boolValue;
	}
}
