package com.ruyicai.activity.more.lotnoalarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.palmdream.RuyicaiAndroid91.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

/**
 * 购彩设置界面：1.界面的显示；2.界面控件的事件
 * 
 * @author PengCX
 * 
 */
public class LotnoAlarmSetActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	private static final String TAG = "LotnoAlarmSetActivity";

	private static final String RECIVER_ACTION_FLAG = "com.android.alarmdemo.rebootreciver";

	private static final int ALARMTIME_LAYOUT_ID = 9;
	private static final int ALARMTIME_TIMEDIALOG_ID = 10;

	private static final int OPENVOICE_TOGGLEBUTTON_ID = 0;
	private static final int SSQ_TOGGLEBUTTON_ID = 1;
	private static final int DLT_TOGGLEBUTTON_ID = 2;
	private static final int FC3D_TOGGLEBUTTON_ID = 3;
	private static final int QLC_TOGGLEBUTTON_ID = 4;
	private static final int QXC_TOGGLEBUTTON_ID = 5;
	private static final int PL3_TOGGLEBUTTON_ID = 6;
	private static final int PL5_TOGGLEBUTTON_ID = 7;
	private static final int TWENTYFIVE_TOGGLEBUTTON_ID = 8;

	private LotnoAlarmManager lotnoAlarmManager;

	private LinearLayout linearlayout;
	private TextView alarmTimeTextView;
	private ListView alarmSettingListView;
	private ToggleButton openVoiceToggleButton, ssqToggleButton,
			dltToggleButton, fc3dToggleButton, qlcToggleButton,
			qxcToggleButton, pl3ToggleButton, pl5ToggleButton,
			tewntyfiveToggleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.lotno_alarm_setting);

		initScreenShow();
	}

	private void initScreenShow() {
		lotnoAlarmManager = LotnoAlarmManager.getInstance(this);

		initAlarmTimeTextview();

		initLotnoSettingListview();
	}

	private void initAlarmTimeTextview() {
		alarmTimeTextView = (TextView) findViewById(R.id.lotnoalarm_textview_time);

		linearlayout = (LinearLayout) findViewById(R.id.lotnoalarm_layout_time);
		linearlayout.setOnClickListener(this);

		updateAlarmTimeTextView();
	}

	private void initLotnoSettingListview() {
		openVoiceToggleButton = (ToggleButton) findViewById(R.id.lotnoalarm_togglebutton_openvoice);
		ssqToggleButton = (ToggleButton) findViewById(R.id.lotno_alarm_togglebutton_ssq);
		dltToggleButton = (ToggleButton) findViewById(R.id.lotno_alarm_togglebutton_dlt);
		fc3dToggleButton = (ToggleButton) findViewById(R.id.lotno_alarm_togglebutton_fc3d);
		qlcToggleButton = (ToggleButton) findViewById(R.id.lotno_alarm_togglebutton_qlc);
		qxcToggleButton = (ToggleButton) findViewById(R.id.lotno_alarm_togglebutton_qxc);
		pl3ToggleButton = (ToggleButton) findViewById(R.id.lotno_alarm_togglebutton_pl3);
		pl5ToggleButton = (ToggleButton) findViewById(R.id.lotno_alarm_togglebutton_pl5);
		tewntyfiveToggleButton = (ToggleButton) findViewById(R.id.lotno_alarm_toggle_twentyfive);

		openVoiceToggleButton.setOnCheckedChangeListener(this);
		ssqToggleButton.setOnCheckedChangeListener(this);
		dltToggleButton.setOnCheckedChangeListener(this);
		fc3dToggleButton.setOnCheckedChangeListener(this);
		qlcToggleButton.setOnCheckedChangeListener(this);
		qxcToggleButton.setOnCheckedChangeListener(this);
		pl3ToggleButton.setOnCheckedChangeListener(this);
		pl5ToggleButton.setOnCheckedChangeListener(this);
		tewntyfiveToggleButton.setOnCheckedChangeListener(this);

		updateLotnoToggleButton();
	}

	private void updateAlarmTimeTextView() {
		int hour = lotnoAlarmManager
				.getAlarmTimeSetting(LotnoAlarmManager.PREFERENCE_HOUR_KEY);
		int minute = lotnoAlarmManager
				.getAlarmTimeSetting(LotnoAlarmManager.PREFERENCE_MINUTE_KEY);

		alarmTimeTextView.setText(new StringBuilder()
				.append(fromatNumberToDoublePlace(hour)).append(":")
				.append(fromatNumberToDoublePlace(minute)));
	}

	/**
	 * 格式化时间数字：如果为个位，则前面加“0”
	 * 
	 * @param num
	 *            时间数字
	 * @return 时间数字字符串
	 */
	private String fromatNumberToDoublePlace(int num) {
		String result = "";
		if (num < 10) {
			result = "0" + String.valueOf(num);
		} else {
			result = String.valueOf(num);
		}
		return result;
	}

	private void updateLotnoToggleButton() {
		openVoiceToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_OPENVOICE_KEY));
		ssqToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_SSQ_KEY));
		dltToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_DLT_KEY));
		fc3dToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_FC3D_KEY));
		qlcToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_QLC_KEY));
		qxcToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_QXC_KEY));
		pl3ToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_PL3_KEY));
		pl5ToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_PL5_KEY));
		tewntyfiveToggleButton.setChecked(lotnoAlarmManager
				.getLotnoSetting(LotnoAlarmManager.PREFERENCE_TWENTYFIVE_KEY));
	}

	/**
	 * 获取彩种设置列表显示内容
	 * 
	 * @return 彩种设置列表显示内容集合
	 */
	private List<Map<String, String>> getLotnoSettingContent() {
		List<Map<String, String>> contents = new ArrayList<Map<String, String>>();

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("title", "双色球");
		map1.put("data", "每周二、四、日提醒");
		contents.add(map1);

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("title", "大乐透");
		map2.put("data", "每周一、三、六提醒");
		contents.add(map2);

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("title", "福彩3D");
		map3.put("data", "每天提醒");
		contents.add(map3);

		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("title", "七乐彩");
		map4.put("data", "每周一、三、五提醒");
		contents.add(map4);

		Map<String, String> map5 = new HashMap<String, String>();
		map5.put("title", "七星彩");
		map5.put("data", "每周二、五、日提醒");
		contents.add(map5);

		Map<String, String> map6 = new HashMap<String, String>();
		map6.put("title", "排列三");
		map6.put("data", "每天提醒");
		contents.add(map6);

		Map<String, String> map7 = new HashMap<String, String>();
		map7.put("title", "排列五");
		map7.put("data", "每天提醒");
		contents.add(map7);

		Map<String, String> map8 = new HashMap<String, String>();
		map8.put("title", "22选5");
		map8.put("data", "每天提醒");
		contents.add(map8);

		return contents;
	}

	@Override
	public void onClick(View v) {
		int id = Integer.valueOf((String) v.getTag());

		switch (id) {
		case ALARMTIME_LAYOUT_ID:
			showDialog(ALARMTIME_TIMEDIALOG_ID);
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;

		switch (id) {
		case ALARMTIME_TIMEDIALOG_ID:
			dialog = new TimePickerDialog(
					this,
					timeSetListener,
					lotnoAlarmManager
							.getAlarmTimeSetting(LotnoAlarmManager.PREFERENCE_HOUR_KEY),
					lotnoAlarmManager
							.getAlarmTimeSetting(LotnoAlarmManager.PREFERENCE_MINUTE_KEY),
					false);
			break;
		}

		return dialog;
	}

	private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			lotnoAlarmManager.setAlarmTimeSetting(
					LotnoAlarmManager.PREFERENCE_HOUR_KEY, hourOfDay);
			lotnoAlarmManager.setAlarmTimeSetting(
					LotnoAlarmManager.PREFERENCE_MINUTE_KEY, minuteOfHour);
			updateAlarmTimeTextView();
		}
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int id = Integer.valueOf(buttonView.getTag().toString());

		switch (id) {
		case OPENVOICE_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_OPENVOICE_KEY, isChecked);
			break;
		case SSQ_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_SSQ_KEY, isChecked);
			break;
		case DLT_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_DLT_KEY, isChecked);
			break;
		case FC3D_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_FC3D_KEY, isChecked);
			break;
		case QLC_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_QLC_KEY, isChecked);
			break;
		case QXC_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_QXC_KEY, isChecked);
			break;
		case PL3_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_PL3_KEY, isChecked);
			break;
		case PL5_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_PL5_KEY, isChecked);
			break;
		case TWENTYFIVE_TOGGLEBUTTON_ID:
			lotnoAlarmManager.setLotnoSetting(
					LotnoAlarmManager.PREFERENCE_TWENTYFIVE_KEY, isChecked);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		sendAlarmSettingBrocast();
	}

	public void sendAlarmSettingBrocast() {
		Intent intentForReciver = new Intent();
		intentForReciver.setAction(RECIVER_ACTION_FLAG);

		this.sendBroadcast(intentForReciver);
	}
}
