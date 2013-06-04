package com.ruyicai.activity.buy;

import com.palmdream.RuyicaiAndroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


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
}
