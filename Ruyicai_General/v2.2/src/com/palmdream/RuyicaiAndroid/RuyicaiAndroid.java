package com.palmdream.RuyicaiAndroid;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;

public class RuyicaiAndroid extends ScrollableTabActivity {
	int stringId[] = { R.string.goucaitouzhu, R.string.kaijianggonggao,
			R.string.zhanghuchongzhi, R.string.zhuanjiafenxi, R.string.gengduo };
	int drawableId[] = { R.drawable.goucai, R.drawable.kaijiang,
			R.drawable.zhanghu, R.drawable.zhuanjia, R.drawable.gengduo };
	int drawableId_on[] = { R.drawable.goucai_b, R.drawable.kaijiang_b,
	R.drawable.zhanghu_b, R.drawable.zhuanjia_b, R.drawable.gengduo_b };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 周黎鸣 7.4 代码修改：添加登陆的判断
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
				RuyicaiAndroid.this, "addInfo");
		// pre.setUserLoginInfo("sessionid", "");
		for (int i = 0; i < 5; i++) {
			Intent intent = null;

			switch (i) {
			
			case 0:
			//	Bundle bundle=new Bundle();
			  //  bundle.putString("type_pre", "主页面");
				
			    intent = new Intent(this, BuyLotteryMainList.class);
			//    intent.putExtras(bundle);
				// intent = new Intent(this, BuyLotteryMainList.class);
				// intent = new Intent(this, UserLogin.class);
				break;
			case 1:
			//	Bundle bundle1=new Bundle();
			//    bundle1.putString("type_pre", "开奖");
				intent = new Intent(this, NoticePrizesOfLottery.class);
			//	intent.putExtras(bundle1);
				// intent = new Intent(this, NoticePrizesOfLottery.class);
				// intent = new Intent(this, UserRegister.class);
				break;
			case 2:
              //  Bundle bd3=new Bundle();
              //  bd3.putString("type_pre", "充值");
				intent = new Intent(this, AccountRecharge.class);
			//	intent.putExtras(bd3);
				break;
			case 3:
              //  Bundle bd4=new Bundle();
              //  bd4.putString("type_pre", "荐号");
				intent = new Intent(this, ExpertAnalyze.class);
				//intent.putExtras(bd4);
				// intent = new Intent(this, RuyiExpressFeel.class);
				// intent = new Intent(this, RuYiChuanQingMainPage.class);
				// intent = new Intent(this, UserRegister.class);
				break;
			case 4:
             //   Bundle bd5=new Bundle();
              //  bd5.putString("type_pre", "更多");
				intent = new Intent(this, RuyiHelper.class);
			//	intent.putExtras(bd5);
			default:
				break;
			}
			/*
			 * This adds a title and an image to the tab bar button Image should
			 * be a PNG file with transparent background. Shades are opaque
			 * areas in on and off state are specific as parameters
			 */
			// this.addTab("title"+i, R.drawable.star_big_on,
			// RadioStateDrawable.SHADE_GRAY,
			// RadioStateDrawable.SHADE_GREEN,intent);
			String sss = getResources().getString(stringId[i]);
			// PublicMethod.myOutput("-----"+i+":"+sss);
			// this.addTab(sss, drawableId[0], RadioStateDrawable.SHADE_GRAY,
			// RadioStateDrawable.SHADE_GREEN,intent);
			// this.addTab(sss, drawableId[i], RadioStateDrawable.SHADE_BLACK,
			// RadioStateDrawable.SHADE_BLUE, intent);
			this.addTab(sss, drawableId[i], drawableId_on[i], intent);
		}
		/*
		 * { Intent intent = null; intent = new Intent(this, UserLogin.class);
		 * this.addMyIntent(intent); }
		 */
		/*
		 * commit is required to redraw the bar after add tabs are added if you
		 * know of a better way, drop me your suggestion please.
		 */
		if (getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			int index = bundle.getInt("index");// 读出数据
			AccountRecharge.flag = bundle.getBoolean("flag");// 读出数据
			commit(index);
		} else {
			commit(0);
		}

	}

}