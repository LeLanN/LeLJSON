package com.lelann.json.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lelann.json.JSON;
import com.lelann.json.exceptions.JSyntaxError;
import com.lelann.json.utils.FileUtils;
import com.lelann.json.utils.JCharacter;
import com.lelann.json.utils.JData;
import com.lelann.json.utils.ReaderUtils;
/**
 * Represent an JSON Object
 *
 */
public class JSONObject {
	private Map<String, JSONElement> values = new HashMap<String, JSONElement>();
	private Map<String, JSONArray> arrays = new HashMap<String, JSONArray>();
	private Map<String, JSONObject> objects = new HashMap<String, JSONObject>();
	
	private List<String> order = new ArrayList<String>();
	
	/**
     * Check if the Object is empty ("{}")
     * 
     */
	public boolean isEmpty(){
		return values.isEmpty() && arrays.isEmpty() && objects.isEmpty();
	}
	/**
     * @return Number, boolean and strings of the object
     */
	public Map<String, JSONElement> getValues(){
		return values;
	}
	/**
     * @return Arrays of the object
     */
	public Map<String, JSONArray> getArrays(){
		return arrays;
	}
	/**
     * @return Sub objects of the object
     */
	public Map<String, JSONObject> getObjects(){
		return objects;
	}
	/**
     * @return Number, boolean and strings, arrays and 'sub-objects' of the object
     */
	public Map<String, Object> getAll(){
		Map<String, Object> result = new HashMap<String, Object>();
			result.putAll(values);
			result.putAll(arrays);
			result.putAll(objects);
		return result;
	}
	/**
     * Set (or replace) a value
     * If the Object is null, the previous value will be errase
     * 
     * @param where
     *            The field name
     * @parem what
     * 			  The value
     */
	public void set(String where, Object what){
		JSONObject o = getCorrespondingObject(where);
		where = getRequest(where);
		
		if(!o.contains(where)){
			o.order.add(where);
		}
		if(o.objects.containsKey(where))
			o.objects.remove(where);
		if(o.values.containsKey(where))
			o.values.remove(where);
		if(o.arrays.containsKey(where))
			o.arrays.remove(where);
		
		if(what == null) {
			o.order.remove(where); return;
		}
		if(what instanceof JSONObject){
			o.objects.put(where, (JSONObject) what);
		} else if(what instanceof String){
			o.values.put(where, new JSONElement((String) what));
		} else if(what instanceof Number){
			o.values.put(where, new JSONElement(((Number) what).toString()));
		} else if(what instanceof List){
			o.arrays.put(where, new JSONArray((List<?>) what));
		} else if(what instanceof JSONArray){
			o.arrays.put(where, (JSONArray)what);
		} else if(what instanceof Boolean){
			o.values.put(where, new JSONElement(((Boolean)what).toString()));
		} else if(what instanceof JSONElement){
			o.values.put(where, (JSONElement)what);
		} else if(what instanceof JStorable){
			o.objects.put(where, ((JStorable) what).save());
		} else if(what.getClass().isArray()){
			o.arrays.put(where, new JSONArray(what));
		} else {
			o.values.put(where, new JSONElement(o.toString()));
		}
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return The object, which match with the field name. Can be null.
	 */
	public Object get(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.values.containsKey(what)){
			return o.values.get(what);
		} else if(o.arrays.containsKey(what)){
			return o.arrays.get(what);
		} else if(o.objects.containsKey(objects)){
			return o.objects.get(what);
		} else return null;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Check if the object contains the field
	 */
	public boolean contains(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		return o.values.containsKey(what) || o.arrays.containsKey(what) || o.objects.containsKey(what);
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching JSONObject. Can be null
	 */
	public JSONObject getObject(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.objects.containsKey(what))
			return o.objects.get(what);
		else return null;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching String. Can be null
	 */
	public String getString(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.values.containsKey(what))
			return o.values.get(what).getStringValue();
		else return null;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Integer. If null, 0
	 */
	public int getInt(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.values.containsKey(what))
			return o.values.get(what).getIntValue();
		else return 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Double. If null, 0
	 */
	public double getDouble(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.values.containsKey(what))
			return o.values.get(what).getDoubleValue();
		else return 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Float. If null, 0
	 */
	public float getFloat(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.values.containsKey(what))
			return o.values.get(what).getFloatValue();
		else return 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Long. If null, 0
	 */
	public long getLong(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.values.containsKey(what))
			return o.values.get(what).getLongValue();
		else return 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Short. If null, 0
	 */
	public int getShort(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.values.containsKey(what))
			return o.values.get(what).getShortValue();
		else return 0;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Boolean. If null, false
	 */
	public boolean getBoolean(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.values.containsKey(what))
			return o.values.get(what).getBooleanValue();
		else return false;
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Boolean List
	 */
	public List<Boolean> getBooleanList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getBooleanList();
		else return new ArrayList<Boolean>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Integer List
	 */
	public List<Integer> getIntList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getIntList();
		else return new ArrayList<Integer>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Double List
	 */
	public List<Double> getDoubleList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getDoubleList();
		else return new ArrayList<Double>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Float List
	 */
	public List<Float> getFloatList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getFloatList();
		else return new ArrayList<Float>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Short List
	 */
	public List<Short> getShortList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getShortList();
		else return new ArrayList<Short>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching String List
	 */
	public List<String> getStringList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getStringList();
		else return new ArrayList<String>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Array List (array containing arrays)
	 */
	public List<JSONArray> getArrayList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getArrayList();
		else return new ArrayList<JSONArray>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Object List
	 */
	public List<JSONObject> getObjectList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getObjectList();
		else return new ArrayList<JSONObject>();
	}
	/**
	 * 
	 * @param what
	 * 			The field name
	 * @return Get the matching Object List
	 */
	public List<Object> getList(String what){
		JSONObject o = getCorrespondingObject(what);
		what = getRequest(what);
		
		if(o.arrays.containsKey(what))
			return o.arrays.get(what).getList();
		else return new ArrayList<Object>();
	}
	
	private JSONObject getCorrespondingObject(String s){
		String[] objects = s.split("\\.");
		JSONObject o = this;
		for(int i=0;i<objects.length -1;i++){
			o = o.objects.get(objects[i]);
			if(o == null) o = JSON.loadFromString("{}");
		}
		
		return o;
	}
	private String getRequest(String s){
		String[] objects = s.split("\\.");
		return objects[objects.length - 1];
	}
	/**
	 * User is not suppose to use the constructor
	 */
	public JSONObject(JCharacter[] datas) throws JSyntaxError{
		boolean isString = false, isEchap = false;
		String name = "", current = "";
		
		JData wdata = JData.NEW_DATA;
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
						set(name, new JSONElement(current));
						current = ""; name = "";
					} else if(wdata == JData.NEW_DATA){
						wdata = JData.DATA_OPEN;
						name = current;
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
				if(wdata == JData.DATA_VALUE || wdata == JData.NEW_DATA){
					isString = true;
				} else {
					throw new JSyntaxError(c);
				}
			} else if(c.getValue().equals("{")){
				if(wdata == JData.DATA_VALUE){
					JCharacter[] nDatas = ReaderUtils.getFullObject(datas, i);
					i += nDatas.length + 1;
					wdata = JData.NEXT_DATA;
					
					set(name, new JSONObject(nDatas));
				} else {
					throw new JSyntaxError(c);
				}
			} else if(c.getValue().equals(",")){
				if(wdata == JData.NEXT_DATA){
					wdata = JData.NEW_DATA;
				} else if(wdata == JData.VAR_NBR){
					wdata = JData.NEW_DATA;
					set(name, new JSONElement(current));
					current = ""; name = "";
				} else {
					throw new JSyntaxError(c);
				}
			} else if(c.getValue().equals(":")){
				if(wdata == JData.DATA_OPEN){
					wdata = JData.DATA_VALUE;
				} else {
					throw new JSyntaxError(c);
				}
			} else if(c.getValue().equals("[")){
				if(wdata == JData.DATA_VALUE){
					JCharacter[] nDatas = ReaderUtils.getFullArray(datas, i);
					i += nDatas.length + 1;
					wdata = JData.NEXT_DATA;
					
					set(name, new JSONArray(nDatas));
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
			values.put(name, new JSONElement(current));
		}
	}
	/**
	 * 
	 * @param file
	 * 			Save the JSONObject as File
	 * @throws IOException
	 */
	public void save(File file) throws IOException{
		FileUtils.save(getObject(0) + "\n", file);
	}
	/**
	 * Useless for the user : the API use it to save Objects as File
	 */
	public String getObject(int incremente){
		String inc = "", result = "";
		for(int i=0;i<incremente;i++){
			inc += " ";
		}
		result += "{\n";
		String valueInc = inc +  "  ";
		String value = "";
		
		boolean isFirst = true;
		for(final String key : order){
			if(objects.containsKey(key))
				value = objects.get(key).getObject(incremente + 2);
			else if(values.containsKey(key))
				value = values.get(key).getStorableValue();
			else if(arrays.containsKey(key))
				value = arrays.get(key).getArray(incremente + 2);
			else continue;
			
			if(!isFirst) result += ",\n"; else isFirst = false;
			result += valueInc + "\"" + key + "\": " + value;
		}
		
		result += "\n" + inc + "}";
		return result;
	}
	/**
	 * Return the object without space, \n, \t, ...
	 */
	@Override
	public String toString(){
		String result = "", value = "";
		result += "{";
		
		boolean isFirst = true;
		for(final String key : order){
			if(objects.containsKey(key))
				value = objects.get(key).toString();
			else if(values.containsKey(key))
				value = values.get(key).getStorableValue();
			else if(arrays.containsKey(key))
				value = arrays.get(key).toString();
			else continue;
			
			if(!isFirst) result += ","; else isFirst = false;
			result += "\"" + key + "\":" + value;
		}
		
		result += "}";
		return result;
	}
}
