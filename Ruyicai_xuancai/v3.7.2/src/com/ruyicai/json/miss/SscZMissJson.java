package com.ruyicai.json.miss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 时时彩组合遗漏
 * 
 * @author win
 * 
 */
public class SscZMissJson extends MissJson {
	public List<String> keyList = new ArrayList<String>();
	public Map<String, String> missMap = new TreeMap<String, String>();

	private String sellWay;

	@Override
	public List<List> jsonToList(String sellWay, JSONObject json) {
		// TODO Auto-generated method stub
		this.sellWay = sellWay;
		pxJson(json);
		return null;
	}

	private void pxJson(JSONObject json) {
		try {
			String key = null;
			String value = null;
			JSONArray names = json.names();
			int[] intName = new int[names.length()];
			for (int i = 0; i < names.length(); i++) {
				key = names.getString(i);
				value = json.getString(key);
				intName[i] = Integer.parseInt(value);
			}
			int[] intIndex = rankListMiss(intName);
			for (int i : intIndex) {
				key = names.getString(i);
				value = json.getString(key);
				keyList.add(key);
				zMissList.add(value);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Map<String, String> toMap(JSONObject jsonObject)
			throws JSONException {
		Map<String, String> result = new HashMap<String, String>();
		Iterator<String> iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);
		}
		return result;
	}

	public List<String> getMissKey() {
		return keyList;
	}

	/**
	 * 冒泡排序
	 * 
	 * @param myArray
	 * @return
	 */
	public static int[] rankListMiss(int[] rankInt) {
		int[] rankIndex = new int[rankInt.length];
		for (int n = 0; n < rankInt.length; n++) {
			rankIndex[n] = n;
		}
		// 取长度最长的词组 -- 冒泡法
		for (int j = 1; j < rankInt.length; j++) {
			for (int i = 0; i < rankInt.length - j; i++) {
				// 如果 myArray[i] > myArray[i+1] ，则 myArray[i] 上浮一位
				if (rankInt[i] < rankInt[i + 1]) {
					int temp = rankInt[i];
					rankInt[i] = rankInt[i + 1];
					rankInt[i + 1] = temp;

					int index = rankIndex[i];
					rankIndex[i] = rankIndex[i + 1];
					rankIndex[i + 1] = index;
				}
			}
		}
		return rankIndex;
	}
}
