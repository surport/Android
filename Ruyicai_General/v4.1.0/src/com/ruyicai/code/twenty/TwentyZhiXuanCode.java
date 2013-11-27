package com.ruyicai.code.twenty;

import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;

public class TwentyZhiXuanCode extends CodeInterface {

	@Override
	public String zhuma(AreaNum[] areaNums, int beishu, int type) {
		int zhuma[] = null;
		zhuma = areaNums[0].table.getHighlightBallNOs();
		int iRedHighlights = areaNums[0].table.getHighlightBallNums();
		String t_str = "";
		if (iRedHighlights == 5) {
			t_str += "0@";
		} else if (iRedHighlights > 5) {
			t_str += "1@";
		}
		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
		}

		t_str += "^";
		return t_str;
	}

}
