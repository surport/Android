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
 * @author wangyl 用于福彩3D机选弹出选球个数的对话框
 * 
 */
public class ChooseNumberDialog3D extends Dialog implements OnClickListener,
		SeekBar.OnSeekBarChangeListener {
	MyDialogListener iListener;
	int iWhichLayout; // 1为直选,2为组3,3为组6,4为胆拖

	// 福彩3D直选
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

	// 福彩3D组3
	SeekBar mSeekBarZu3;

	TextView iTVZu3DialogTips;
	TextView iTVZu3;

	int iProgressZu3 = 2;

	Button zu3_okBtn;
	Button zu3_cancelBtn;

	// 福彩3D组6
	SeekBar mSeekBarZu6;

	TextView iTVZu6DialogTips;
	TextView iTVZu6;

	int iProgressZu6 = 3;

	Button zu6_okBtn;
	Button zu6_cancelBtn;

	// 福彩3D胆拖
	SeekBar mSeekBarDantuoDanma;
	SeekBar mSeekBarDantuoTuoma;
	TextView iTVDantuoTips;
	TextView iTVDantuoDanma;
	TextView iTVDantuoTuoma;

	int iProgressDantuoDanma = 1;
	int iProgressDantuoTuoma = 1;

	Button dantuo_okBtn;
	Button dantuo_cancelBtn;

	public ChooseNumberDialog3D(Context context) {
		super(context);
	}

	public ChooseNumberDialog3D(Context context, MyDialogListener aListener) {
		super(context);
		this.iListener = aListener;
	}

	public ChooseNumberDialog3D(Context context, int aWhichDialog,
			MyDialogListener aListener) {
		super(context);
		iWhichLayout = aWhichDialog;
		this.iListener = aListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 福彩3D直选
		if (iWhichLayout == 1) {
			setContentView(R.layout.choose_fc3d_zhixuan_number_dialog);
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
		// 福彩3D组3
		if (iWhichLayout == 2) {
			setContentView(R.layout.choose_fc3d_zu3_number_dialog);
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
		// 福彩3D组6
		if (iWhichLayout == 3) {
			setContentView(R.layout.choose_fc3d_zu6_number_dialog);
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
		// 福彩3D胆拖
		if (iWhichLayout == 4) {
			setContentView(R.layout.choose_fc3d_dantuo_number_dialog);
			setTitle(R.string.choose_zhixuan_title);

			iTVDantuoTips = (TextView) findViewById(R.id.tv_dantuo_dialog_tips);
			iTVDantuoDanma = (TextView) findViewById(R.id.tv_dantuo_danma_number);
			iTVDantuoDanma.setText("" + iProgressDantuoDanma);
			iTVDantuoTuoma = (TextView) findViewById(R.id.tv_dantuo_tuoma_number);
			iTVDantuoTuoma.setText("" + iProgressDantuoTuoma);

			mSeekBarDantuoDanma = (SeekBar) findViewById(R.id.seek_dantuo_danma);
			mSeekBarDantuoDanma.setProgress(iProgressDantuoDanma);
			mSeekBarDantuoDanma.setOnSeekBarChangeListener(this);
			mSeekBarDantuoTuoma = (SeekBar) findViewById(R.id.seek_dantuo_tuoma);
			mSeekBarDantuoTuoma.setProgress(iProgressDantuoTuoma);
			mSeekBarDantuoTuoma.setOnSeekBarChangeListener(this);

			ImageButton danmaAddBtn = (ImageButton) findViewById(R.id.dantuo_danma_add);
			ImageButton danmaSubtractBtn = (ImageButton) findViewById(R.id.dantuo_danma_subtract);
			danmaAddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarDantuoDanma.setProgress(++iProgressDantuoDanma);
				}

			});
			danmaSubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarDantuoDanma
									.setProgress(--iProgressDantuoDanma);
						}

					});

			ImageButton tuomaAddBtn = (ImageButton) findViewById(R.id.dantuo_tuoma_add);
			ImageButton tuomaSubtractBtn = (ImageButton) findViewById(R.id.dantuo_tuoma_subtract);
			tuomaAddBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarDantuoTuoma.setProgress(++iProgressDantuoTuoma);
				}

			});
			tuomaSubtractBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarDantuoTuoma
									.setProgress(--iProgressDantuoTuoma);
						}

					});

			dantuo_okBtn = (Button) findViewById(R.id.dantuo_okButton);
			dantuo_okBtn.setOnClickListener(this);
			dantuo_cancelBtn = (Button) findViewById(R.id.dantuo_cancelButton);
			dantuo_cancelBtn.setOnClickListener(this);

		}

	}

	/**
	 * 对话框提示
	 */
	public void updateDialogTips() {

		switch (iWhichLayout) {
		// 福彩3D直选对话框小提示
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
		// 福彩3D组6
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
		// 福彩3D直选
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
		// 福彩3D组3
		case R.id.seek_zu3:
			if (progress < 2)
				seekBar.setProgress(2);
			iProgressZu3 = seekBar.getProgress();
			iTVZu3.setText("" + iProgressZu3);
			break;
		// 福彩3D组6
		case R.id.seek_zu6:
			if (progress < 3)
				seekBar.setProgress(3);
			iProgressZu6 = seekBar.getProgress();
			iTVZu6.setText("" + iProgressZu6);
			updateDialogTips();
			break;
		// 福彩3D胆拖
		case R.id.seek_dantuo_danma:
			if (progress <= 0)
				seekBar.setProgress(1);
			iProgressDantuoDanma = seekBar.getProgress();
			iTVDantuoDanma.setText("" + iProgressDantuoDanma);
			break;
		case R.id.seek_dantuo_tuoma:
			if (progress <= 0)
				seekBar.setProgress(1);
			iProgressDantuoTuoma = seekBar.getProgress();
			iTVDantuoTuoma.setText("" + iProgressDantuoTuoma);
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
		// 福彩3D直选
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
		// 福彩3D组3
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
		// 福彩3D组6
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
		// 福彩3D胆拖
		case R.id.dantuo_okButton:
			if (iProgressDantuoDanma + iProgressDantuoTuoma < 3) {// 胆码+拖码>=3
				iTVDantuoTips.setText("请至少再选择1位胆码或者拖码");
			}
			if (iProgressDantuoDanma + iProgressDantuoTuoma >= 11) {
				iTVDantuoTips.setText("胆码和拖码的小球个数不得超过10个");
			}
			if ((iProgressDantuoDanma + iProgressDantuoTuoma) > 2
					&& (iProgressDantuoDanma + iProgressDantuoTuoma) <= 10) {
				int[] iReturn = { iProgressDantuoDanma, iProgressDantuoTuoma,
						iProgressDantuoTuoma };
				iListener.onOkClick(iReturn);
				dismiss();
			}
			break;
		case R.id.dantuo_cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		}
	}
}
