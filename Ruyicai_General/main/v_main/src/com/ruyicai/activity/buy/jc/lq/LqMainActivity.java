package com.ruyicai.activity.buy.jc.lq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.buy.jc.lq.view.DxView;
import com.ruyicai.activity.buy.jc.lq.view.HunHeLqView;
import com.ruyicai.activity.buy.jc.lq.view.RfView;
import com.ruyicai.activity.buy.jc.lq.view.SfView;
import com.ruyicai.activity.buy.jc.lq.view.SfcView;
import com.ruyicai.activity.buy.jc.score.lq.JcLqScoreActivity;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreActivity;
import com.ruyicai.activity.buy.jc.zq.view.BFView;
import com.ruyicai.activity.buy.jc.zq.view.BQCView;
import com.ruyicai.activity.buy.jc.zq.view.HunHeZqView;
import com.ruyicai.activity.buy.jc.zq.view.SPfView;
import com.ruyicai.activity.buy.jc.zq.view.ZJQView;
import com.ruyicai.activity.notice.NoticeJcActivity;
import com.ruyicai.activity.notice.NoticeJclActivity;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

public class LqMainActivity extends JcMainActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setType(Constants.JCBASKET);
		createView(SF, isDanguan);
		setLotNo(Constants.LOTNO_JCL);
		setTitle(false);
		setScoreBtn();
		isTeamBtn(true);
		MobclickAgent.onEvent(this, "jingcailanqiu"); // BY贺思明 点击首页的“竞彩篮球”图标
	}

	/**
	 * 根据玩法按钮创建界面
	 */
	protected void createView(int type, boolean isdanguan) {
		switch (type) {
		case SF:
			lqMainView = new SfView(this, betAndGift, new Handler(),
					layoutView, this.type, isdanguan, checkTeam);
			TYPE = SF;
			break;
		case RF_SF:
			lqMainView = new RfView(this, betAndGift, new Handler(),
					layoutView, this.type, isdanguan, checkTeam);
			TYPE = RF_SF;
			break;
		case SFC:
			lqMainView = new SfcView(this, betAndGift, new Handler(),
					layoutView, this.type, isdanguan, checkTeam);
			TYPE = SFC;
			break;
		case DXF:
			lqMainView = new DxView(this, betAndGift, new Handler(),
					layoutView, this.type, isdanguan, checkTeam);
			TYPE = DXF;
			break;
		case HUN_HE:
			lqMainView = new HunHeLqView(this, betAndGift, new Handler(),
					layoutView, this.type, isdanguan, checkTeam);
			TYPE = HUN_HE;
			break;
		}
		lqMainView.initTeamNum(textTeamNum);
		textTitle.setText(lqMainView.getTitle());

	}

	public void turnHosity() {
		Intent intent = new Intent(context, NoticeJclActivity.class);
		startActivity(intent);
	}

	public void setScoreBtn() {
		Button btnScore = (Button) findViewById(R.id.buy_lq_main_btn_score);
		btnScore.setVisibility(Button.VISIBLE);
		btnScore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, JcLqScoreActivity.class);
				startActivity(intent);
			}
		});
	}

}
