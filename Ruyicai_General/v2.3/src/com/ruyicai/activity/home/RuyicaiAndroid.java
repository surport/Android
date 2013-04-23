package com.ruyicai.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.AccountRecharge;
import com.ruyicai.activity.common.RuyiHelper;
import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.activity.notice.NoticePrizesOfLottery;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;


public class RuyicaiAndroid extends ScrollableTabActivity {
	int stringId[] = { R.string.goucaitouzhu, R.string.kaijianggonggao, R.string.zhuanjiafenxi, R.string.gengduo };
	
	//未选中状态图片
	int buttomPic[] = { R.drawable.buy, R.drawable.kaijingxinxi, R.drawable.account, R.drawable.more };
	
	//选中状态图片
	int buttomPicChecked[] = { R.drawable.buy_b, R.drawable.kaijiangxinxi,	R.drawable.account_b, R.drawable.more2 };
	boolean lucy=false;
//	public  boolean ACCOUNTRECHARGE_EIXT_TYPE=false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		RuyicaiActivityManager.getInstance().addActivity(this);
		initNum();
		super.onCreate(savedInstanceState);
		// 周黎鸣 7.4 代码修改：添加登陆的判断
		//this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		boolean lucy = false;
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
		    lucy = bundle.getBoolean("ischooselucy");// 读出数据
		}
		
		for (int i = 0; i < 4; i++) {
			Intent intent = null;
			switch (i) {
			case 0:
				intent = new Intent(this, BuyLotteryMainList.class);
				break;
			case 1:
				
				intent = new Intent(this, NoticePrizesOfLottery.class);
				break;
			case 2:
				intent = new Intent(this, AccountRecharge.class);
				break;
			case 3:
				
//				if(lucy){
//					intent = new Intent(this,ChooseLuckyNum.class);	
//				}else{
					intent = new Intent(this, RuyiHelper.class);
//				}
//				lucy = false;
				break;
			default:
				break;
			}
			
			String sss = getResources().getString(stringId[i]);
			addTab(sss, buttomPic[i], buttomPicChecked[i], intent);
		}

		//跳转到哪个界面
       if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			int index = bundle.getInt("index");// 读出数据
		//	ACCOUNTRECHARGE_EIXT_TYPE=bundle.getBoolean("ACCOUNTRECHARGE_EIXT_TYPE");
			commit(index);		
		} else {
			commit(0);
		}
	}

	// 根据屏幕初始化一些数据
	protected void initNum() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
		Constants.SCREEN_DENSITYDPI = metric.densityDpi;
		Log.e("Constants.SCREEN_DENSITYDPI", Constants.SCREEN_DENSITYDPI+"");
		Constants.SCREEN_DENSITY = metric.density;
		Constants.SCREEN_WIDTH = getWindowManager().getDefaultDisplay().getWidth();// 设置屏幕宽度
		Constants.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
	 if (Constants.SCREEN_WIDTH == 320) {
			PublicConst.IMG_WITH = 95;
		} else {
			PublicConst.IMG_WITH = 135;
		}
	}
}