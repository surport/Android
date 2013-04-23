package com.ruyicai.activity.common;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.dialog.ExitDialogFactory;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseLuckyNum extends RuyiHelper{
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	//	iQuitFlag=90;
		showListView(ID_XINGYUNXUANHAO);
	}
    
	protected void showCLLNMainListView(){
		super.showCLLNMainListView();
		
		ImageView tvreturn = (ImageView) findViewById(R.id.tv_choose_luck_lottery_num_return);
		tvreturn.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				ChooseLuckyNum.this.finish();
			}
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
			/*
			 * if(iCallOnKeyDownFlag==false){ iCallOnKeyDownFlag=true;}
			 */
		if(keyCode==KeyEvent.KEYCODE_BACK){
			ChooseLuckyNum.this.finish();
		}
		
		
		
    return super.onKeyDown(keyCode, event);
	}
}
