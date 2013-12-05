/**
 * 
 */
package com.ruyicai.util;

import com.ruyicai.constant.ShellRWConstants;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/**
 * 实现手机震动的类
 * 
 * @author Administrator
 * 
 */
public abstract class SensorActivity {
	private Context context;
	private SensorManager sm = null;

	// private ShellRWSharesPreferences shellRW;
	public void startAction() {
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		int sensorType = android.hardware.Sensor.TYPE_ACCELEROMETER;
		sm.registerListener(mySensorEventListener,
				sm.getDefaultSensor(sensorType),
				SensorManager.SENSOR_DELAY_UI);
	}

	public void getContext(Context context) {
		this.context = context;
	}

	public void stopAction() {
		if (sm != null) {
			sm.unregisterListener(mySensorEventListener);
		}

	}

	public abstract void action();

	public final SensorEventListener mySensorEventListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(android.hardware.Sensor sensor,
				int accuracy) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if (event.sensor.getType() == android.hardware.Sensor.TYPE_ACCELEROMETER) {

				// 获取加速度传感器的三个参数

				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				float z = event.values[SensorManager.DATA_Z];

				 if (Math.abs(x) > 14 || Math.abs(y) > 14 || Math.abs(z) > 14){
						action();
						onVibrator();
				 }
			}
		}
	};

	/**
	 * 实现震动的方法
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public void onVibrator() {
		// 读取是否震动参数
		RWSharedPreferences shellRW = new RWSharedPreferences(context,
				"addInfo");
		boolean isOn = shellRW.getBooleanValue(ShellRWConstants.ISON);
		if (!isOn) {
			Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			if (vibrator == null) {
				Vibrator localVibrator = (Vibrator) context
						.getApplicationContext().getSystemService("vibrator");
				vibrator = localVibrator;
			}
			vibrator.vibrate(100L);
		}
	}
}
