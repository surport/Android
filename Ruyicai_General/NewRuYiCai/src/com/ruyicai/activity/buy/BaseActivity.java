package com.ruyicai.activity.buy;

import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.ruyicai.util.SensorActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.EditText;

public abstract class BaseActivity extends Activity implements OnClickListener {
	public abstract void isBallTable(int iBallId);

	public abstract void showEditText();

	public abstract void changeTextSumMoney();
	public abstract void showBetInfo(String Text);
	
	public abstract void again();

	public AreaNum areaNums[];
	protected int newPosition;
	public EditText editZhuma;
	protected BallSensor baseSensor;
	private boolean isJixuan = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSensor = new BallSensor(this);
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		isJixuan = shellRW.getBooleanValue(ShellRWConstants.ISJIXUAN, true);
	}

	public Context getContext() {
		return this;
	}

	public int getNewPosition() {
		return newPosition;
	}

	public void setNewPosition(int newPosition) {
		this.newPosition = newPosition;
	}

	public void again(int position) {

	}

	public void setJiXuanEdit() {
		if (editZhuma != null) {
			editZhuma.setTextColor(Color.RED);
			editZhuma.setText("摇一摇可以机选一注");
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
		stopSensor();

	}

	public void stopSensor() {
		if (areaNums != null) {
			for (int i = 0; i < areaNums.length; i++) {
				if (areaNums[i].jixuanBtn != null && areaNums[i].isSensor
						&& isJixuan == true) {
					baseSensor.stopAction();
				}
			}
		}
	}
	
	public void closeMediaPlayer(){
		if (areaNums != null) {
			for (int i = 0; i < areaNums.length; i++) {
				if (areaNums[i].jixuanBtn != null && areaNums[i].isSensor
						&& isJixuan == true) {
					if(areaNums[i].jixuanBtn.animation != null){
						areaNums[i].jixuanBtn.animation.closeMediaPlayer();
					}
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
		startSensor();

	}

	public void startSensor() {
		if (areaNums != null) {
			for (int i = 0; i < areaNums.length; i++) {
				if (areaNums[i].jixuanBtn != null && areaNums[i].isSensor
						&& isJixuan == true) {
					baseSensor.startAction();
					setJiXuanEdit();
				}
			}
		}
	}

	/**
	 * 实现震动的类
	 * 
	 * @author Administrator
	 * 
	 */
	public class BallSensor extends SensorActivity {

		public BallSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
			if (areaNums != null) {
				if (areaNums[0].isAgain) {
					int[] iHighlightBallId = PublicMethod
							.getRandomsWithoutCollision(areaNums.length, 0,
									areaNums[0].areaNum - 1);
					for (int i = 0; i < areaNums.length; i++) {
						if (areaNums[i].jixuanBtn != null) {
							areaNums[i].jixuanBtn.onclickText(i,iHighlightBallId);
						}
						
					}
				} else {
					for (int i = 0; i < areaNums.length; i++) {
						if (areaNums[i].jixuanBtn != null) {
							areaNums[i].jixuanBtn.dialogOnclick();
						}
					}
				}
			}
		}

	}
	
	public void setAllBall(int i, int[] iHighlightBallId) {
		areaNums[i].table.clearAllHighlights();
		areaNums[i].table.changeBallState(areaNums[i].chosenBallSum,
				iHighlightBallId[i]);
	}
}
