/**
 * 
 */
package com.ruyicai.activity.buy.pl3;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanActivity;
import com.ruyicai.code.pl3.PL3ZiZuXuanCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

/**
 * 排列三组选
 * @author Administrator
 *
 */
public class PL3ZiZuXuan extends ZixuanActivity implements BuyImplement, OnCheckedChangeListener{
	private LinearLayout topLinear;
	private LinearLayout topLinearOne;
	private LinearLayout topLinearTwo;
	private Button zu3Button,zu6Button;
	private RadioGroup topButton;
	private String topTitle[]={"单式","复式"};
	public static int iCurrentButton;//标签
	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] ;// 选区
	private PL3ZiZuXuanCode fc3dCode = new PL3ZiZuXuanCode();
	BallTable oneBallTable ;
	BallTable twoBallTable ;
	BallTable thirdBallTable ;
    public static boolean isDanFu = true;//true是单式
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init();
	}
	/**
	 * 初始化组件
	 */
	public void init(){
		topLinear = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top);
		topLinearOne = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top_one);
		topLinearTwo = (LinearLayout) findViewById(R.id.buy_zixuan_linear_top_two);
		topLinear.setVisibility(LinearLayout.VISIBLE);
		topLinearOne.setVisibility(LinearLayout.VISIBLE);
		topLinearTwo.setVisibility(LinearLayout.VISIBLE);
		initButton();
		initRadioGroup();
		create_ZU3();//默认
	}
	public void initButton(){
		zu3Button = (Button) findViewById(R.id.buy_zixuan_button_zu3);
		zu6Button = (Button) findViewById(R.id.buy_zixuan_button_zu6);
		zu3Button.setText("组三");
		zu6Button.setText("组六");
		zu3Button.setBackgroundResource(R.drawable.zu_button_b);
		zu6Button.setBackgroundResource(R.drawable.zu_button);
		zu3Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zu3Button.setBackgroundResource(R.drawable.zu_button_b);
				zu6Button.setBackgroundResource(R.drawable.zu_button);
				topLinearTwo.setVisibility(LinearLayout.VISIBLE);
				create_ZU3();
			}
		});

		zu6Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zu3Button.setBackgroundResource(R.drawable.zu_button);
				zu6Button.setBackgroundResource(R.drawable.zu_button_b);
				topLinearTwo.setVisibility(LinearLayout.GONE);
                create_ZU6();
			}
		});

	}
	/**
	 * 初始化单选按钮组
	 */
	public void initRadioGroup(){
		topButton = (RadioGroup) findViewById(R.id.buy_zixuan_radiogroup_top);
		for(int i=0;i<topTitle.length;i++){
			RadioButton radio = new RadioButton(this);
			radio.setText(topTitle[i]);
			radio.setTextColor(Color.BLACK);
			radio.setTextSize(13);
			radio.setId(i);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setPadding(Constants.PADDING, 0, 6, 0);
			topButton.addView(radio);
		}
		topButton.setOnCheckedChangeListener(this);
		topButton.check(0);
	}
	/**
	 * 组三
	 */
	public void create_ZU3(){
		iCurrentButton=PublicConst.BUY_PL3_ZU3;
        topButton.check(0);
    	isDanFu = true;
		create_ZU3_DAN();
	}
	/**
	 * 组三单式
	 */
	public void create_ZU3_DAN(){
		areaInfos = new AreaInfo[3];
		LinearLayout thirdLinear = (LinearLayout)findViewById(R.id.buy_zixuan_linear_third);
		thirdLinear.setVisibility(LinearLayout.VISIBLE);
        String baiTitle = getResources().getString(R.string.fc3d_text_bai_title).toString();
        String shiTitle = getResources().getString(R.string.fc3d_text_shi_title).toString();
        String geTitle = getResources().getString(R.string.fc3d_text_ge_title).toString();
		areaInfos[0] = new AreaInfo(10, 1, ballResId, 0, 0,Color.RED,baiTitle);
		areaInfos[1] = new AreaInfo(10, 1, ballResId, 0, 0,Color.RED,shiTitle);
		areaInfos[2] = new AreaInfo(10, 1, ballResId, 0, 0,Color.RED,geTitle);
		createView(areaInfos, fc3dCode,this,false);
		oneBallTable = areaNums[0].table;
		twoBallTable = areaNums[1].table;
		thirdBallTable = areaNums[2].table;
	}
	/**
	 * 组三复式
	 * 
	 */
	public void create_ZU3_FU(){
		areaInfos = new AreaInfo[1];
        String title = getResources().getString(R.string.fc3d_text_hezhi_title).toString();
		areaInfos[0] = new AreaInfo(10, 10, ballResId, 0, 0,Color.RED,title);
		createView(areaInfos, fc3dCode,this,false);
		oneBallTable = areaNums[0].table;
	}
	/**
	 *  组六
	 */
	public void create_ZU6(){
		areaInfos = new AreaInfo[1];
		iCurrentButton=PublicConst.BUY_PL3_ZU6;
        String title = getResources().getString(R.string.fc3d_text_hezhi_title).toString();
		areaInfos[0] = new AreaInfo(10, 9, ballResId, 0, 0,Color.RED,title);
		createView(areaInfos, fc3dCode,this,false);
		oneBallTable = areaNums[0].table;
	}
	/**
	 * 重写RadioGroup监听方法onCheckedChanged
	 * 
	 * @param RadioGroup
	 *            RadioGroup
	 * @param int checkedId 当前被选择的RadioId
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (group.getId()) {
		case R.id.buy_zixuan_radiogroup_top:
			switch (checkedId) {
			case 0:// 单式
			    isDanFu = true;
			    create_ZU3_DAN();
				break;
			case 1:// 复式
				isDanFu = false;
				 create_ZU3_FU();
				break;
			}
		}
	}
	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId){
		int nBallId = 0; 
		for(int i=0;i<areaNums.length;i++){
			nBallId = iBallId;
			iBallId = iBallId - areaNums[i].info.areaNum;
			if(iBallId<0){
				if(iCurrentButton==PublicConst.BUY_PL3_ZU3&&isDanFu==true){
					if(i==0||i==1){
						 areaNums[0].table.clearAllHighlights();
						 areaNums[1].table.clearAllHighlights();
						 areaNums[1].table.changeBallState(areaNums[0].info.chosenBallSum, nBallId);
						 int isHighLight = areaNums[0].table.changeBallState(areaNums[0].info.chosenBallSum, nBallId);
						 if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& areaNums[2].table.getOneBallStatue(nBallId) !=0) {
							areaNums[2].table.clearOnBallHighlight(nBallId);
						 }
					}else{
						areaNums[2].table.clearAllHighlights();
						int isHighLight = areaNums[2].table.changeBallState(areaNums[2].info.chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& areaNums[0].table.getOneBallStatue(nBallId) !=0) {
							areaNums[0].table.clearOnBallHighlight(nBallId);
							areaNums[1].table.clearOnBallHighlight(nBallId);
						}
					}
				}else{
					  areaNums[i].table.changeBallState(areaNums[i].info.chosenBallSum, nBallId);
				}
				  break;
			}

	     }

	}
	/**
	 * 判断是否满足投注条件
	 */
	public void isTouzhu() {
		// TODO Auto-generated method stub
		// 组3
		if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {

			int iZhuShu = 0;
            if (isDanFu) {
					int baiweiNums = oneBallTable.getHighlightBallNums();
					int shiweiNums = twoBallTable.getHighlightBallNums();
					int geweiNums = thirdBallTable.getHighlightBallNums();

					if (baiweiNums < 1 || shiweiNums < 1 || geweiNums < 1) {
						alertInfo("请再百位，十位， 个位中均选择一个小球后再投注");
					} else if (baiweiNums == 1 && shiweiNums == 1&& geweiNums == 1) {
						iZhuShu = 1;
						String baiweistr = (oneBallTable.getHighlightBallNOs()[0])+"";// 前2位相同
						String geweistr = (thirdBallTable.getHighlightBallNOs()[0])+"";// 前2位相同
						if (iZhuShu * 2 > 100000) {
							dialogExcessive();
						} else {
							setZhuShu(iZhuShu);
							alert( "注码：\n" + baiweistr + "," + baiweistr + ","
									+ geweistr );
						}
					}
			} else{
				if (oneBallTable.getHighlightBallNums() < 2) {
					alertInfo("请至少选择2个小球后再投注");
				} else {
					// wangyl 7.12 修改确定投注成功后的对话框
					int[] fushiNums = oneBallTable.getHighlightBallNOs();
					String fushiStr = "";
					for (int i = 0; i < fushiNums.length; i++) {
						fushiStr += (fushiNums[i]) + ",";
						if (i == fushiNums.length - 1) {
							fushiStr = fushiStr.substring(0, fushiStr
									.length() - 1);
						}
					}
					iZhuShu = getZhuShu();

					if (iZhuShu * 2 > 100000) {
						dialogExcessive();
					} else {
						setZhuShu(iZhuShu);
						alert("注码：" + fushiStr  );
					}
				}
			}
		}
		// 福彩3D组6
		if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {

			if (oneBallTable.getHighlightBallNums() < 3) {
				alertInfo("请至少选择3个小球后再投注");
			} else {
				int[] fushiNums = oneBallTable.getHighlightBallNOs();
				String fushiStr = "";
				for (int i = 0; i < fushiNums.length; i++) {
					fushiStr += PublicMethod.getZhuMa(fushiNums[i]) + ",";
					if (i == fushiNums.length - 1) {
						fushiStr = fushiStr.substring(0,fushiStr.length() - 1);
					}
				}
				int iZhuShu = getZhuShu();

				if (iZhuShu * 2 > 100000) {
					dialogExcessive();
				} else {
					setZhuShu(iZhuShu);
					alert("注码：\n" + fushiStr  );
				}
			}

		}
	}

	/**
	 * 点击小球提示金额
	 * 
	 * @param areaNum
	 * @param iProgressBeishu
	 * @return
	 */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String mTextSumMoney = "";
		// 福彩3D组3
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_ZU3:
			if(isDanFu){
				if (oneBallTable.getHighlightBallNums() == 1&& twoBallTable.getHighlightBallNums() == 1&& thirdBallTable.getHighlightBallNums() == 1) {
					int iZhuShu = iProgressBeishu;
					mTextSumMoney = "共" + iZhuShu + "注，共"+ (iZhuShu * 2) + "元";
				}else if(oneBallTable.getHighlightBallNums() == 0){
					mTextSumMoney = "百位还需要1个球";
				}else if(thirdBallTable.getHighlightBallNums() == 0){
					mTextSumMoney = "个位还需要1个球";
				}
			}else{	
				if (oneBallTable.getHighlightBallNums() > 1) {
					int iZhuShu = getZhuShu() ;
					mTextSumMoney = "共" + iZhuShu + "注，共"+ (iZhuShu * 2) + "元";
				}else{
					int num = 2 - oneBallTable.getHighlightBallNums();
					mTextSumMoney = "还需要"+num+"个球";
				}
			}

			break;
		// 福彩3D组6
		case PublicConst.BUY_PL3_ZU6:
			if (oneBallTable.getHighlightBallNums() > 2) {
				int iZhuShu = getZhuShu();
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
 		case PublicConst.BUY_PL3_ZU3:
 			if (isDanFu) {
				iReturnValue = 1;
			}else {// 组3 复式注数的获取 20100912 陈晨
				int iZu3Balls = oneBallTable.getHighlightBallNums();
				iReturnValue = (int) getPL3Zu3FushiZhushu(iZu3Balls);
			}
 			break;
 		// 福彩3D组6
 		case PublicConst.BUY_PL3_ZU6:
			int iZu6Balls = oneBallTable.getHighlightBallNums();
			iReturnValue = (int) getPL3Zu6FushiZhushu(iZu6Balls);
 			break;
 		}
		return iReturnValue* iProgressBeishu;
    	 
     }

 	/**
 	 * 获得排列三组6复式注数
 	 * 
 	 * @param iZu6balls
 	 *            选择小球个数
 	 * @return 返回注数
 	 */
 	private long getPL3Zu6FushiZhushu(int iZu6balls) {
 		long tempzhushu = 0l;
 		if (iZu6balls > 0) {
 			tempzhushu += PublicMethod.zuhe(3, iZu6balls);
 		}
 		return tempzhushu;

 	}

 	/**
 	 * 获得排列三组3复式注数
 	 * 
 	 * @param iZu3balls
 	 *            选择小球个数
 	 * @return 返回注数
 	 */
 	private long getPL3Zu3FushiZhushu(int iZu3balls) {
 		long tempzhushu = 0l;
 		if (iZu3balls > 0) {
 			tempzhushu += PublicMethod.zuhe(2, iZu3balls) * 2;
 		}
 		return tempzhushu;

 	}
	/**
	 * 投注联网
	 */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_PL3);
	}

}
