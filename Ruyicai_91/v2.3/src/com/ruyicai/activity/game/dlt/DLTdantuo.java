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
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class DLTdantuo extends DLT implements OnFocusChangeListener{

	/* 胆托布局中的元素 */
	private TextView red_dan_text;// 前区胆码的TextView
	private TextView red_tuo_text;// 前区托码的TextView
	private TextView blue_dan_text;// 后区胆码的TextView
	private TextView blue_tuo_text;// 后区托码的TextView

	private TableLayout red_dan;// 前区胆码的小球区
	private TableLayout red_tuo;// 前区托码的小球区
	private TableLayout blue_dan;// 后区胆码的小球区
	private TableLayout blue_tuo;// 后区托码的小球区
	@Override
	public void onCreate(Bundle savedInstanceState){
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		lottery_title.setText(getString(R.string.dlt_dantuo));	
		goucaidating_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent goucai = new Intent(DLTdantuo.this,RuyicaiAndroid.class);
				startActivity(goucai);
				DLTdantuo.this.finish();
			}});
		dltzhixuan_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent zhixuan = new Intent(DLTdantuo.this,DLTzhixuan.class);
				startActivity(zhixuan);
				DLTdantuo.this.finish();
			}});
		dltdantuo_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Toast.makeText(DLTdantuo.this, R.string.dlt_dantuo_tishi, Toast.LENGTH_SHORT).show();
			}});
		dlt12xuan2_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent twoxuan2 = new Intent(DLTdantuo.this,DLT12xuan2.class);
				startActivity(twoxuan2);	
				DLTdantuo.this.finish();
			}});
		init();
	}

	private void init() {
		iCurrentButton = PublicConst.BUY_DLT_DANTUO;
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_dlt_dantuo, null);
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		red_dan_text = (TextView) iV.findViewById(R.id.dlt_new_front_dan_text);
		red_tuo_text = (TextView) iV.findViewById(R.id.dlt_new_front_tuo_text);
		blue_dan_text = (TextView) iV.findViewById(R.id.dlt_new_rear_dan_text);
		blue_tuo_text = (TextView) iV.findViewById(R.id.dlt_new_rear_tuo_text);
		qian_dan = (EditText) iV.findViewById(R.id.dlt_dantou_front_dan_edit_text);
		qian_tuo = (EditText) iV.findViewById(R.id.dlt_dantou_front_tuo_edit_text);
		hou_dan = (EditText) iV.findViewById(R.id.dlt_dantou_rear_dan_edit_text);
		hou_tuo = (EditText) iV.findViewById(R.id.dlt_dantou_rear_tuo_edit_text);
		red_dan = (TableLayout) iV.findViewById(R.id.table_front_danma);
		red_tuo = (TableLayout) iV.findViewById(R.id.table_front_tuoma);
		blue_dan = (TableLayout) iV.findViewById(R.id.table_rear_danma);
		blue_tuo = (TableLayout) iV.findViewById(R.id.table_rear_tuoma);
		qian_dan.setOnFocusChangeListener(this);/* 注册事件 */
		qian_tuo.setOnFocusChangeListener(this);
		hou_dan.setOnFocusChangeListener(this);
		hou_tuo.setOnFocusChangeListener(this);
		
		PublicMethod.recycleBallTable(redBallTable);
		redBallTable = makeBallTable(iV, R.id.table_front_danma, iScreenWidth, redBallViewNum, redBallResId, RED_BALL_START);
		PublicMethod.recycleBallTable(redTuoBallTable);
		redTuoBallTable = makeBallTable(iV, R.id.table_front_tuoma,iScreenWidth, redBallViewNum, redBallResId,	RED_TUO_BALL_START);
		PublicMethod.recycleBallTable(blueBallTable);
		blueBallTable = makeBallTable(iV, R.id.table_rear_danma, iScreenWidth,blueBallViewNum, blueBallResId,	BLUE_BALL_START);
		PublicMethod.recycleBallTable(blueTuoBallTable);
		blueTuoBallTable = makeBallTable(iV, R.id.table_rear_tuoma,iScreenWidth, blueBallViewNum, blueBallResId, BLUE_TUO_BALL_START);
		
		mTextSumMoney = (TextView) iV.findViewById(R.id.dlt_dantuo_text_sum_money);
		mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));

		dlt_btn_newSelect = (ImageButton)iV.findViewById(R.id.dlt_dantuo_btn_newSelect);
		dlt_btn_newSelect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			      beginReselect();
			}
		});
		mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.dlt_dantuo_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);

		mSeekBarQishu = (SeekBar) iV.findViewById(R.id.dlt_dantuo_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);
		setSeekWhenAddOrSub(R.id.dlt_dantuo_seekbar_subtract_beishu, iV, -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.dlt_dantuo_seekbar_add_beishu, iV, 1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.dlt_dantuo_seekbar_subtract_qihao, iV, -1,mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.dlt_dantuo_seekbar_add_qihao, iV, 1,mSeekBarQishu, false);
		zhuijiatouzhu = (ToggleButton)iV.findViewById(R.id.dlt_dantuo_zhuijia_touzhu_cb);
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				mTimesMoney=isChecked?3:2;
				changeTextSumMoney();
			}
		});
		mTextBeishu = (TextView) iV.findViewById(R.id.dlt_dantuo_text_beishu_change);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView) iV.findViewById(R.id.dlt_dantuo_text_qishu_change);
		mTextQishu.setText("" + iProgressQishu);
		dlt_b_touzhu_dantuo = (ImageButton) iV.findViewById(R.id.dlt_dantuo_b_touzhu);
		dlt_b_touzhu_dantuo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView.getLayoutParams().width, buyView.getLayoutParams().height));
	}
    /**
     * 四个文本框被选中的触发事件*/
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.dlt_dantou_front_dan_edit_text:
			if (hasFocus) {
				qian_dan.setBackgroundResource(R.drawable.hongkuang);
				red_dan.setVisibility(LinearLayout.VISIBLE);
				red_tuo.setVisibility(LinearLayout.GONE);
				blue_dan.setVisibility(LinearLayout.GONE);
				blue_tuo.setVisibility(LinearLayout.GONE);
				zhuijiatouzhu.setVisibility(ToggleButton.GONE);
			} else {
				qian_dan.setBackgroundResource(R.drawable.huikuang);
				red_dan_text.setTextColor(Color.BLACK);
			}
			break;
		case R.id.dlt_dantou_front_tuo_edit_text:
			if (hasFocus) {
				qian_tuo.setBackgroundResource(R.drawable.hongkuang);
				red_dan.setVisibility(LinearLayout.GONE);
				red_tuo.setVisibility(LinearLayout.VISIBLE);
				blue_dan.setVisibility(LinearLayout.GONE);
				blue_tuo.setVisibility(LinearLayout.GONE);
				zhuijiatouzhu.setVisibility(ToggleButton.GONE);
			} else {
				qian_tuo.setBackgroundResource(R.drawable.huikuang);
				red_tuo_text.setTextColor(Color.BLACK);
			}
			break;
		case R.id.dlt_dantou_rear_dan_edit_text:
			if (hasFocus) {
				hou_dan.setBackgroundResource(R.drawable.lankuang);
				red_dan.setVisibility(LinearLayout.GONE);
				red_tuo.setVisibility(LinearLayout.GONE);
				blue_dan.setVisibility(LinearLayout.VISIBLE);
				blue_tuo.setVisibility(LinearLayout.GONE);
				zhuijiatouzhu.setVisibility(ToggleButton.VISIBLE);
			} else {
				hou_dan.setBackgroundResource(R.drawable.huikuang);
				blue_dan_text.setTextColor(Color.BLACK);
			}
			break;
		case R.id.dlt_dantou_rear_tuo_edit_text:
			if (hasFocus) {
				hou_tuo.setBackgroundResource(R.drawable.lankuang);
				red_dan.setVisibility(LinearLayout.GONE);
				red_tuo.setVisibility(LinearLayout.GONE);
				blue_dan.setVisibility(LinearLayout.GONE);
				blue_tuo.setVisibility(LinearLayout.VISIBLE);
				zhuijiatouzhu.setVisibility(ToggleButton.VISIBLE);
			} else {
				hou_tuo.setBackgroundResource(R.drawable.huikuang);
				blue_tuo_text.setTextColor(Color.BLACK);
			}
			break;
		}
	}
	
	protected void onDestroy() {
		super.onDestroy();
		PublicMethod.recycleBallTable(redBallTable);
		PublicMethod.recycleBallTable(redTuoBallTable);
		PublicMethod.recycleBallTable(blueBallTable);
		PublicMethod.recycleBallTable(blueTuoBallTable);
	}
	
}
