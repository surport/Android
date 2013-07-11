package com.ruyicai.json.miss;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class SsqMissJson extends MissJson {
	private final String BLUE = "blue";
	private final String RED = "red";

	public List<List> jsonToList(String type, JSONObject json) {
		try {
			if (type.equals(MissConstant.SSQ_DAN)) {
				missList.add(getJsonArray(json.getJSONArray(RED)));
				missList.add(getJsonArray(json.getJSONArray(RED)));
				missList.add(getJsonArray(json.getJSONArray(BLUE)));
			} else {
				missList.add(getJsonArray(json.getJSONArray(RED)));
				missList.add(getJsonArray(json.getJSONArray(BLUE)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return missList;
	}

}
