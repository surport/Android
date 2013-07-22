package com.ruyicai.code.ssc;

import android.util.Log;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class OneStarCode extends CodeInterface {

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int code) {
		String str = "";
		str = "1D|";

		// TODO Auto-generated method stub
		int zhuma[] = areaNums[0].table.getHighlightBallNOs();
		str += "-," + "-," + "-," + "-," + getStr(zhuma);
		return str;
	}

	public String getStr(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
		}
		return str;

	}
}