package com.ruyicai.code.ssc;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class BigSamllCode extends CodeInterface {

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		// TODO Auto-generated method stub
		String str = "";
		int[] shi = areaNums[0].table.getHighlightBallNOsbigsmall();
		int[] ge = areaNums[1].table.getHighlightBallNOsbigsmall();
		str = "DD|";
		str += getStr(shi) + getStr(ge);
		return str;
	}

	public String getStr(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {

			switch (balls[i]) {
			case 1:
				str += "2";// 大
				break;
			case 2:
				str += "1";// 小
				break;
			case 3:
				str += "5";// 单
				break;
			case 4:
				str += "4";// 双
				break;
			}

		}
		return str;

	}

}
