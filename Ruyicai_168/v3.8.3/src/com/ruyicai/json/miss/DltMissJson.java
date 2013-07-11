package com.ruyicai.json.miss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class DltMissJson extends MissJson {
	private final String QIAN = "qian";
	private final String HOU = "hou";

	public List<List> jsonToList(String type, JSONObject json) {
		try {
			if (type.equals(MissConstant.DLT_DAN)) {
				missList.add(getJsonArray(json.getJSONArray(QIAN)));
				missList.add(getJsonArray(json.getJSONArray(QIAN)));
				missList.add(getJsonArray(json.getJSONArray(HOU)));
				missList.add(getJsonArray(json.getJSONArray(HOU)));
			} else if (type.equals(MissConstant.DLT_12_2)) {
				missList.add(getJsonArray(json.getJSONArray(HOU)));
			} else {
				missList.add(getJsonArray(json.getJSONArray(QIAN)));
				missList.add(getJsonArray(json.getJSONArray(HOU)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return missList;
	}

}
