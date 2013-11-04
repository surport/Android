package com.ruyicai.json.miss;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Nmk3MissJson extends MissJson {
	private final String MISS = "miss";
	private final String DAN="dan";
	private final String SHUANG="shuang";

	@Override
	public List<List> jsonToList(String type, JSONObject json) {
		try {
			// 和值，二不同
			if (type.equals(MissConstant.NMK3_HEZHI)
					|| type.equals(MissConstant.NMK3_THREE_TWO)
					|| type.equals(MissConstant.NMK3_TWOSAME_FU)) {
				missList.add(getJsonArray(json.getJSONArray(MISS)));
			}
			// 二同号单选
			else if (type.equals(MissConstant.NMK3_TWO_DAN)) {
				missList.add(getJsonArray(json.getJSONArray(DAN)));
				missList.add(getJsonArray(json.getJSONArray(SHUANG)));
			}
			// 三不同，三同号
			else if (type.equals(MissConstant.NMK3_THREE_TWO + ";"
					+ MissConstant.NMK3_THREE_LINK_TONG)
					|| type.equals(MissConstant.NMK3_THREE_DAN_FU + ";"
							+ MissConstant.NMK3_THREESAME_TONG)) {
				String[] types = type.split(";");
				missList.add(getJsonArray(json.getJSONObject(types[0])
						.getJSONArray(MISS)));
				missList.add(getJsonArray(json.getJSONObject(types[1])
						.getJSONArray(MISS)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return missList;
	}
}
