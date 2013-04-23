package com.ruyicai.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.handler.MyDialogListener;
import com.ruyicai.util.PublicMethod;

public class ChooseNumberDialogQLC extends Dialog implements OnClickListener,
		SeekBar.OnSeekBarChangeListener {
	private Button okButton;
	private Button cancelButton;
	MyDialogListener iListener;

	int iWhichLayout; // 0 复式
	// 1 胆拖

	SeekBar mSeekBarRed;
	SeekBar mSeekBarBlue;

	// 红 拖码
	SeekBar mSeekBarRedTuo;
	int iProgressRedTuo = 1;

	int iProgressRed = 7;
	int iProgressBlue = 1;

	TextView iDialogTip;
	TextView iTVRed;
	TextView iTVRedTuo;
	TextView iTVBlue;

	public ChooseNumberDialogQLC(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ChooseNumberDialogQLC(Context context, int aWhichDialog,
			MyDialogListener aListener) {
		super(context);
		// TODO Auto-generated constructor stub
		// 复式
		iWhichLayout = aWhichDialog;
		this.iListener = aListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (iWhichLayout == 0) {
			setContentView(R.layout.choose_number_qlc_fushi_dialog);
			iDialogTip = (TextView) findViewById(R.id.qlc_fushi_dialog_game_type_tip_change);
			updateDialogTips();

			iTVRed = (TextView) findViewById(R.id.qlc_fushi_dialog_tv_number_change);
			iTVRed.setText("" + iProgressRed);
	

			mSeekBarRed = (SeekBar) findViewById(R.id.qlc_fushi_dialog_seekbar_red);
			mSeekBarRed.setProgress(iProgressRed);
			mSeekBarRed.setOnSeekBarChangeListener(this);

			/*
			 * 点击加减图标，对seekbar进行设置：
			 * 
			 * @param ImageButton imageButton final SeekBar mSeekBar final int
			 * isAdd, 1表示加 -1表示减
			 * 
			 * @return void
			 */
			ImageButton qlc_fushi_red_add = (ImageButton) findViewById(R.id.qlc_fushi_dialog_red_add);
			ImageButton qlc_fushi_red_subtract = (ImageButton) findViewById(R.id.qlc_fushi_dialog_red_subtract);


			setSeekWhenAddOrSub(qlc_fushi_red_add, mSeekBarRed, 1, 0);
			setSeekWhenAddOrSub(qlc_fushi_red_subtract, mSeekBarRed, -1, 0);


			setTitle(R.string.choose_number_dialog_title_fushi);
		} else {
			setContentView(R.layout.choose_number_qlc_dantuo_dialog);
			iDialogTip = (TextView) findViewById(R.id.qlc_dantuo_dialog_tv_tips);
			updateDialogTips();

			iProgressRed = 6;
			iProgressRedTuo = 2;
			iTVRed = (TextView) findViewById(R.id.qlc_dantuo_dialog_reddan_tv_number_change);
			iTVRed.setText("" + iProgressRed);

			iTVRedTuo = (TextView) findViewById(R.id.qlc_dantuo_dialog_red_tuo_number_change);
			iTVRedTuo.setText("" + iProgressRedTuo);

			// iTVBlue = (TextView)findViewById(R.id.tv_blue_ball_number);
			// iTVBlue.setText(""+iProgressBlue);

			mSeekBarRed = (SeekBar) findViewById(R.id.qlc_dantuo_dialog_seek_reddan);
			mSeekBarRed.setProgress(iProgressRed);
			mSeekBarRed.setOnSeekBarChangeListener(this);

			mSeekBarRedTuo = (SeekBar) findViewById(R.id.qlc_dantuo_dialog_seek_redtuo);
			mSeekBarRedTuo.setProgress(iProgressRedTuo);
			mSeekBarRedTuo.setOnSeekBarChangeListener(this);


			ImageButton qlc_dantuo_red_subtract = (ImageButton) findViewById(R.id.qlc_dantuo_dialog_reddan_subtract);
			ImageButton qlc_dantuo_red_add = (ImageButton) findViewById(R.id.qlc_dantuo_dialog_reddan_add);
			ImageButton qlc_dantuo_red_tuo_subtract = (ImageButton) findViewById(R.id.qlc_dantuo_dialog_redtuo_subtract);
			ImageButton qlc_dantuo_red_tuo_add = (ImageButton) findViewById(R.id.qlc_dantuo_dialog_redtuo_add);


			setSeekWhenAddOrSub(qlc_dantuo_red_add, mSeekBarRed, 1, 0);
			setSeekWhenAddOrSub(qlc_dantuo_red_subtract, mSeekBarRed, -1, 0);

			setSeekWhenAddOrSub(qlc_dantuo_red_tuo_add, mSeekBarRedTuo, 1, 2);
			setSeekWhenAddOrSub(qlc_dantuo_red_tuo_subtract, mSeekBarRedTuo,
					-1, 2);


			setTitle(R.string.choose_number_dialog_title_dantuo);
		}
		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

	}

	public void updateDialogTips() {
		if (iWhichLayout == 0) {

			if (iProgressRed == 7)
				iDialogTip.setText(R.string.choose_number_dialog_tip7);
			else if (iProgressRed > 7)
				iDialogTip.setText(R.string.choose_number_dialog_tip8);
		} else {
			iDialogTip.setText(R.string.choose_number_dialog_tip5);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("------******" + seekBar.getId());
		switch (seekBar.getId()) {
		case R.id.qlc_fushi_dialog_seekbar_red:
			if (progress < 8)
				seekBar.setProgress(8);
			iProgressRed = seekBar.getProgress();
			iTVRed.setText("" + iProgressRed);
			updateDialogTips();
			break;

		case R.id.qlc_dantuo_dialog_seek_reddan:
			if (progress < 1)
				seekBar.setProgress(1);
			iProgressRed = seekBar.getProgress();

			if (iProgressRedTuo < 8 - iProgressRed) {
				iProgressRedTuo = 8 - iProgressRed;
				mSeekBarRedTuo.setProgress(iProgressRedTuo);
				iTVRedTuo.setText("" + iProgressRedTuo);
			}
			if (iProgressRedTuo > 26 - iProgressRed) {
				iProgressRedTuo = 26 - iProgressRed;
				mSeekBarRedTuo.setProgress(iProgressRedTuo);
				iTVRedTuo.setText("" + iProgressRedTuo);
			}
			iTVRed.setText("" + iProgressRed);

			updateDialogTips();
			break;
		case R.id.qlc_dantuo_dialog_seek_redtuo:
			if (progress < 2)
				seekBar.setProgress(2);
			iProgressRedTuo = seekBar.getProgress();
			if (progress < 8 - iProgressRed) {
				seekBar.setProgress(8 - iProgressRed);
				iProgressRed = 8 - iProgressRedTuo;
				mSeekBarRed.setProgress(iProgressRed);
				iTVRed.setText("" + iProgressRed);

			}

			if (iProgressRed >= 26 - iProgressRedTuo) {
				iProgressRed = 26 - iProgressRedTuo;
				// +iProgressRed+iProgressRedTuo);
				mSeekBarRed.setProgress(iProgressRed);
				iTVRed.setText("" + iProgressRed);
			}
			iTVRedTuo.setText("" + iProgressRedTuo);
			updateDialogTips();
			break;

		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		// PublicMethod.myOutput("--->>>"+v.getId());
		PublicMethod.myOutput("-----onClick" + v.getId());
		switch (v.getId()) {
		case R.id.okButton:
			int[] iReturnInts = null;
			if (iWhichLayout == 0) {
				iReturnInts = new int[2];
				iReturnInts[0] = iProgressRed;
				iReturnInts[1] = iProgressBlue;
			} else {
				if (iProgressRedTuo > 25) {
					iProgressRedTuo = 25;
				}
				iReturnInts = new int[3];
				iReturnInts[0] = iProgressRed;
				iReturnInts[1] = iProgressRedTuo;
				iReturnInts[2] = iProgressBlue;
			}
			iListener.onOkClick(iReturnInts);
			dismiss();
			break;
		case R.id.cancelButton:
			iListener.onCancelClick();
			cancel();
			break;
		}
	}

	// fqc edit 添加参数whichGroup 来判断对话框中seekbar是红球胆码 红球拖码 或者篮球 0表示红球胆吗 1表示篮球
	// 2表示红球拖码
	private void setSeekWhenAddOrSub(ImageButton imageButton,
			final SeekBar seekBar, final int isAdd, final int whichGroup) {
		imageButton.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (whichGroup == 0) {
					if (isAdd == 1){
						if(iProgressRed<6)
						seekBar.setProgress(++iProgressRed);
					}else{
						seekBar.setProgress(--iProgressRed);	
					}
				} else if (whichGroup == 1) {

					if (isAdd == 1){
						seekBar.setProgress(++iProgressBlue);
					}else{
						seekBar.setProgress(--iProgressBlue);
					}

				} else if (whichGroup == 2) {

					if (isAdd == 1){
						if(iProgressRedTuo<25)
						seekBar.setProgress(++iProgressRedTuo);	
					}
					else{
						seekBar.setProgress(--iProgressRedTuo);	
					}

				}
			}

		});

	}
}
