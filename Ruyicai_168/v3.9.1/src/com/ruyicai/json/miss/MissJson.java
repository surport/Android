package com.ruyicai.json.miss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class MissJson {
	public List<List> missList = new ArrayList<List>();
	public List<String> zMissList = new ArrayList<String>();
	private final String MISS = "miss";

	public List<String> getMissKey() {
		return null;
	}

	public abstract List<List> jsonToList(String type, JSONObject json);

	public List<String> getJsonArray(JSONArray jsonArray) throws JSONException {
		List<String> missValues = new ArrayList<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			missValues.add(jsonArray.getString(i));
		}
		return missValues;
	}
}
