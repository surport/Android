package com.ruyicai.code.ssc;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.constant.Constants;
import com.ruyicai.pojo.AreaNum;

public class FiveStarCode extends CodeInterface {

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		String str = "";
		int wan[] = areaNums[0].table.getHighlightBallNOs();
		int qian[] = areaNums[1].table.getHighlightBallNOs();
		int bai[] = areaNums[2].table.getHighlightBallNOs();
		int shi[] = areaNums[3].table.getHighlightBallNOs();
		int ge[] = areaNums[4].table.getHighlightBallNOs();
		switch (type) {
		case Constants.SSC_FIVESTAR_ZHIXUAN:
			str = "5D|";
			// TODO Auto-generated method stub
			str += getStr(wan) + "," + getStr(qian) + "," + getStr(bai) + ","
					+ getStr(shi) + "," + getStr(ge);
			break;
		case Constants.SSC_FIVESTAR_TONGXUAN:
			str = "5T|";
			// TODO Auto-generated method stub
			str += getStr(wan) + "," + getStr(qian) + "," + getStr(bai) + ","
					+ getStr(shi) + "," + getStr(ge);
			break;
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
