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
		// TODO Auto-generated method stub
		try {
			if(type.equals(MissConstant.NMK3_HEZHI)
					||type.equals(MissConstant.NMK3_THREE_TWO)
					||type.equals(MissConstant.NMK3_THREE_DAN_FU)
					||type.equals(MissConstant.NMK3_TWOSAME_FU)
					||type.equals(MissConstant.NMK3_THREESAME_TONG)
					||type.equals(MissConstant.NMK3_THREE_LINK_TONG)){
				missList.add(getJsonArray(json.getJSONArray(MISS)));
			}else if(type.equals(MissConstant.NMK3_TWO_DAN)){
				missList.add(getJsonArray(json.getJSONArray(DAN)));
				missList.add(getJsonArray(json.getJSONArray(SHUANG)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return missList;
	}
}
