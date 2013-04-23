package com.ruyicai.activity.buy.pl3;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanActivity;
import com.ruyicai.code.pl3.PL3ZiHeZhiCode;
import com.ruyicai.code.pl3.PL3ZiZuXuanCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class PL3ZuLiu extends ZixuanActivity implements BuyImplement,OnCheckedChangeListener{
	private int iCurrentButton;
	private LinearLayout topLinear;
	private LinearLayout topLinearTwo;
	private RadioGroup topButton;
	private String topTitle[] = { "组六", "组六和值" };
	private AreaInfo areaInfos[];// 选区
	private PL3ZiZuXuanCode pl3Code = new PL3ZiZuXuanCode();
	private PL3ZiHeZhiCode pl3CodeHZ = new PL3ZiHeZhiCode();
	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	BallTable oneBallTable;

	/**
	 * 初始化组件
	 */
	public void init() {
		topLinear = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top);
		topLinearTwo = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top_two);
		topLinear.setVisibility(LinearLayout.VISIBLE);
		topLinearTwo.setVisibility(LinearLayout.VISIBLE);
		initRadioGroup();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	
	/**
	 * 初始化单选按钮组
	 */
	public void initRadioGroup() {
		topButton = (RadioGroup) findViewById(R.id.buy_zixuan_radiogroup_top);
		for (int i = 0; i < topTitle.length; i++) {
			RadioButton radio = new RadioButton(this);
			radio.setText(topTitle[i]);
			radio.setTextColor(Color.BLACK);
			radio.setTextSize(13);
			radio.setId(i);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setPadding(Constants.PADDING, 0, 10, 0);
			topButton.addView(radio);

		}
		topButton.setOnCheckedChangeListener(this);
		topButton.check(0);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (group.getId()) {
		case R.id.buy_zixuan_radiogroup_top:
			switch (checkedId) {
			case 0://组六直选
				iCurrentButton = PublicConst.BUY_PL3_ZU6;
				create_ZU6();
				break;
			case 1:// 组六和值
				iCurrentButton = PublicConst.BUY_PL3_HEZHI_ZU6;
				create_PL3_HEZHI_ZU6();
				break;
			}
		}
		
	}
	/**
	 * 福彩3D和值组6
	 */
	private void create_PL3_HEZHI_ZU6() {
		pl3CodeHZ.setiCurrentButton(iCurrentButton);
        String title = getResources().getString(R.string.fc3d_text_hezhi_title).toString();
		areaInfos[0] = new AreaInfo(22, 1, ballResId, 0, 3,Color.RED,title);
		createView(areaInfos, pl3CodeHZ,this,false);
		oneBallTable = areaNums[0].table;
	}
	/**
	 * 组六
	 */
	private void create_ZU6() {
		pl3Code.setiCurrentButton(iCurrentButton);
		areaInfos = new AreaInfo[1];
		iCurrentButton = PublicConst.BUY_PL3_ZU6;
		String title = getResources().getString(R.string.fc3d_text_hezhi_title).toString();
		areaInfos[0] = new AreaInfo(10, 9, ballResId, 0, 0, Color.RED, title);
		createView(areaInfos, pl3Code, this,true);
		oneBallTable = areaNums[0].table;
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String mTextSumMoney = "";
		int iZhuShu = getZhuShu();
		switch (iCurrentButton) {
		
			case PublicConst.BUY_PL3_HEZHI_ZU6:
			
				if(iZhuShu==0){
					mTextSumMoney = "需要1个球";
				}else{
					mTextSumMoney = "共"+ (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
				}
			break;
			case PublicConst.BUY_PL3_ZU6:	// 福彩3D组6
				if (oneBallTable.getHighlightBallNums() > 2) {
					mTextSumMoney = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
				}else{
					int num = 3 - oneBallTable.getHighlightBallNums();
					mTextSumMoney = "还需要"+num+"个球";
				}
			break;
		}
		return mTextSumMoney;
	}
	/**
	 * 
	 * @return
	 */
     public int getZhuShu(){
    	int iReturnValue = 0;
 		// 福彩3D组3
 		switch (iCurrentButton) {
 		case PublicConst.BUY_PL3_ZU6:
			int iZu6Balls = oneBallTable.getHighlightBallNums();
			iReturnValue = (int) getFc3dZu6FushiZhushu(iZu6Balls);
 			break;
 		case PublicConst.BUY_PL3_HEZHI_ZU6:
 			iReturnValue= getFc3dZu6HezhiZhushu() ;
			break;
 		}
		return iReturnValue* iProgressBeishu ;
    	 
     }
 	/**
 	 * 获得福彩3D组6和值注数
 	 * 
 	 * @return 返回注数
 	 */
 	private int getFc3dZu6HezhiZhushu() {
 		int iZhuShu = 0;
 		int[] BallNos = oneBallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
 		int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9,
 				8, 7, 5, 4, 3, 2, 1, 1 };// 3~24

 		for (int i = 0; i < BallNos.length; i++) {
 			for (int j = 0; j < BallNoZhushus.length; j++) {
 				if (j == BallNos[i] - 3) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
 					// 删除倍数 cc 20100713
 					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
 					// String temp = "当前玩法为和值组6，共"
 					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
 					// mTextSumMoney.setText(temp);
 				}
 			}
 		}
 		return iZhuShu;
 	}
 	/**
  	 * 获得福彩3D组6复式注数
  	 * 
  	 * @param iZu6balls
  	 *            选择小球个数
  	 * @return 返回注数
  	 */
  	private long getFc3dZu6FushiZhushu(int iZu6balls) {
  		long tempzhushu = 0l;
  		if (iZu6balls > 0) {
  			tempzhushu += PublicMethod.zuhe(3, iZu6balls);
  		}
  		return tempzhushu;

  	}

	@Override
	public void isTouzhu() {
		// TODO Auto-generated method stub
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_ZU6:
			if (oneBallTable.getHighlightBallNums() < 3) {
				alertInfo("请至少选择3个小球后再投注");
			} else {
				int[] fushiNums = oneBallTable.getHighlightBallNOs();
				String fushiStr = "";
				for (int i = 0; i < fushiNums.length; i++) {
					fushiStr += (fushiNums[i]) + ",";
					if (i == fushiNums.length - 1) {
						fushiStr = fushiStr.substring(0,
								fushiStr.length() - 1);
					}
				}
				int iZhuShu = getZhuShu();

				if (iZhuShu * 2 > 100000) {
					dialogExcessive();
				} else {
					setZhuShu(iZhuShu);
					alert(  "注码：" + fushiStr );
				}
			}
			break;

		case PublicConst.BUY_PL3_HEZHI_ZU6:
			if (oneBallTable.getHighlightBallNums() < 1) {
				alertInfo("请选择小球号码后再投注");
			} else if (oneBallTable.getHighlightBallNums() == 1) {
				// wangyl 7.13 配合陈晨投注时用
				int iZhuShu = getZhuShu();
				String fushiStr = PublicMethod.getZhuMa(oneBallTable.getHighlightBallNOs()[0])+"";
				if (iZhuShu * 2 > 100000) {
					dialogExcessive();
				} else {
					setZhuShu(iZhuShu);
					alert( "注码：" + fushiStr);
				}
			}
			break;
		}
	}

	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId){
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_ZU6:
			areaNums[0].table.changeBallState(areaNums[0].info.chosenBallSum, iBallId);
			break;

		case PublicConst.BUY_PL3_HEZHI_ZU6:
			areaNums[0].table.clearAllHighlights();
			areaNums[0].table.changeBallState(areaNums[0].info.chosenBallSum, iBallId);
			  break;
		}


	}
	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_PL3);
	}

}