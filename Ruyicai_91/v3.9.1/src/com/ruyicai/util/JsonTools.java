package com.ruyicai.util;

import org.json.JSONObject;

import android.text.TextUtils;

public final class JsonTools {

	private JsonTools() {
	}

	public static String safeParseJSONObjectForValueIsString(
			JSONObject jsonObject, String key, String defaultValue)
			throws Exception {
		if (null == jsonObject || TextUtils.isEmpty(key)) {
			throw new NullPointerException("jsonObject or key is null");
		}
		String values = defaultValue;
		if (jsonObject.has(key) && !jsonObject.isNull(key)) {
			values = jsonObject.getString(key);
		}
		return values;
	}

}
