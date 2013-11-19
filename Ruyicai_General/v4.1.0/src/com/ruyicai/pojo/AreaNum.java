/**
 * 
 */
package com.ruyicai.pojo;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.JiXuanBtn;
import com.ruyicai.util.PublicMethod;

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
	public int isNum = 8;// 每一行的小球数
	public boolean isRed;
	public boolean isJxBtn = false;
	public boolean isSensor = false;
	public int areaMin;
	public JiXuanBtn jixuanBtn;
	public boolean isAgain = false;// 选区是否可以重复
	
	public String textViewTishi;//投注提示
	public TextView texViewTishiTitle;
	public int [] area={5,6};

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
	public AreaNum(int areaNum, int isNum, int chosenBallSum, int ballResId[],
			int startId, int startText, int textColor, String textView) {
		this.areaNum = areaNum;
		this.isNum = isNum;
		this.chosenBallSum = chosenBallSum;
		this.ballResId = ballResId;
		this.aIdStart = startId;
		this.aBallViewText = startText;
		this.textColor = textColor;
		this.textTtitle = textView;
		setIsRed(textColor);
	}

	public AreaNum(int areaNum, int isNum, int areaMin, int chosenBallSum,
			int ballResId[], int startId, int startText, int textColor,
			String textView, boolean isJxBtn, boolean isSensor) {
		this.areaNum = areaNum;
		this.isNum = isNum;
		this.areaMin = areaMin;
		this.chosenBallSum = chosenBallSum;
		this.ballResId = ballResId;
		this.aIdStart = startId;
		this.aBallViewText = startText;
		this.textColor = textColor;
		this.textTtitle = textView;
		this.isJxBtn = isJxBtn;
		this.isSensor = isSensor;
		this.isNum = isNum;
		setIsRed(textColor);
	}

	public AreaNum(int areaNum, int isNum, int areaMin, int chosenBallSum,
			int ballResId[], int startId, int startText, int textColor,
			String textView, boolean isJxBtn, boolean isSensor, boolean isAgain) {
		this.areaNum = areaNum;
		this.isNum = isNum;
		this.areaMin = areaMin;
		this.chosenBallSum = chosenBallSum;
		this.ballResId = ballResId;
		this.aIdStart = startId;
		this.aBallViewText = startText;
		this.textColor = textColor;
		this.textTtitle = textView;
		this.isJxBtn = isJxBtn;
		this.isSensor = isSensor;
		this.isNum = isNum;
		this.isAgain = isAgain;
		setIsRed(textColor);
	}
	
	
	/**
	 * 
	 * @param areaNum 小球数组
	 * @param isNum
	 * @param areaMin
	 * @param chosenBallSum
	 * @param ballResId
	 * @param startId
	 * @param startText
	 * @param textColor
	 * @param textView
	 * @param textViewTishi
	 * @param isJxBtn
	 * @param isSensor
	 * @param isAgain
	 */
	public AreaNum(int[] areaNum, int areaMin, int chosenBallSum,
			int ballResId[], int startId, int startText, int textColor,
			String textView,String textViewTishi, boolean isJxBtn, boolean isSensor, boolean isAgain) {
		this.area=areaNum;
		int num=0;
		for(int i=0;i<areaNum.length;i++){
			num+=areaNum[i];
		}
		this.areaNum = num;
		this.areaMin = areaMin;
		this.chosenBallSum = chosenBallSum;
		this.ballResId = ballResId;
		this.aIdStart = startId;
		this.aBallViewText = startText;
		this.textColor = textColor;
		this.textTtitle = textView;
		this.textViewTishi = textViewTishi;
		this.isJxBtn = isJxBtn;
		this.isSensor = isSensor;
		this.isAgain = isAgain;
		setIsRed(textColor);
	}

	public void setIsRed(int textColor) {
		if (textColor == Color.RED) {
			isRed = true;
		} else {
			isRed = false;
		}
	}

	public void initView(int tableLayoutId, int textViewId, View view) {
		tableLayout = (TableLayout) view.findViewById(tableLayoutId);
		textTitle = (TextView) view.findViewById(textViewId);
	}

	public void initView(int tableLayoutId, int textViewId, int linearViewId,
			View view) {
		layout = (LinearLayout) view.findViewById(linearViewId);
		layout.setVisibility(LinearLayout.VISIBLE);
		tableLayout = (TableLayout) view.findViewById(tableLayoutId);
		textTitle = (TextView) view.findViewById(textViewId);

	}
	/*add by xups start*/
	public void initView(int tableLayoutId, int textViewId,int linearViewId,int texViewTishiId,
			View view) {
		layout = (LinearLayout) view.findViewById(linearViewId);
		layout.setVisibility(LinearLayout.VISIBLE);
		tableLayout = (TableLayout) view.findViewById(tableLayoutId);
		textTitle = (TextView) view.findViewById(textViewId);
		texViewTishiTitle=(TextView)view.findViewById(texViewTishiId);
		if(TextUtils.isEmpty(textTtitle)&&TextUtils.isEmpty(textViewTishi)){
			textTitle.setVisibility(View.GONE);
			texViewTishiTitle.setVisibility(View.GONE);
		}

	}
	/*add by xups end*/
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
	public void init(int type) {
		textTitle.setText(textTtitle);
		if (type == ZixuanAndJiXuan.NMK3_HEZHI
				|| type == ZixuanAndJiXuan.NMK3_DIFF_THREE
				|| type == ZixuanAndJiXuan.NMK3_DIFF_TWO
				|| type == ZixuanAndJiXuan.NMK3_THREESAME
				|| type == ZixuanAndJiXuan.NMK3_TWOSAME_FU
				|| type == ZixuanAndJiXuan.NMK3_TWOSAME_DAN) {
			textTitle.setTextSize(14);
			textTitle.setTextColor(Color.WHITE);
		}
	}

	/*by add xups start*/
	/**
	 * 初始化选区提示
	 */
	public void initTishi() {
		texViewTishiTitle.setText(textViewTishi);
	}
	public void initTextColor(int textTitleColor,int textTishiColor){
		if(textTitleColor!=0){
			textTitle.setTextColor(textTitleColor);
		}
		if(textTishiColor!=0){
			texViewTishiTitle.setTextColor(textTishiColor);
		}
	}
	public void initTextBg(int textViewBg,int textViewTishiBg){
		if(textViewBg!=0){
			textTitle.setBackgroundResource(textViewBg);
		}
		if(textViewTishiBg!=0){
			texViewTishiTitle.setBackgroundResource(textViewTishiBg);
		}
	}
	public void initTextBgColor(int textViewBgColor,int textViewTishiBgColor){
		if(textViewBgColor!=0){
			textTitle.setBackgroundColor(textViewBgColor);
		}
		if(textViewTishiBgColor!=0){
			texViewTishiTitle.setBackgroundColor(textViewTishiBgColor);
		}
	}
	/*by add xups end*/
	public void removeView() {
		tableLayout.removeAllViews();
		table.clearAllBall();
	}
}
