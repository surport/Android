/**
 * 
 */
package com.ruyicai.code.pl3;

import com.ruyicai.activity.buy.pl3.PL3ZiZuXuan;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicConst;

/**
 * @author Administrator
 * 
 */
public class PL3ZiZuXuanCode extends CodeInterface {


	String zuxuan = "6|";// 组选单式（包括组三和组六）
	String zu3fushi = "F3|";// 组三复式
	String zu6fushi = "F6|";// 组六复式

	public String zhuma(AreaNum[] areaNums, int beishu,int type) {

		// TODO Auto-generated method stub
		String strZM = "";
		// 排列三组三玩法
		if (PL3ZiZuXuan.iCurrentButton == PublicConst.BUY_PL3_ZU3) {
			if (PL3ZiZuXuan.isDanFu) {// 单式
				strZM = zuxuan;
				int[] zhuma_zu3danshi_A1 = areaNums[0].table.getHighlightBallNOs();
				int[] zhuma_zu3danshi_A2 =  areaNums[1].table.getHighlightBallNOs();
				int[] zhuma_zu3danshi_A3 =  areaNums[2].table.getHighlightBallNOs();

				if (zhuma_zu3danshi_A1.length > 0&& zhuma_zu3danshi_A2.length > 0&& zhuma_zu3danshi_A3.length > 0) {
					strZM += (zhuma_zu3danshi_A3[0]) + ","
							+ (zhuma_zu3danshi_A1[0]) + ","
							+ (zhuma_zu3danshi_A1[0]);
				}

			} else {// 复式也叫组三包号
				strZM = zu3fushi;
				int[] zhuma_zu3fushi =  areaNums[0].table.getHighlightBallNOs();
				if (zhuma_zu3fushi.length > 0) {
					for (int i = 1; i < zhuma_zu3fushi.length + 1; i++) {
						strZM += zhuma_zu3fushi[i - 1] + "";
					}
				}

			}

		}
		// 排列三组六玩法
		else if (PL3ZiZuXuan.iCurrentButton == PublicConst.BUY_PL3_ZU6) {
			int[] zhuma_zu6danfushi =  areaNums[0].table.getHighlightBallNOs();
			if (zhuma_zu6danfushi.length == 3) {// 单式
				strZM = zuxuan + (zhuma_zu6danfushi[0]) + ","
						+ (zhuma_zu6danfushi[1]) + "," + (zhuma_zu6danfushi[2]);
			}
			if (zhuma_zu6danfushi.length > 3) {// 复式也叫组六包号
				strZM = zu6fushi;
				for (int i = 1; i < zhuma_zu6danfushi.length + 1; i++) {
					strZM += zhuma_zu6danfushi[i - 1] + "";
				}
			}

		}
		return strZM;
	}


	

}
