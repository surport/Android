package com.ruyicai.json.miss.ssc;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.MissConstant;

/**
 * 时时彩遗漏值解析
 * @author Administrator
 *
 */
public class SscMissJson {
	private final String GE = "ge";
	private final String SHI = "shi";
	private final String BAI = "bai";
	private final String QIAN = "qian";
	private final String WAN = "wan";
	private final String GE_DX = "geDX";
	private final String SHI_DX = "shiDX";
	private final String MISS = "miss";
	public  List<List> missList5X = null;
	public SscMissJson() {
	}
	public  List<List>  jsonToList(String sellWay,JSONObject json){
		if(sellWay.equals(MissConstant.SSC_5X_ZX)){
			missList5X = json5x(json);
			return missList5X;
		}else if(sellWay.equals(MissConstant.SSC_MV_DD)){
			missList5X = jsonDx(json);
			return missList5X;
		}else if(sellWay.equals(MissConstant.SSC_MV_2ZX)||sellWay.equals(MissConstant.SSC_MV_2ZXHZ)){
			missList5X = jsonZx(json);
			return missList5X;
		}
		return null;
	}
	/**
	 * 解析时时彩组选和和值
	 */
	public List<List>  jsonZx(JSONObject json) {
		List<List> missList = new ArrayList<List>();
		try {
			missList.add(getJsonArray(json.getJSONArray(MISS)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return missList;
	}
	/**
	 * 解析时时彩5星通选
	 */
	public List<List>  json5x(JSONObject json) {
		List<List> missList = new ArrayList<List>();
		try {
			missList.add(getJsonArray(json.getJSONArray(GE)));
			missList.add(getJsonArray(json.getJSONArray(SHI)));
			missList.add(getJsonArray(json.getJSONArray(BAI)));
			missList.add(getJsonArray(json.getJSONArray(QIAN)));
			missList.add(getJsonArray(json.getJSONArray(WAN)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return missList;
	}
	/**
	 * 解析时时彩大小单双
	 */
	public List<List>  jsonDx(JSONObject json) {
		List<List> missList = new ArrayList<List>();
		try {
			missList.add(getJsonArray(json.getJSONArray(GE_DX)));
			missList.add(getJsonArray(json.getJSONArray(SHI_DX)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return missList;
	}

	private List<String> getJsonArray(JSONArray jsonArray) throws JSONException {
		List<String> missValues = new ArrayList<String>();
		for(int i=0;i<jsonArray.length();i++){
			missValues.add(jsonArray.getString(i));
		}
		return missValues;
	}
}
