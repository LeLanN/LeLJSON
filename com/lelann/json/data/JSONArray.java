package com.lelann.json.data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.lelann.json.exceptions.JSyntaxError;
import com.lelann.json.utils.JCharacter;
import com.lelann.json.utils.JData;
import com.lelann.json.utils.ReaderUtils;
/**
 * Represent a JSON Array
 *
 */
public class JSONArray {
	private List<JSONElement> values = new ArrayList<JSONElement>();
	private List<JSONArray> arrays = new ArrayList<JSONArray>();
	private List<JSONObject> objects = new ArrayList<JSONObject>();
	
	public JSONArray(List<?> what){
		for(Object o : what){
			if(o instanceof Number){
				values.add(new JSONElement(((Number)o).toString()));
			} else if(o instanceof String){
				values.add(new JSONElement((String) o));
			} else if(o instanceof JSONObject){
				objects.add((JSONObject) o);
			} else if(o instanceof List){
				arrays.add(new JSONArray((List<?>) o));
			} else if(o instanceof JSONArray){
				arrays.add((JSONArray)o);
			} else if(o.getClass().isArray()){
				arrays.add(new JSONArray(o));
			} else if(o instanceof JStorable){
				objects.add(((JStorable) o).save());
			} else if(o instanceof Boolean){
				values.add(new JSONElement(((Boolean)o).toString()));
			} else {
				values.add(new JSONElement(o.toString()));
			}
		}
	}
	public JSONArray(Object what){
		if(what.getClass().isArray()){
			for(Object o : convertToObjectArray(what)){
				if(o instanceof Number){
					values.add(new JSONElement(((Number)o).toString()));
				} else if(o instanceof String){
					values.add(new JSONElement((String) o));
				} else if(o instanceof JSONObject){
					objects.add((JSONObject) o);
				} else if(o instanceof List){
					arrays.add(new JSONArray((List<?>) o));
				} else if(o instanceof JSONArray){
					arrays.add((JSONArray)o);
				} else if(o instanceof JStorable){
					objects.add(((JStorable) o).save());
				} else if(o.getClass().isArray()){
					arrays.add(new JSONArray(o));
				} else if(what instanceof Boolean){
					values.add(new JSONElement(((Boolean)what).toString()));
				} else {
					values.add(new JSONElement(o.toString()));
				}
			}
		}
	}
	public JSONArray(JCharacter[] datas) throws JSyntaxError{
		boolean isString = false, isEchap = false;
		String current = "";
		
		JData wdata = JData.DATA_VALUE;
		for(int i=0;i<datas.length;i++){
			final JCharacter c = datas[i];
			final char ch = c.getValue().toCharArray()[0];
			if(isString){
				if(c.getValue().equals("\\")){
					isEchap = !isEchap;
				} else if(c.getValue().equals("\"") && !isEchap){
					isString = false;
					if(wdata == JData.DATA_VALUE){
						wdata = JData.NEXT_DATA;
						values.add(new JSONElement(current));
						current = "";
					} else {
						throw new JSyntaxError(c);
					}
					continue;
				} else {
					isEchap = false;
				}
				current += c.getValue();
			} else if(c.getValue().equals("\"")){
				if(wdata == JData.DATA_VALUE){
					isString = true;
				} else {
					throw new JSyntaxError(c);
				}
			} else if(c.getValue().equals("{")){
				if(wdata == JData.DATA_VALUE){
					JCharacter[] nDatas = ReaderUtils.getFullObject(datas, i);
					i += nDatas.length + 1;
					wdata = JData.NEXT_DATA;
					
					objects.add(new JSONObject(nDatas));
				} else {
					throw new JSyntaxError(c);
				}
			} else if(c.getValue().equals(",")){
				if(wdata == JData.NEXT_DATA){
					wdata = JData.DATA_VALUE;
				} else if(wdata == JData.VAR_NBR){
					wdata = JData.DATA_VALUE;
					values.add(new JSONElement(current));
					current = "";
				} else {
					throw new JSyntaxError(c);
				}
			} else if(c.getValue().equals("[")){
				if(wdata == JData.DATA_VALUE){
					JCharacter[] nDatas = ReaderUtils.getFullArray(datas, i);
					i += nDatas.length + 1;
					wdata = JData.NEXT_DATA;
					
					arrays.add(new JSONArray(nDatas));
				} else {
					throw new JSyntaxError(c);
				}
			} else if(Character.isDigit(ch)){
				if(wdata == JData.DATA_VALUE || wdata == JData.VAR_NBR){
					wdata = JData.VAR_NBR;
					current += c.getValue();
				} else {
					throw new JSyntaxError(c);
				}
			} else if(c.getValue().equals(".")){
				if(wdata == JData.VAR_NBR){
					wdata = JData.VAR_NBR;
					current += c.getValue();
				} else {
					throw new JSyntaxError(c);
				}
			} else {
				throw new JSyntaxError(c);
			}
		}
		if(wdata == JData.VAR_NBR){
			values.add(new JSONElement(current));
		}
	}
	public boolean isEmpty(){
		return values.isEmpty() && arrays.isEmpty() && objects.isEmpty();
	}
	public List<Boolean> getBooleanList(){
		List<Boolean> result = new ArrayList<Boolean>();
			for(final JSONElement je : values){
				result.add(je.getBooleanValue());
			}
		return result;
	}
	public List<Integer> getIntList(){
		List<Integer> result = new ArrayList<Integer>();
			for(final JSONElement je : values){
				result.add(je.getIntValue());
			}
		return result;
	}
	public List<Double> getDoubleList(){
		List<Double> result = new ArrayList<Double>();
			for(final JSONElement je : values){
				result.add(je.getDoubleValue());
			}
		return result;
	}
	public List<Float> getFloatList(){
		List<Float> result = new ArrayList<Float>();
			for(final JSONElement je : values){
				result.add(je.getFloatValue());
			}
		return result;
	}
	public List<Short> getShortList(){
		List<Short> result = new ArrayList<Short>();
			for(final JSONElement je : values){
				result.add(je.getShortValue());
			}
		return result;
	}
	public List<String> getStringList(){
		List<String> result = new ArrayList<String>();
			for(final JSONElement je : values){
				result.add(je.getStringValue());
			}
		return result;
	}
	public List<JSONArray> getArrayList(){
		return arrays;
	}
	public List<JSONObject> getObjectList(){
		return objects;
	}
	public List<Object> getList(){
		List<Object> result = new ArrayList<Object>();
			result.addAll(arrays);
			result.addAll(objects);
			result.addAll(values);
		return result;
	}
	
