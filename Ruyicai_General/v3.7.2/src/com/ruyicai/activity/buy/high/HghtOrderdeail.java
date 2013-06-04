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
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.constant.Constants;

public class HghtOrderdeail extends BuyActivityGroup {
	private String[] titles = { "投注", "追号", "收益追号" };
	private String[] topTitles = { "投注确认", "追号设置", "收益追号" };
	private Class[] allId = { ZiXuanTouZhu.class, ZixuanZhuihao.class,
			High_Frequencyrevenue_Recovery.class };
	private String[] titles2 = { "投注", "追号" };
	private String[] topTitles2 = { "投注确认", "追号设置" };
	private Class[] allId2 = { ZiXuanTouZhu.class, ZixuanZhuihao.class };
	private AddView addview;
	private String lotnoString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		addview = app.getAddview();
		lotnoString	= app.getPojo().getLotno();
		isIssue(false);
		if (addview.getSize() <= 1 && !lotnoString.equals(Constants.LOTNO_NMK3)) {
			init(titles, topTitles, allId);
		} else {
			init(titles2, topTitles2, allId2);
		}

	}

	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);

	}

}
