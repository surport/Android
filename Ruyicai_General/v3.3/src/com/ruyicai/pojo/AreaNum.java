/**
 * 
 */
package com.ruyicai.pojo;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;

/**
 * 选区类
 * 
 * @author Administrator
 */
public class AreaNum {
	public TableLayout tableLayout;
	public BallTable table;
	public TextView textTitle;
	public LinearLayout layout;

	public int areaNum = 33;// 选区小球个数
	public int chosenBallSum = 6;// 选区最多选择小球数
	public int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	public int aIdStart = 0;// 选区小球起始id
	public int aBallViewText = 1;// 小球的起始值
	public int textColor = 0;// 注码的颜色
	public String textTtitle;// 选区标题
    public int isNum = 8;//每一行的小球数

	/**
	 * 选区信息类
	 * 
	 * @param areaNum
	 *            选区小球数
	 * @param areaNum
	 *            每一行小球数
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
	public AreaNum(int areaNum,int isNum, int chosenBallSum, int ballResId[],int startId, int startText, int textColor, String textView) {
		this.areaNum = areaNum;
		this.isNum = isNum;
		this.chosenBallSum = chosenBallSum;
		this.ballResId = ballResId;
		this.aIdStart = startId;
		this.aBallViewText = startText;
		this.textColor = textColor;
		this.textTtitle = textView;
	}

	public void initView(int tableLayoutId, int textViewId, View view) {
		tableLayout = (TableLayout) view.findViewById(tableLayoutId);
		textTitle = (TextView) view.findViewById(textViewId);
	}

	public void initView(int tableLayoutId, int textViewId, int linearViewId,View view) {
		layout = (LinearLayout) view.findViewById(linearViewId);
		layout.setVisibility(LinearLayout.VISIBLE);
		tableLayout = (TableLayout) view.findViewById(tableLayoutId);
		textTitle = (TextView) view.findViewById(textViewId);

	}

	public AreaNum(int tableLayoutId, int textViewId, View view) {
		tableLayout = (TableLayout) view.findViewById(tableLayoutId);
		textTitle = (TextView) view.findViewById(textViewId);
	}

	public AreaNum(int tableLayoutId, int textViewId, int linearViewId,
			View view) {
		layout = (LinearLayout) view.findViewById(linearViewId);
		layout.setVisibility(LinearLayout.VISIBLE);
		tableLayout = (TableLayout) view.findViewById(tableLayoutId);
		textTitle = (TextView) view.findViewById(textViewId);

	}

	/**
	 * 初始化数据
	 */
	public void init() {
		textTitle.setText(textTtitle);
	}
	public void removeView(){
		tableLayout.removeAllViews();
		table.clearAllBall();
	}
}
