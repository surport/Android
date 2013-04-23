/**
 * 
 */
package com.ruyicai.util;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/** 
 * 实现手机震动的类
 * @author Administrator
 * 
 */
public abstract class SensorActivity {
	/** Called when the activity is first created. */

	private long initTime = 0;
	private long lastTime = 0;
	private long curTime = 0;
	private long duration = 0;
	private float last_x = 0.0f;
	private float last_y = 0.0f;
	private float last_z = 0.0f;
	private float shake = 0.0f;
	private float totalShake = 0.0f;
	private Context context;
	private SensorManager sm = null;
//	private ShellRWSharesPreferences shellRW;
	public void startAction() {
		sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		int sensorType = android.hardware.Sensor.TYPE_ACCELEROMETER;
		sm.registerListener(mySensorEventListener, sm
				.getDefaultSensor(sensorType),
				SensorManager.SENSOR_DELAY_FASTEST);
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

				// 获取当前时刻的毫秒数

				curTime = System.currentTimeMillis();

				// 100毫秒检测一次

				float aa = curTime - lastTime;

				if ((curTime - lastTime) > 100) {
					duration = (curTime - lastTime);
					// 看是不是刚开始晃动
					if (last_x == 0.0f && last_y == 0.0f && last_z == 0.0f) {
						// last_x、last_y、last_z同时为0时，表示刚刚开始记录
						initTime = System.currentTimeMillis();
					} else {
						// 单次晃动幅度
						shake = (Math.abs(x - last_x) + Math.abs(y - last_y) + Math
								.abs(z - last_z));

					}

					// 把每次的晃动幅度相加，得到总体晃动幅度
					totalShake += shake;
					// 判断是否为摇动
					if (shake > 20) {
						action();
						onVibrator();
						initShake();
					}
					last_x = x;
					last_y = y;
					last_z = z;
					lastTime = curTime;
				}
			}
		}
	};

    /**
     * 实现震动的方法
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
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(context, "addInfo");
		boolean isOn= shellRW.getBoolean("isOn");
		if(!isOn){
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		if (vibrator == null) {
			Vibrator localVibrator = (Vibrator) context.getApplicationContext()
					.getSystemService("vibrator");
			vibrator = localVibrator;
		}
		vibrator.vibrate(100L);
		}
	}

	// 摇动初始化

	public void initShake() {
		lastTime = 0;
		duration = 0;
		curTime = 0;
		initTime = 0;
		last_x = 0.0f;
		last_y = 0.0f;
		last_z = 0.0f;
		shake = 0.0f;
		totalShake = 0.0f;
	}
}
