package com.ruyicai.activity.buy;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;

public abstract class BaseActivity extends Activity implements OnClickListener{
	public abstract void isBallTable(int iBallId);
   	public abstract void showEditText();
   	public abstract void changeTextSumMoney();
   	public abstract void again();
   	protected int newPosition;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	public int getNewPosition() {
		return newPosition;
	}
	public void setNewPosition(int newPosition) {
		this.newPosition = newPosition;
	}
	public void again(int position){
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//BY贺思明 2012-7-24
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-24
	}
}
