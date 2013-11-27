package com.ruyicai.activity.buy.high;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.activity.buy.zixuan.ZiXuanTouZhu;
import com.ruyicai.activity.buy.zixuan.ZixuanZhuihao;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;

public class HghtOrderdeail extends BuyActivityGroup {
	private String[] titles = { "投注", "追号", "收益追号" };
	private String[] topTitles = { "投注确认", "追号设置", "收益追号" };
	private Class[] allId = { ZiXuanTouZhu.class, ZixuanZhuihao.class,
			High_Frequencyrevenue_Recovery.class };
	private String[] titles2 = { "投注", "追号" };
	private String[] topTitles2 = { "投注确认", "追号设置" };
	private Class[] allId2 = { ZiXuanTouZhu.class, ZixuanZhuihao.class };
	/**add by yejc 20130705 start*/
	private String[] titles3 = {"追号"};
	private String[] topTitles3 = {"追号设置"};
	private Class[] allId3 = {ZixuanZhuihao.class};
	/**add by yejc 20130705 start*/
	public static int fromInt;
	private AddView addview;
	private String lotnoString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		addview = app.getAddview();
		lotnoString = app.getPojo().getLotno();
		isIssue(false);
		fromInt = getIntent().getIntExtra("from", 0);
		/**add by yejc 20130705 start*/
		if (isFromTrackQuery) {
			init(titles3, topTitles3, allId3);
			/**add by yejc 20130705 end*/
		} else {
			if (addview.getSize() <= 1) {
				init(titles, topTitles, allId);
			} else {
				init(titles2, topTitles2, allId2);
			}
		}
	}

	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);

	}

}
