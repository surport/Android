/**
 * 
 */
package com.ruyicai.util;

import com.palmdream.RuyicaiAndroid.R;

/**
 * 选区信息类
 * 
 * @author Administrator
 * 
 */
public class AreaInfo {
	public int areaNum = 33;// 选区小球个数
	public int chosenBallSum = 6;// 选区最多选择小球数
	public int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	public int aIdStart = 0;// 选区小球起始id
	public int aBallViewText = 1;// 小球的起始值
	public int textColor = 0;// 注码的颜色
	public String textTtitle;// 选区标题

	/**
	 * 选区信息类
	 * 
	 * @param areaNum
	 *            选区小球数
	 * @param chosenBallSum
	 *            可选小球数
	 * @param ballResId
	 *            选区小球的类型id，例如，{ R.drawable.grey, R.drawable.red }
	 * @param startId
	 *            小球起始id 一般为“0”
	 * @param startText
	 *            小球其实数字 一般为”1“
	 * @param textColor
	 *            小球颜色
	 * @param textView
	 *            选区标示，例"蓝球区:"
	 */
	public AreaInfo(int areaNum, int chosenBallSum, int ballResId[],
			int startId, int startText, int textColor, String textView) {
		this.areaNum = areaNum;
		this.chosenBallSum = chosenBallSum;
		this.ballResId = ballResId;
		this.aIdStart = startId;
		this.aBallViewText = startText;
		this.textColor = textColor;
		this.textTtitle = textView;
	}
}
