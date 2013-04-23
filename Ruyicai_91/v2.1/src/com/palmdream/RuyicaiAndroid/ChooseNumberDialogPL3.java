package com.palmdream.RuyicaiAndroid;

import com.palmdream.RuyicaiAndroid.R;
import com.palmdream.RuyicaiAndroid.R.id;
import com.palmdream.RuyicaiAndroid.R.layout;
import com.palmdream.RuyicaiAndroid.R.string;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 
 * @author wangyl 用于机选弹出选球个数的对话框
 * 
 */
public class ChooseNumberDialogPL3 extends Dialog implements OnClickListener,
		SeekBar.OnSeekBarChangeListener {
	MyDialogListener iListener;
	int iWhichLayout; // 1为直选,2为组3,3为组6,4为胆拖

	// 直选
	SeekBar mSeekBarZhixuanBaiwei;
	SeekBar mSeekBarZhixuanShiwei;
	SeekBar mSeekBarZhixuanGewei;

	TextView iTVZhixuanDialogTips;
	TextView iTVZhixuanBaiwei;
	TextView iTVZhixuanShiwei;
	TextView iTVZhixuanGewei;

	int iProgressZhixuanBaiwei = 1;
	int iProgressZhixuanShiwei = 1;
	int iProgressZhixuanGewei = 1;

	Button zhixuan_okBtn;
	Button zhixuan_cancelBtn;

	// 组3
	SeekBar mSeekBarZu3;

	TextView iTVZu3DialogTips;
	TextView iTVZu3;

	int iProgressZu3 = 2;

	Button zu3_okBtn;
	Button zu3_cancelBtn;

	// 组6
	SeekBar mSeekBarZu6;

	TextView iTVZu6DialogTips;
	TextView iTVZu6;

	int iProgressZu6 = 3;

	Button zu6_okBtn;
	Button zu6_cancelBtn;

	//和值直选
	SeekBar mSeekBarZhixuanHezhi;

	TextView iTVZhixuanHezhiDialogTips;
	TextView iTVZhixuanHezhi;

	int iProgressZhixuanHezhi = 1;

	Button ZhixuanHezhi_okBtn;
	Button ZhixuanHezhi_cancelBtn;

	//和值组三
	SeekBar mSeekBarZu3Hezhi;

	TextView iTVZu3HezhiDialogTips;
	TextView iTVZu3Hezhi;

	int iProgressZu3Hezhi = 1;

	Button Zu3Hezhi_okBtn;
	Button Zu3Hezhi_cancelBtn;
	
	//和值组六
	SeekBar mSeekBarZu6Hezhi;

	TextView iTVZu6HezhiDialogTips;
	TextView iTVZu6Hezhi;

	int iProgressZu6Hezhi = 1;

	Button Zu6Hezhi_okBtn;
	Button Zu6Hezhi_cancelBtn;
	
	
	public ChooseNumberDialogPL3(Context context) {
		super(context);
	}

	public ChooseNumberDialogPL3(Context context, MyDialogListener aListener) {
		super(context);
		this.iListener = aListener;
	}

	public ChooseNumberDialogPL3(Context context, int aWhichDialog,
			MyDialogListener aListener) {
		super(context);
		iWhichLayout = aWhichDialog;
		this.iListener = aListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 直选
		if (iWhichLayout == 1) {
			setContentView(R.layout.choose_pl3_zhixuan_number_dialog);
			setTitle(R.string.choose_zhixuan_title);

			iTVZhixuanDialogTips = (TextView) findViewById(R.id.tv_zhixuan_dialog_tips);
			updateDialogTips();

			iTVZhixuanBaiwei = (TextView) findViewById(R.id.tv_zhixuan_baiwei_number);
			iTVZhixuanShiwei = (TextView) findViewById(R.id.tv_zhixuan_shiwei_number);
			iTVZhixuanGewei = (TextView) findViewById(R.id.tv_zhixuan_gewei_number);
			iTVZhixuanBaiwei.setText("" + iProgressZhixuanBaiwei);
			iTVZhixuanShiwei.setText("" + iProgressZhixuanShiwei);
			iTVZhixuanGewei.setText("" + iProgressZhixuanGewei);

			mSeekBarZhixuanBaiwei = (SeekBar) findViewById(R.id.seek_zhixuan_baiwei);
			mSeekBarZhixuanBaiwei.setProgress(iProgressZhixuanBaiwei);
			mSeekBarZhixuanBaiwei.setOnSeekBarChangeListener(this);
			mSeekBarZhixuanShiwei = (SeekBar) findViewById(R.id.seek_zhixuan_shiwei);
			mSeekBarZhixuanShiwei.setProgress(iProgressZhixuanShiwei);
			mSeekBarZhixuanShiwei.setOnSeekBarChangeListener(this);
			mSeekBarZhixuanGewei = (SeekBar) findViewById(R.id.seek_zhixuan_gewei);
			mSeekBarZhixuanGewei.setProgress(iProgressZhixuanGewei);
			mSeekBarZhixuanGewei.setOnSeekBarChangeListener(this);

			ImageButton baiweiAddBtn = (ImageButton) findViewById(R.id.zhixuan_baiwei_add);
			ImageButton baiweiSubtractBtn = (ImageButton) findViewById(R.id.zhixuan_baiwei_subtract);
			baiweiAddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarZhixuanBaiwei.setProgress(++iProgressZhixuanBaiwei);
				}

			});
			baiweiSubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarZhixuanBaiwei
									.setProgress(--iProgressZhixuanBaiwei);
						}

					});

			ImageButton shiweiAddBtn = (ImageButton) findViewById(R.id.zhixuan_shiwei_add);
			ImageButton shiweiSubtractBtn = (ImageButton) findViewById(R.id.zhixuan_shiwei_subtract);
			shiweiAddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarZhixuanShiwei.setProgress(++iProgressZhixuanShiwei);
				}

			});
			shiweiSubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarZhixuanShiwei
									.setProgress(--iProgressZhixuanShiwei);
						}

					});

			ImageButton geweiAddBtn = (ImageButton) findViewById(R.id.zhixuan_gewei_add);
			ImageButton geweiSubtractBtn = (ImageButton) findViewById(R.id.zhixuan_gewei_subtract);
			geweiAddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarZhixuanGewei.setProgress(++iProgressZhixuanGewei);
				}

			});
			geweiSubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarZhixuanGewei
									.setProgress(--iProgressZhixuanGewei);
						}

					});

			zhixuan_okBtn = (Button) findViewById(R.id.zhixuan_okButton);
			zhixuan_cancelBtn = (Button) findViewById(R.id.zhixuan_cancelButton);
			zhixuan_okBtn.setOnClickListener(this);
			zhixuan_cancelBtn.setOnClickListener(this);

		}
		// 组3
		if (iWhichLayout == 2) {
			setContentView(R.layout.choose_pl3_zu3_number_dialog);
			setTitle(R.string.choose_zhixuan_title);

			iTVZu3DialogTips = (TextView) findViewById(R.id.tv_zu3_dialog_tips);
			updateDialogTips();

			iTVZu3 = (TextView) findViewById(R.id.tv_zu3);
			iTVZu3.setText("" + iProgressZu3);

			mSeekBarZu3 = (SeekBar) findViewById(R.id.seek_zu3);
			mSeekBarZu3.setProgress(iProgressZu3);
			mSeekBarZu3.setOnSeekBarChangeListener(this);

			ImageButton zu3AddBtn = (ImageButton) findViewById(R.id.zu3_add);
			ImageButton zu3SubtractBtn = (ImageButton) findViewById(R.id.zu3_subtract);
			zu3AddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarZu3.setProgress(++iProgressZu3);
				}

			});
			zu3SubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarZu3.setProgress(--iProgressZu3);
						}

					});

			zu3_okBtn = (Button) findViewById(R.id.zu3_okButton);
			zu3_okBtn.setOnClickListener(this);
			zu3_cancelBtn = (Button) findViewById(R.id.zu3_cancelButton);
			zu3_cancelBtn.setOnClickListener(this);
		}
		// 组6
		if (iWhichLayout == 3) {
			setContentView(R.layout.choose_pl3_zu6_number_dialog);
			setTitle(R.string.choose_zhixuan_title);

			iTVZu6DialogTips = (TextView) findViewById(R.id.tv_zu6_dialog_tips);
			updateDialogTips();

			iTVZu6 = (TextView) findViewById(R.id.tv_zu6);
			iTVZu6.setText("" + iProgressZu6);

			mSeekBarZu6 = (SeekBar) findViewById(R.id.seek_zu6);
			mSeekBarZu6.setProgress(iProgressZu6);
			mSeekBarZu6.setOnSeekBarChangeListener(this);

			ImageButton zu6AddBtn = (ImageButton) findViewById(R.id.zu6_add);
			ImageButton zu6SubtractBtn = (ImageButton) findViewById(R.id.zu6_subtract);
			zu6AddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarZu6.setProgress(++iProgressZu6);
				}

			});
			zu6SubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarZu6.setProgress(--iProgressZu6);
						}

					});

			zu6_okBtn = (Button) findViewById(R.id.zu6_okButton);
			zu6_okBtn.setOnClickListener(this);
			zu6_cancelBtn = (Button) findViewById(R.id.zu6_cancelButton);
			zu6_cancelBtn.setOnClickListener(this);

		}
		// 和值直选
		if (iWhichLayout == 4) {

			setContentView(R.layout.choose_pl3_zhixuanhezhi_number_dialog);
			setTitle(R.string.choose_zhixuan_title);

			iTVZhixuanHezhiDialogTips = (TextView) findViewById(R.id.tv_zhixuanhezhi_dialog_tips);

			iTVZhixuanHezhi = (TextView) findViewById(R.id.tv_zhixuanhezhi);
			iTVZhixuanHezhi.setText("" + iProgressZhixuanHezhi);

			mSeekBarZhixuanHezhi = (SeekBar) findViewById(R.id.seek_zhixuanhezhi);
			mSeekBarZhixuanHezhi.setProgress(iProgressZhixuanHezhi);
			mSeekBarZhixuanHezhi.setOnSeekBarChangeListener(this);

			ImageButton ZhixuanHezhiAddBtn = (ImageButton) findViewById(R.id.zhixuanhezhi_add);
			ImageButton ZhixuanHezhiSubtractBtn = (ImageButton) findViewById(R.id.zhixuanhezhi_subtract);
			ZhixuanHezhiAddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarZhixuanHezhi.setProgress(++iProgressZhixuanHezhi);
				}

			});
			ZhixuanHezhiSubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarZhixuanHezhi.setProgress(--iProgressZhixuanHezhi);
						}

					});

			ZhixuanHezhi_okBtn = (Button) findViewById(R.id.zhixuanhezhi_okButton);
			ZhixuanHezhi_okBtn.setOnClickListener(this);
			ZhixuanHezhi_cancelBtn = (Button) findViewById(R.id.zhixuanhezhi_cancelButton);
			ZhixuanHezhi_cancelBtn.setOnClickListener(this);

		
		}
		// 和值组三
		if (iWhichLayout == 5) {

			setContentView(R.layout.choose_pl3_zu3hezhi_number_dialog);
			setTitle(R.string.choose_zhixuan_title);

			iTVZu3HezhiDialogTips = (TextView) findViewById(R.id.tv_zu3hezhi_dialog_tips);

			iTVZu3Hezhi = (TextView) findViewById(R.id.tv_zu3hezhi);
			iTVZu3Hezhi.setText("" + iProgressZu3Hezhi);

			mSeekBarZu3Hezhi = (SeekBar) findViewById(R.id.seek_zu3hezhi);
			mSeekBarZu3Hezhi.setProgress(iProgressZu3Hezhi);
			mSeekBarZu3Hezhi.setOnSeekBarChangeListener(this);

			ImageButton Zu3HezhiAddBtn = (ImageButton) findViewById(R.id.zu3hezhi_add);
			ImageButton Zu3HezhiSubtractBtn = (ImageButton) findViewById(R.id.zu3hezhi_subtract);
			Zu3HezhiAddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarZu3Hezhi.setProgress(++iProgressZu3Hezhi);
				}

			});
			Zu3HezhiSubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarZu3Hezhi.setProgress(--iProgressZu3Hezhi);
						}

					});

			Zu3Hezhi_okBtn = (Button) findViewById(R.id.zu3hezhi_okButton);
			Zu3Hezhi_okBtn.setOnClickListener(this);
			Zu3Hezhi_cancelBtn = (Button) findViewById(R.id.zu3hezhi_cancelButton);
			Zu3Hezhi_cancelBtn.setOnClickListener(this);

		
		}
		// 和值组六
		if (iWhichLayout == 6) {

			setContentView(R.layout.choose_pl3_zu6hezhi_number_dialog);
			setTitle(R.string.choose_zhixuan_title);

			iTVZu6HezhiDialogTips = (TextView) findViewById(R.id.tv_zu6hezhi_dialog_tips);

			iTVZu6Hezhi = (TextView) findViewById(R.id.tv_zu6hezhi);
			iTVZu6Hezhi.setText("" + iProgressZu6Hezhi);

			mSeekBarZu6Hezhi = (SeekBar) findViewById(R.id.seek_zu6hezhi);
			mSeekBarZu6Hezhi.setProgress(iProgressZu6Hezhi);
			mSeekBarZu6Hezhi.setOnSeekBarChangeListener(this);

			ImageButton Zu6HezhiAddBtn = (ImageButton) findViewById(R.id.zu6hezhi_add);
			ImageButton Zu6HezhiSubtractBtn = (ImageButton) findViewById(R.id.zu6hezhi_subtract);
			Zu6HezhiAddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarZu6Hezhi.setProgress(++iProgressZu6Hezhi);
				}

			});
			Zu6HezhiSubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarZu6Hezhi.setProgress(--iProgressZu6Hezhi);
						}

					});

			Zu6Hezhi_okBtn = (Button) findViewById(R.id.zu6hezhi_okButton);
			Zu6Hezhi_okBtn.setOnClickListener(this);
			Zu6Hezhi_cancelBtn = (Button) findViewById(R.id.zu6hezhi_cancelButton);
			Zu6Hezhi_cancelBtn.setOnClickListener(this);

		
		}

	}

	/**
	 * 对话框提示
	 */
	public void updateDialogTips() {

		switch (iWhichLayout) {
		// 直选对话框小提示
		case 1:
			if (iProgressZhixuanBaiwei == 1 && iProgressZhixuanShiwei == 1
					&& iProgressZhixuanGewei == 1) {
				iTVZhixuanDialogTips
						.setText(R.string.zhixuan_danshi_dialog_tips);
			} else if (iProgressZhixuanBaiwei > 1 || iProgressZhixuanShiwei > 1
					|| iProgressZhixuanGewei > 1) {
				iTVZhixuanDialogTips
						.setText(R.string.zhixuan_fushi_dialog_tips);
			}
			break;
		// 组6
		case 3:
			if (iProgressZu6 == 3) {
				iTVZu6DialogTips.setText(R.string.zu6_danshi_dialog_tips);
			} else {
				iTVZu6DialogTips.setText(R.string.zu6_fushi_dialog_tips);
			}
			break;
		}
	}

	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (seekBar.getId()) {
		// 直选
		case R.id.seek_zhixuan_baiwei:
			if (progress <= 0)
				seekBar.setProgress(1);
			iProgressZhixuanBaiwei = seekBar.getProgress();
			iTVZhixuanBaiwei.setText("" + iProgressZhixuanBaiwei);
			updateDialogTips();
			break;
		case R.id.seek_zhixuan_shiwei:
			if (progress <= 0)
				seekBar.setProgress(1);
			iProgressZhixuanShiwei = seekBar.getProgress();
			iTVZhixuanShiwei.setText("" + iProgressZhixuanShiwei);
			updateDialogTips();
			break;
		case R.id.seek_zhixuan_gewei:
			if (progress <= 0)
				seekBar.setProgress(1);
			iProgressZhixuanGewei = seekBar.getProgress();
			iTVZhixuanGewei.setText("" + iProgressZhixuanGewei);
			updateDialogTips();
			break;
		// 组3
		case R.id.seek_zu3:
			if (progress < 2)
				seekBar.setProgress(2);
			iProgressZu3 = seekBar.getProgress();
			iTVZu3.setText("" + iProgressZu3);
			break;
		// 组6
		case R.id.seek_zu6:
			if (progress < 3)
				seekBar.setProgress(3);
			iProgressZu6 = seekBar.getProgress();
			iTVZu6.setText("" + iProgressZu6);
			updateDialogTips();
			break;
		//和值直选
		case R.id.seek_zhixuanhezhi:
			if (progress < 1)
				seekBar.setProgress(1);
			iProgressZhixuanHezhi = seekBar.getProgress();
			iTVZhixuanHezhi.setText("" + iProgressZhixuanHezhi);
			break;
		//和值组3
		case R.id.seek_zu3hezhi:
			if (progress < 1)
				seekBar.setProgress(1);
			iProgressZu3Hezhi = seekBar.getProgress();
			iTVZu3Hezhi.setText("" + iProgressZu3Hezhi);
			break;
		//和值组6
		case R.id.seek_zu6hezhi:
			if (progress < 1)
				seekBar.setProgress(1);
			iProgressZu6Hezhi = seekBar.getProgress();
			iTVZu6Hezhi.setText("" + iProgressZu6Hezhi);
			break;
		default:
			break;
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	public void onClick(View v) {
		switch (v.getId()) {
		// 直选
		case R.id.zhixuan_okButton: {
			int[] iReturn = { iProgressZhixuanBaiwei, iProgressZhixuanShiwei,
					iProgressZhixuanGewei };
			iListener.onOkClick(iReturn);
			dismiss();
			break;
		}
		case R.id.zhixuan_cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		// 组3
		case R.id.zu3_okButton: {
			int[] iReturn = { iProgressZu3, iProgressZu3, iProgressZu3 };
			iListener.onOkClick(iReturn);
			dismiss();
			break;
		}
		case R.id.zu3_cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		// 组6
		case R.id.zu6_okButton: {
			int[] iReturn = { iProgressZu6, iProgressZu6, iProgressZu6 };
			iListener.onOkClick(iReturn);
			dismiss();
			break;
		}
		case R.id.zu6_cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		//和值直选
		case R.id.zhixuanhezhi_okButton: {
			int[] iReturn = { iProgressZhixuanHezhi, iProgressZhixuanHezhi, iProgressZhixuanHezhi };
			iListener.onOkClick(iReturn);
			dismiss();
			break;
		}
		case R.id.zhixuanhezhi_cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		//和值组三
		case R.id.zu3hezhi_okButton: {
			int[] iReturn = { iProgressZu3Hezhi, iProgressZu3Hezhi, iProgressZu3Hezhi };
			iListener.onOkClick(iReturn);
			dismiss();
			break;
		}
		case R.id.zu3hezhi_cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		//和值组六
		case R.id.zu6hezhi_okButton: {
			int[] iReturn = { iProgressZu6Hezhi, iProgressZu6Hezhi, iProgressZu6Hezhi };
			iListener.onOkClick(iReturn);
			dismiss();
			break;
		}
		case R.id.zu6hezhi_cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		}
	}
}
