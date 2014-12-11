package com.lelann.json.data;
/**
 * This interface is useful to save an object with JSONObjet.set(String where, Object what)
 *
 */
public interface JStorable {
	/**
	 * Return the Object as an JSONObject
	 */
	public JSONObject save();
}
