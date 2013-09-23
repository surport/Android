package com.ruyicai.android.test.model.intenet;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.ruyicai.android.tools.MathTools;

/**
 * 数学工具类测试
 * 
 * @author Administrator
 * @since RYC1.0 2013-4-12
 */
public class MathToolsTest extends AndroidTestCase {
	public static final String	TAG	= "MathToolsTest";

	public void testGetSpecifiedRangeRadomNumberWithoutRepetation() {
		for (int i = 0; i < 1000; i++) {
			List<Integer> randoms = MathTools
					.getSpecifiedRangeRadomNumberWithoutRepetation(0, 10, 2);
			Log.i(TAG, randoms.toString() + " " + randoms.size());
		}
	}
}
