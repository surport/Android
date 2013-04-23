package com.ruyicai.activity.game.dlt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class DLTzhixuan extends DLT implements OnFocusChangeListener {
	private TextView qiantext;
	private TextView houtext;
	ImageButton dlt_b_touzhu_fushi;
	ImageButton dlt_btn_newSelect;
	private TableLayout redBalllayout;
	private TableLayout blueBalllayout;

	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		lottery_title.setText(getString(R.string.dlt_zhixuan));
		dltzhixuan_button.setBackgroundResource(R.drawable.dlt_downbutton_turnrandom);
		goucaidating_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent goucai = new Intent(DLTzhixuan.this,	RuyicaiAndroid.class);
				startActivity(goucai);
				DLTzhixuan.this.finish();
			}
		});
		dltzhixuan_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent zhixuan = new Intent(DLTzhixuan.this,DLTzhixuanjixuan.class);
				startActivity(zhixuan);
				DLTzhixuan.this.finish();
			}
		});
		dltdantuo_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent dantuo = new Intent(DLTzhixuan.this, DLTdantuo.class);
				startActivity(dantuo);
				DLTzhixuan.this.finish();
			}
		});
		dlt12xuan2_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent twoxuan2 = new Intent(DLTzhixuan.this, DLT12xuan2.class);
				startActivity(twoxuan2);
				DLTzhixuan.this.finish();
			}
		});
		init();
	}

	private void init() {
		iCurrentButton = PublicConst.BUY_DLT_ZHIXUAN;
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_dlt_zhixuan, null);

		qianquedit = (EditText) iV.findViewById(R.id.dlt_zhixuan_front_edit_text);
		houquedit = (EditText) iV.findViewById(R.id.dlt_zhixuan_rear_edit_text);
		mTextSumMoney = (TextView) iV.findViewById(R.id.dlt_zhixuan_text_sum_money);
		mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.dlt_zhixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) iV.findViewById(R.id.dlt_zhixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);
		qiantext = (TextView) iV.findViewById(R.id.dlt_zhixuan_front_text);
		houtext = (TextView) iV.findViewById(R.id.dlt_zhixuan_rear_text);
		redBalllayout = (TableLayout) iV.findViewById(R.id.table_red_zhixuan);
		blueBalllayout = (TableLayout) iV.findViewById(R.id.table_blue_zhixuan);
		
		PublicMethod.recycleBallTable(redBallTable);
		redBallTable = makeBallTable(iV, R.id.table_red_zhixuan, iScreenWidth,redBallViewNum, redBallResId, RED_BALL_START);
		
		PublicMethod.recycleBallTable(blueBallTable);
		blueBallTable = makeBallTable(iV, R.id.table_blue_zhixuan,iScreenWidth, blueBallViewNum, blueBallResId, BLUE_BALL_START);
		dlt_btn_newSelect = (ImageButton) iV.findViewById(R.id.dlt_zhixuan_btn_newSelect);
		dlt_btn_newSelect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginReselect();
			}
		});
		mTextBeishu = (TextView) iV.findViewById(R.id.dlt_zhixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView) iV.findViewById(R.id.dlt_zhixuan_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		setSeekWhenAddOrSub(R.id.dlt_zhixuan_seekbar_subtract_beishu, iV, -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.dlt_zhixuan_seekbar_add_beishu, iV, 1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.dlt_zhixuan_seekbar_subtract_qihao, iV, -1,mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.dlt_zhixuan_seekbar_add_qishu, iV, 1,mSeekBarQishu, false);

		qianquedit.setOnFocusChangeListener(this);
		houquedit.setOnFocusChangeListener(this);
		zhuijiatouzhu = (ToggleButton) iV.findViewById(R.id.dlt_zhixuan_zhuijia_touzhu_cb);
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mTimesMoney = isChecked ? 3 : 2;
				changeTextSumMoney();
			}
		});
		dlt_b_touzhu_fushi = (ImageButton) iV.findViewById(R.id.dlt_zhixuan_b_touzhu);
		dlt_b_touzhu_fushi.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView.getLayoutParams().width, buyView.getLayoutParams().height));
	}

	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.dlt_zhixuan_front_edit_text:
			if (hasFocus) {
				qiantext.setTextColor(Color.RED);
				qianquedit.setBackgroundResource(R.drawable.hongkuang);
				blueBalllayout.setVisibility(TableLayout.GONE);
				redBalllayout.setVisibility(TableLayout.VISIBLE);
				zhuijiatouzhu.setVisibility(ToggleButton.INVISIBLE);
			} else {
				qianquedit.setBackgroundResource(R.drawable.huikuang);
				qiantext.setTextColor(Color.BLACK);
				zhuijiatouzhu.setVisibility(ToggleButton.VISIBLE);
			}
			break;
		case R.id.dlt_zhixuan_rear_edit_text:
			if (hasFocus) {
				houtext.setTextColor(Color.BLUE);
				houquedit.setBackgroundResource(R.drawable.lankuang);
				blueBalllayout.setVisibility(TableLayout.VISIBLE);
				redBalllayout.setVisibility(TableLayout.GONE);
				zhuijiatouzhu.setVisibility(ToggleButton.VISIBLE);
			} else {
				houquedit.setBackgroundResource(R.drawable.huikuang);
				houtext.setTextColor(Color.BLACK);
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PublicMethod.recycleBallTable(redBallTable);
		PublicMethod.recycleBallTable(redTuoBallTable);
		PublicMethod.recycleBallTable(blueBallTable);
		PublicMethod.recycleBallTable(blueTuoBallTable);
	}

}
