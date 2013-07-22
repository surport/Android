/**
 * 
 */
package com.ruyicai.code.pl3;

import com.ruyicai.activity.buy.pl3.PL3;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicConst;

/**
 * @author Administrator
 * 
 */
public class PL3ZiHeZhiCode extends CodeInterface {

	String zxHHH = "S1|";// 直选和值
	String z3HHH = "S3|";// 组3和值
	String z6HHH = "S6|";// 组6和值
	private int iCurrentButton;

	public int getiCurrentButton() {
		return iCurrentButton;
	}

	public void setiCurrentButton(int iCurrentButton) {
		this.iCurrentButton = iCurrentButton;
	}

	public String zhuma(AreaNum[] areaNums, int beishu, int type) {

		// TODO Auto-generated method stub
		String strZM = "";
		if (iCurrentButton == PublicConst.BUY_PL3_HEZHI_ZHIXUAN) {
			strZM = zxHHH;
			int[] zhuma_zhixuanhezhi = areaNums[0].table.getHighlightBallNOs();
			for (int i = 0; i < zhuma_zhixuanhezhi.length; i++) {
				strZM += (zhuma_zhixuanhezhi[i]) + ",";
				if (i == zhuma_zhixuanhezhi.length - 1) {
					strZM = strZM.substring(0, strZM.length() - 1);
				}
			}

		} else if (iCurrentButton == PublicConst.BUY_PL3_HEZHI_ZU3) {
			strZM = z3HHH;
			int[] zhuma_zu3hezhi = areaNums[0].table.getHighlightBallNOs();
			for (int i = 0; i < zhuma_zu3hezhi.length; i++) {
				strZM += (zhuma_zu3hezhi[i]) + ",";
				if (i == zhuma_zu3hezhi.length - 1) {
					strZM = strZM.substring(0, strZM.length() - 1);
				}
			}

		} else if (iCurrentButton == PublicConst.BUY_PL3_HEZHI_ZU6) {
			strZM = z6HHH;
			int[] zhuma_zu6hezhi = areaNums[0].table.getHighlightBallNOs();
			for (int i = 0; i < zhuma_zu6hezhi.length; i++) {
				strZM += (zhuma_zu6hezhi[i]) + ",";
				if (i == zhuma_zu6hezhi.length - 1) {
					strZM = strZM.substring(0, strZM.length() - 1);
				}
			}
		}
		return strZM;
	}

}
