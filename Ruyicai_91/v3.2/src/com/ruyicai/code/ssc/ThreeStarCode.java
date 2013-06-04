package com.ruyicai.code.ssc;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class ThreeStarCode extends CodeInterface{

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu,int code) {
		int [] bai = areaNums[0].table.getHighlightBallNOs();
		int [] shi = areaNums[1].table.getHighlightBallNOs();
		int [] ge  = areaNums[2].table.getHighlightBallNOs();
		String str = "";
			str = "3D|";
			str += "-," + "-," + getStr(bai) + "," + getStr(shi) + ","+ getStr(ge);		
			
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
