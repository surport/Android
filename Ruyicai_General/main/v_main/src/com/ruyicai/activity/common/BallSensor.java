package com.ruyicai.activity.common;

import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;

/**
 * 实现震动的类
 * 
 * @author Administrator
 * 
 */
public class BallSensor extends SensorActivity {
	
	public int dataCol[];/**符合提交的数据*/
	private static int selectNum = 2;
	public String randomString[] = null;
	private Handler handler;
	private int BALL_SENDSOR_RESULT = 1;
	private List<String> list = null;
	Builder reSelectDlg = null;
	public BallSensor(Context context,Handler handler) {
		if (Constants.isDebug) PublicMethod.outLog(this.getClass().getSimpleName(), "BallSensor()");
		getContext(context);
		this.handler = handler;
	}

	@Override
	public void action() {
		if (Constants.isDebug) PublicMethod.outLog(this.getClass().getSimpleName(), "Action()");
		sendMessage();
		
	}
	private void sendMessage() {
		if (list != null && list.size() > 2) {
			randomString = getRandom(selectNum, list);
		} else if (list != null && list.size() == 2) {
			randomString = new String[2];
			randomString[0] = list.get(0);
			randomString[1] = list.get(1);
		} else {
			return;
		}
		Message mesResultIds = handler.obtainMessage();
		mesResultIds.what = BALL_SENDSOR_RESULT;
		mesResultIds.obj = randomString;
		handler.sendMessage(mesResultIds);
		
	}
	public void setDataCol(int[] dataCol) {
		this.dataCol = dataCol;
	}
	
	public int[] getDataCol() {
		return this.dataCol;
	}
	
	public void setList(List<String> list){
		this.list = list;
	}
	public List<String> getList() {
		return this.list;
	}

	public void startSensor() {
		startAction();
	}
	
	public void stopSensor() {
		stopAction();
	}
	/**
	 * 随机数
	 * 
	 * @param int aRandomNums 随机高亮的数量
	 * @parem dataCol 随即数据结果集合
	 * @return void
	 */
	public int[] getRandom(int aRandomNums,int[] dataCol) {
		return PublicMethod.getRandomsWithoutCollision(
				aRandomNums, 0, dataCol.length - 1);
	}
	
	/**
	 * 随机数
	 * @param int aRandomNums 随机高亮的数量
	 * @parem dataCol 随即数据结果集合
	 * @return int[]
	 */
	public String[] getRandom(int aRandomNums,List<String> list) {
		return PublicMethod.getRandomsWithoutCollision(
				aRandomNums, list);
	}
}

