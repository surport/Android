package com.ruyicai.activity.buy;

import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.SensorActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.EditText;

public abstract class BaseActivity extends Activity implements OnClickListener{
	public abstract void isBallTable(int iBallId);
   	public abstract void showEditText();
   	public abstract void changeTextSumMoney();
   	public abstract void again();
	public AreaNum areaNums[];
   	protected int newPosition;
   	protected EditText editZhuma;
   	private BallSensor  sensor;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensor = new BallSensor(this);
	}
	public int getNewPosition() {
		return newPosition;
	}
	public void setNewPosition(int newPosition) {
		this.newPosition = newPosition;
	}
	public void again(int position){
		
	}
	public void setJiXuanEdit(){
		if(editZhuma!=null){
			editZhuma.setTextColor(Color.RED);
			editZhuma.setText("摇一摇可以机选一注");
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//BY贺思明 2012-7-24
		if(areaNums != null){
			for(int i=0;i<areaNums.length;i++){
				if(areaNums[i].jixuanBtn!=null){
					 sensor.stopAction();                          
				}
			}
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-24
		if(areaNums != null){
			for(int i=0;i<areaNums.length;i++){
				if(areaNums[i].jixuanBtn!=null){
                    sensor.startAction();                          
					setJiXuanEdit();
				}
			}
		}
		
	}
	/**
	 * 实现震动的类
	 * @author Administrator
	 *
	 */
	public class BallSensor extends SensorActivity {

		public BallSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
			if(areaNums != null){
				for(int i=0;i<areaNums.length;i++){
					if(areaNums[i].jixuanBtn!=null){
						areaNums[i].jixuanBtn.dialogOnclick();
					}
				}
			}
		}
	}
}