	public String getArray(int incremente){
		String inc = "", result = "";
		for(int i=0;i<incremente;i++){
			inc += " ";
		}
		result += "[\n";
		String valueInc = inc + "  ";
		
		boolean isFirst = true;
		for(final JSONElement value : values){
			if(!isFirst) result += ",\n"; else isFirst = false;
			result += valueInc + value.getStorableValue();
		}
		for(final JSONObject value : objects){
			if(!isFirst) result += ",\n"; else isFirst = false;
			result += valueInc + value.getObject(incremente + 2);
		}
		for(final JSONArray value : arrays){
			if(!isFirst) result += ",\n"; else isFirst = false;
			result += valueInc + value.getArray(incremente + 2);
		}
		result += "\n" + inc + "]";
		return result;
	}
	
	
	public static Object[] convertToObjectArray(Object array) {
	    Class<?> ofArray = array.getClass().getComponentType();
	    if (ofArray.isPrimitive()) {
	        List<Object> ar = new ArrayList<Object>();
	        int length = Array.getLength(array);
	        for (int i = 0; i < length; i++) {
	            ar.add(Array.get(array, i));
	        }
	        return ar.toArray();
	    }
	    else {
	        return (Object[]) array;
	    }
	}
	@Override
	public String toString(){
		String result = "";
		result += "[";
		
		boolean isFirst = true;
		for(final JSONElement value : values){
			if(!isFirst) result += ","; else isFirst = false;
			result += value.getStorableValue();
		}
		for(final JSONObject value : objects){
			if(!isFirst) result += ","; else isFirst = false;
			result += value.toString();
		}
		for(final JSONArray value : arrays){
			if(!isFirst) result += ","; else isFirst = false;
			result += value.toString();
		}
		result += "]";
		return result;
	}
}
