package com.ruyicai.activity.game.dlt;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class DLT12xuan2 extends DLT{
	
	int blueBallViewNum = 12;/*12—°2–°«Ú«¯”Ú*/

	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		lottery_title.setText(getString(R.string.dlt_12xuan2));
		dlt12xuan2_button.setBackgroundResource(R.drawable.dlt_downbutton_turnrandom);
		goucaidating_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent goucai = new Intent(DLT12xuan2.this,RuyicaiAndroid.class);
				startActivity(goucai);
				DLT12xuan2.this.finish();
			}});
		dltzhixuan_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent zhixuan = new Intent(DLT12xuan2.this,DLTzhixuan.class);
				startActivity(zhixuan);
				DLT12xuan2.this.finish();
			}});
		dltdantuo_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent dantuo = new Intent(DLT12xuan2.this,DLTdantuo.class);
				startActivity(dantuo);		
				DLT12xuan2.this.finish();
			}
		});
		dlt12xuan2_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent twoxuan2 = new Intent(DLT12xuan2.this,DLT12xuan2jixuan.class);
				startActivity(twoxuan2);	
				DLT12xuan2.this.finish();
			}
		});
		init();
	}
	private void init(){		
		iCurrentButton = PublicConst.BUY_DLT_TWOIN12;
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_dlt_12xuan2, null);
		dlt12xuan2edit = (EditText)iV.findViewById(R.id.dlt_12xuan2_edit_text);
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		
		PublicMethod.recycleBallTable(blueBallTable);
		blueBallTable = makeBallTable(iV, R.id.table_blue, iScreenWidth,
				blueBallViewNum, redBallResId,BLUE_BALL_START);
		
		dlt_btn_newSelect = (ImageButton)iV.findViewById(R.id.dlt_12xuan2_btn_newselect);
		dlt_btn_newSelect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginReselect();
			}
		});
		mTextSumMoney = (TextView) iV.findViewById(R.id.dlt_twoin12_text_sum_money);
        mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.dlt_twoin12_seek_beishu);
        mSeekBarBeishu.setOnSeekBarChangeListener(this);
        mSeekBarBeishu.setProgress(iProgressBeishu);

        mSeekBarQishu = (SeekBar) iV.findViewById(R.id.dlt_twoin12_seek_qishu);
        mSeekBarQishu.setOnSeekBarChangeListener(this);
        mSeekBarQishu.setProgress(iProgressQishu);
        
        setSeekWhenAddOrSub(R.id.dlt_twoin12_seekbar_subtract_beishu, iV,-1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.dlt_twoin12_seekbar_add_beishu, iV, 1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.dlt_twoin12_seekbar_subtract_qihao, iV,-1, mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.dlt_twoin12_seekbar_add_qihao, iV, 1,mSeekBarQishu, false);

        mTextBeishu = (TextView) iV.findViewById(R.id.dlt_twoin12_text_beishu);
        mTextBeishu.setText("" + iProgressBeishu);
        mTextQishu = (TextView) iV.findViewById(R.id.dlt_twoin12_text_qishu);
        mTextQishu.setText("" + iProgressQishu);
     
        dlt_b_touzhu_twoin12 = (ImageButton) iV.findViewById(R.id.dlt_twoin12_b_touzhu);
        dlt_b_touzhu_twoin12.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		    beginTouZhu(); 
	      }
       });
        buyView.addView(iV, new LinearLayout.LayoutParams(buyView.getLayoutParams().width, buyView.getLayoutParams().height));
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
