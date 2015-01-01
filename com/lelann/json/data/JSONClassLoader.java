package com.lelann.json.data;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class JSONClassLoader {
	public static Object load(JSONObject jo, Object o){
		for(Field f : o.getClass().getDeclaredFields()){
			Object newValue = jo.get(f.getName());
			if(f.getName().equals("natives")){
				System.out.println(jo);
			}
			if(newValue != null){
				try {
					f.setAccessible(true);
					if(newValue instanceof JSONElement){
						JSONElement e = (JSONElement)newValue;
						if(f.getType().equals(Double.class)){
							f.setDouble(o, e.getDoubleValue());
						} else if(f.getType().equals(Integer.class)){
							f.setInt(o, e.getIntValue());
						} else if(f.getType().equals(Long.class)){
							f.setLong(o, e.getLongValue());
						} else if(f.getType().equals(Short.class)){
							f.setShort(o, e.getShortValue());
						} else if(f.getType().equals(Float.class)){
							f.setFloat(o, e.getFloatValue());
						} else if(f.getType().equals(Boolean.class)){
							f.setBoolean(o, e.getBooleanValue());
						} else if(f.getType().equals(String.class)){
							f.set(o, e.getStringValue());
						}
					} else if(newValue instanceof JSONArray){
						JSONArray e = (JSONArray)newValue;
						if(f.getType().equals(List.class)){
							Class<?> c = (Class<?>)((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
							if(c.equals(Integer.class)){
								f.set(o, e.getIntList());
							} else if(c.equals(Double.class)){
								f.set(o, e.getDoubleList());
							} else if(c.equals(Float.class)){
								f.set(o, e.getFloatList());
							} else if(c.equals(Short.class)){
								f.set(o, e.getShortList());
							} else if(c.equals(Boolean.class)){
								f.set(o, e.getBooleanList());
							} else if(c.equals(String.class)){
								f.set(o, e.getStringList());
							} else {
								List<Object> list = new ArrayList<Object>();
								for(JSONObject obj : e.getObjectList()){
									try {
										Object val = c.newInstance();
										val = load((JSONObject) obj, val);
										list.add(val);
									} catch (InstantiationException ex) {
										ex.printStackTrace();
									}
								}
								f.set(o, list);
							}
						}
					} else if(newValue instanceof JSONObject){
						try {
							Object obj = f.getType().newInstance();
							f.set(o, load((JSONObject) newValue, obj));
						} catch (InstantiationException e) {
							e.printStackTrace();
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return o;
	}
}
