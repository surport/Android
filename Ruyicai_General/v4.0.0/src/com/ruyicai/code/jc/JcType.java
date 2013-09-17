package com.ruyicai.code.jc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.palmdream.RuyicaiAndroid.R;

public class JcType {
	private static final String JC_D = "500";
	private static final String JC2_1 = "502";
	private static final String JC3_1 = "503";
	private static final String JC4_1 = "504";
	private static final String JC5_1 = "505";
	private static final String JC6_1 = "506";
	private static final String JC7_1 = "507";
	private static final String JC8_1 = "508";

	private static final String JC3_3 = "526";
	private static final String JC3_4 = "527";
	private static final String JC4_4 = "539";
	private static final String JC4_5 = "540";
	private static final String JC4_6 = "528";
	private static final String JC4_11 = "529";
	private static final String JC5_5 = "544";
	private static final String JC5_6 = "545";
	private static final String JC5_10 = "530";
	private static final String JC5_16 = "541";
	private static final String JC5_20 = "531";
	private static final String JC5_26 = "532";
	private static final String JC6_6 = "549";
	private static final String JC6_7 = "550";
	private static final String JC6_15 = "533";
	private static final String JC6_20 = "542";
	private static final String JC6_22 = "546";
	private static final String JC6_35 = "534";
	private static final String JC6_42 = "543";
	private static final String JC6_50 = "535";
	private static final String JC6_57 = "536";
	private static final String JC7_7 = "553";
	private static final String JC7_8 = "554";
	private static final String JC7_21 = "551";
	private static final String JC7_35 = "547";
	private static final String JC7_120 = "537";
	private static final String JC8_8 = "556";
	private static final String JC8_9 = "557";
	private static final String JC8_28 = "555";
	private static final String JC8_56 = "552";
	private static final String JC8_70 = "548";
	private static final String JC8_247 = "538";
	private int textId[] = { R.string.jc_touzhu_DAN, R.string.jc_touzhu_check1,
			R.string.jc_touzhu_check2, R.string.jc_touzhu_check3,
			R.string.jc_touzhu_check4, R.string.jc_touzhu_check5,
			R.string.jc_touzhu_check6, R.string.jc_touzhu_check7,
			R.string.jc_touzhu_radio3_3, R.string.jc_touzhu_radio3_4,
			R.string.jc_touzhu_radio4_4, R.string.jc_touzhu_radio4_5,
			R.string.jc_touzhu_radio4_6, R.string.jc_touzhu_radio4_11,
			R.string.jc_touzhu_radio5_5, R.string.jc_touzhu_radio5_6,
			R.string.jc_touzhu_radio5_10, R.string.jc_touzhu_radio5_16,
			R.string.jc_touzhu_radio5_20, R.string.jc_touzhu_radio5_26,
			R.string.jc_touzhu_radio6_6, R.string.jc_touzhu_radio6_7,
			R.string.jc_touzhu_radio6_15, R.string.jc_touzhu_radio6_20,
			R.string.jc_touzhu_radio6_22, R.string.jc_touzhu_radio6_35,
			R.string.jc_touzhu_radio6_42, R.string.jc_touzhu_radio6_50,
			R.string.jc_touzhu_radio6_57, R.string.jc_touzhu_radio7_7,
			R.string.jc_touzhu_radio7_8, R.string.jc_touzhu_radio7_21,
			R.string.jc_touzhu_radio7_35, R.string.jc_touzhu_radio7_120,
			R.string.jc_touzhu_radio8_8, R.string.jc_touzhu_radio8_9,
			R.string.jc_touzhu_radio8_28, R.string.jc_touzhu_radio8_56,
			R.string.jc_touzhu_radio8_70, R.string.jc_touzhu_radio8_247 };
	private String[] values = { JC_D, JC2_1, JC3_1, JC4_1, JC5_1, JC6_1, JC7_1,
			JC8_1, JC3_3, JC3_4, JC4_4, JC4_5, JC4_6, JC4_11, JC5_5, JC5_6,
			JC5_10, JC5_16, JC5_20, JC5_26, JC6_6, JC6_7, JC6_15, JC6_20,
			JC6_22, JC6_35, JC6_42, JC6_50, JC6_57, JC7_7, JC7_8, JC7_21,
			JC7_35, JC7_120, JC8_8, JC8_9, JC8_28, JC8_56, JC8_70, JC8_247 };
	private final int DAN_TYPE = 100;
	Map<String, Object> map = new HashMap<String, Object>();

	public JcType(Context context) {
		for (int i = 0; i < values.length; i++) {
			map.put(context.getString(textId[i]), values[i]);
		}
	}

	public String getValues(String key) {
		return (String) map.get(key);
	}

	public int getDanType() {
		return DAN_TYPE;
	}
}
