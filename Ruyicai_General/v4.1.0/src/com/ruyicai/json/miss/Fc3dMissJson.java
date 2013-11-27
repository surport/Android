package com.ruyicai.json.miss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fc3dMissJson extends MissJson {
	private final String BAI = "bai";
	private final String SHI = "shi";
	private final String GE = "ge";
	private final String MISS = "miss";

	public List<List> jsonToList(String type, JSONObject json) {
		try {
			if (type.equals(MissConstant.FC3D_Z36)) {
				missList.add(getJsonArray(json.getJSONArray(MISS)));
			} else if (type.equals(MissConstant.FC3D_ZX)) {
				missList.add(getJsonArray(json.getJSONArray(BAI)));
				missList.add(getJsonArray(json.getJSONArray(SHI)));
				missList.add(getJsonArray(json.getJSONArray(GE)));
			} else if (type.equals(MissConstant.FC3D_ZXHZ)) {
				missList.add(getJsonArray(json.getJSONArray(MISS)));
			} else if (type.equals(MissConstant.FC3D_Z36HZ)) {
				missList.add(getJsonArray(json.getJSONArray(MISS)));
			} else if (type.equals(MissConstant.FC3D_TYPE_Z6HZ)) {
				missList.add(getZ6HZJsonArray(json.getJSONArray(MISS)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return missList;
	}

	public List<String> getZ6HZJsonArray(JSONArray jsonArray)
			throws JSONException {
		List<String> missValues = new ArrayList<String>();
		for (int i = 2; i < jsonArray.length(); i++) {
			missValues.add(jsonArray.getString(i));
		}
		return missValues;
	}
}
