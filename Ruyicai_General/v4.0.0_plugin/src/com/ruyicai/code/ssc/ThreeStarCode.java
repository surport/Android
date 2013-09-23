package com.ruyicai.code.ssc;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.constant.Constants;
import com.ruyicai.pojo.AreaNum;

public class ThreeStarCode extends CodeInterface {
	
	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int code) {
		String str = "";
		if (isZHmiss()) {
			str = "3D|";
			str += "-," + "-," + getIsZHcode();
		} else {
			switch (ssc_type) {
			case Constants.SSC_THREE:
				int[] bai = areaNums[0].table.getHighlightBallNOs();
				int[] shi = areaNums[1].table.getHighlightBallNOs();
				int[] ge = areaNums[2].table.getHighlightBallNOs();
				str = "3D|";
				str += "-," + "-," + getStr(bai) + "," + getStr(shi) + ","
						+ getStr(ge);
				break;
			case Constants.SSC_THREE_GROUP_THREE:
				int[] one = areaNums[0].table.getHighlightBallNOs();
				str = "Z3F|";
				str += getStr(one);
				break;
			case Constants.SSC_THREE_GROUP_SIX:
				int[] groupSix = areaNums[0].table.getHighlightBallNOs();
				if (groupSix.length == 3) {
					str = "6|";
				} else {
					str = "Z6F|";
				}
				str += getStr(groupSix);
                break;
		    }
		}
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
