/**
 * 
 */
package com.palmdream.RuyicaiAndroid;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 */
public class ChooseNumberDialogSsc extends Dialog implements OnClickListener,
		SeekBar.OnSeekBarChangeListener {
	MyDialogListener iListener;

	int iWhichLayout; // 0 复式
	SeekBar mSeekBarWan;
	SeekBar mSeekBarQian;
	SeekBar mSeekBarBai;
	SeekBar mSeekBarShi;
	SeekBar mSeekBarGe;

	int iProgressWan = 1;
	int iProgressQian = 1;
	int iProgressBai = 1;
	int iProgressShi = 1;
	int iProgressGe = 1;

	TextView iDialogTip;
	TextView iTVWan;
	TextView iTVQian;
	TextView iTVBai;
	TextView iTVShi;
	TextView iTVGe;
	public static final int MAX = 10;
	public static final int MIN = 1;
	private Button okButton;
	private Button cancelButton;

	/**
	 * @param context
	 */
	public ChooseNumberDialogSsc(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ChooseNumberDialogSsc(Context context, int aWhichDialog,
			MyDialogListener aListener) {
		super(context);
		// TODO Auto-generated constructor stub
		// 复式
		iWhichLayout = aWhichDialog;
		this.iListener = aListener;
	}

	/**
	 * 入口函数
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_ssc_five_star);
		iDialogTip = (TextView) findViewById(R.id.ssc_text_type_title);
		updateDialogTips();

		iTVWan = (TextView) findViewById(R.id.ssc_wan_dialog_text_number);
		iTVWan.setText("" + iProgressWan);
		iTVQian = (TextView) findViewById(R.id.ssc_qian_dialog_text_number);
		iTVQian.setText("" + iProgressQian);
		iTVBai = (TextView) findViewById(R.id.ssc_bai_dialog_text_number);
		iTVBai.setText("" + iProgressBai);
		iTVShi = (TextView) findViewById(R.id.ssc_shi_dialog_text_number);
		iTVShi.setText("" + iProgressShi);
		iTVGe = (TextView) findViewById(R.id.ssc_ge_dialog_text_number);
		iTVGe.setText("" + iProgressGe);

		mSeekBarWan = (SeekBar) findViewById(R.id.ssc_wan_dialog_seekbar);
		mSeekBarWan.setProgress(iProgressWan);
		mSeekBarWan.setOnSeekBarChangeListener(this);
		mSeekBarQian = (SeekBar) findViewById(R.id.ssc_qian_dialog_seekbar);
		mSeekBarQian.setOnSeekBarChangeListener(this);
		mSeekBarQian.setProgress(iProgressQian);
		mSeekBarBai = (SeekBar) findViewById(R.id.ssc_bai_dialog_seekbar);
		mSeekBarBai.setOnSeekBarChangeListener(this);
		mSeekBarBai.setProgress(iProgressBai);
		mSeekBarShi = (SeekBar) findViewById(R.id.ssc_shi_dialog_seekbar);
		mSeekBarShi.setOnSeekBarChangeListener(this);
		mSeekBarShi.setProgress(iProgressShi);
		mSeekBarGe = (SeekBar) findViewById(R.id.ssc_ge_dialog_seekbar);
		mSeekBarGe.setOnSeekBarChangeListener(this);
		mSeekBarGe.setProgress(iProgressGe);
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param ImageButton imageButton final SeekBar mSeekBar final int
		 * isAdd, 1表示加 -1表示减
		 * 
		 * @return void
		 */
		ImageButton ssc_wan_add = (ImageButton) findViewById(R.id.ssc_wan_dialog_red_add);
		ImageButton ssc_wan_subtract = (ImageButton) findViewById(R.id.ssc_wan_dialog_subtract);
		ImageButton ssc_qian_add = (ImageButton) findViewById(R.id.ssc_qian_dialog_red_add);
		ImageButton ssc_qian_subtract = (ImageButton) findViewById(R.id.ssc_qian_dialog_subtract);
		ImageButton ssc_bai_add = (ImageButton) findViewById(R.id.ssc_bai_dialog_red_add);
		ImageButton ssc_bai_subtract = (ImageButton) findViewById(R.id.ssc_bai_dialog_subtract);
		ImageButton ssc_shi_add = (ImageButton) findViewById(R.id.ssc_shi_dialog_red_add);
		ImageButton ssc_shi_subtract = (ImageButton) findViewById(R.id.ssc_shi_dialog_subtract);
		ImageButton ssc_ge_add = (ImageButton) findViewById(R.id.ssc_ge_dialog_red_add);
		ImageButton ssc_ge_subtract = (ImageButton) findViewById(R.id.ssc_ge_dialog_subtract);

		setSeekWhenAddOrSub(ssc_wan_add, mSeekBarWan, true, 0);
		setSeekWhenAddOrSub(ssc_wan_subtract, mSeekBarWan, false, 0);

		setSeekWhenAddOrSub(ssc_qian_add, mSeekBarQian, true, 1);
		setSeekWhenAddOrSub(ssc_qian_subtract, mSeekBarQian, false, 1);

		setSeekWhenAddOrSub(ssc_bai_add, mSeekBarBai, true, 2);
		setSeekWhenAddOrSub(ssc_bai_subtract, mSeekBarBai, false, 2);

		setSeekWhenAddOrSub(ssc_shi_add, mSeekBarShi, true, 3);
		setSeekWhenAddOrSub(ssc_shi_subtract, mSeekBarShi, false, 3);

		setSeekWhenAddOrSub(ssc_ge_add, mSeekBarGe, true, 4);
		setSeekWhenAddOrSub(ssc_ge_subtract, mSeekBarGe, false, 4);

		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		RelativeLayout layoutWan = (RelativeLayout) findViewById(R.id.ssc_wan_jixuan_layout);

		RelativeLayout layoutQian = (RelativeLayout) findViewById(R.id.ssc_qian_jixuan_layout);

		RelativeLayout layoutBai = (RelativeLayout) findViewById(R.id.ssc_bai_jixuan_layout);

		RelativeLayout layoutShi = (RelativeLayout) findViewById(R.id.ssc_shi_jixuan_layout);

		// 根据玩法隐藏为数
		switch (iWhichLayout) {

		case PublicConst.SSC_FIVE_STAR:
			setTitle(R.string.ssc_five_five_dialog_title);
			break;
		case PublicConst.SSC_THREE_STAR:
			layoutWan.setVisibility(RelativeLayout.GONE);
			layoutQian.setVisibility(RelativeLayout.GONE);
			setTitle(R.string.ssc_five_three_dialog_title);
			break;
		case PublicConst.SSC_TWO_STAR:
			layoutWan.setVisibility(RelativeLayout.GONE);
			layoutQian.setVisibility(RelativeLayout.GONE);
			layoutBai.setVisibility(RelativeLayout.GONE);
			setTitle(R.string.ssc_five_two_dialog_title);
			break;
		case PublicConst.SSC_ONE_STAR:
			layoutWan.setVisibility(RelativeLayout.GONE);
			layoutQian.setVisibility(RelativeLayout.GONE);
			layoutBai.setVisibility(RelativeLayout.GONE);
			layoutShi.setVisibility(RelativeLayout.GONE);
			setTitle(R.string.ssc_five_one_dialog_title);
			break;
		case PublicConst.SSC_TWO_STAR_ZU_XUAN:
			layoutWan.setVisibility(RelativeLayout.GONE);
			layoutQian.setVisibility(RelativeLayout.GONE);
			layoutBai.setVisibility(RelativeLayout.GONE);
			layoutShi.setVisibility(RelativeLayout.GONE);
			setTitle(R.string.ssc_two_star_dialog_zu_xuan);
			mSeekBarGe.setMax(7);
			mSeekBarGe.setProgress(2);
			break;
		case PublicConst.SSC_TWO_STAR_FEN_WEI:
			layoutWan.setVisibility(RelativeLayout.GONE);
			layoutQian.setVisibility(RelativeLayout.GONE);
			layoutBai.setVisibility(RelativeLayout.GONE);
			setTitle(R.string.ssc_two_star_dialog_fen_wei);
			break;
		case PublicConst.SSC_TWO_STAR_BAO_DIAN:
			layoutWan.setVisibility(RelativeLayout.GONE);
			layoutQian.setVisibility(RelativeLayout.GONE);
			layoutBai.setVisibility(RelativeLayout.GONE);
			layoutShi.setVisibility(RelativeLayout.GONE);
			setTitle(R.string.ssc_two_star_dialog_bao_dian);
			mSeekBarGe.setMax(19);
			break;
		case PublicConst.SSC_TWO_STAR_HE_ZHI:
			layoutWan.setVisibility(RelativeLayout.GONE);
			layoutQian.setVisibility(RelativeLayout.GONE);
			layoutBai.setVisibility(RelativeLayout.GONE);
			layoutShi.setVisibility(RelativeLayout.GONE);
			setTitle(R.string.ssc_two_star_dialog_he_zhi);
			mSeekBarGe.setMax(19);
			break;
		}
	}

	public void updateDialogTips() {
		switch (iWhichLayout) {
		case PublicConst.SSC_FIVE_STAR:
			if (iProgressWan == 1 && iProgressQian == 1 && iProgressBai == 1
					&& iProgressShi == 1 && iProgressGe == 1) {
				iDialogTip.setText(R.string.ssc_five_dan_dialog_title2);
			} else {
				iDialogTip.setText(R.string.ssc_five_fushi_dialog_title2);
			}

			break;
		case PublicConst.SSC_THREE_STAR:
			if (iProgressBai == 1 && iProgressShi == 1 && iProgressGe == 1) {
				iDialogTip.setText(R.string.ssc_five_dan_dialog_title2);
			} else {
				iDialogTip.setText(R.string.ssc_five_fushi_dialog_title2);
			}
			break;
		case PublicConst.SSC_TWO_STAR:
		case PublicConst.SSC_TWO_STAR_FEN_WEI:
			if (iProgressShi == 1 && iProgressGe == 1) {
				iDialogTip.setText(R.string.ssc_five_dan_dialog_title2);
			} else {
				iDialogTip.setText(R.string.ssc_five_fushi_dialog_title2);
			}
			break;
		case PublicConst.SSC_ONE_STAR:
		case PublicConst.SSC_TWO_STAR_BAO_DIAN:
		case PublicConst.SSC_TWO_STAR_HE_ZHI:
			if (iProgressGe == 1) {
				iDialogTip.setText(R.string.ssc_five_dan_dialog_title2);
			} else {
				iDialogTip.setText(R.string.ssc_five_fushi_dialog_title2);
			}
			break;
		case PublicConst.SSC_TWO_STAR_ZU_XUAN:
			if (iProgressGe == 2) {
				iDialogTip.setText(R.string.ssc_five_dan_dialog_title2);
			} else {
				iDialogTip.setText(R.string.ssc_five_fushi_dialog_title2);
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("-----onClick" + v.getId());
		switch (v.getId()) {
		case R.id.okButton:
			int[] iReturnInts = null;
			switch (iWhichLayout) {
			case PublicConst.SSC_FIVE_STAR:
				iReturnInts = new int[5];
				iReturnInts[0] = iProgressWan;
				iReturnInts[1] = iProgressQian;
				iReturnInts[2] = iProgressBai;
				iReturnInts[3] = iProgressShi;
				iReturnInts[4] = iProgressGe;
				break;
			case PublicConst.SSC_THREE_STAR:
				iReturnInts = new int[3];
				iReturnInts[0] = iProgressBai;
				iReturnInts[1] = iProgressShi;
				iReturnInts[2] = iProgressGe;
				break;
			case PublicConst.SSC_TWO_STAR:
			case PublicConst.SSC_TWO_STAR_FEN_WEI:
				iReturnInts = new int[2];
				iReturnInts[0] = iProgressShi;
				iReturnInts[1] = iProgressGe;
				break;
			case PublicConst.SSC_ONE_STAR:
			case PublicConst.SSC_TWO_STAR_ZU_XUAN:
			case PublicConst.SSC_TWO_STAR_BAO_DIAN:
			case PublicConst.SSC_TWO_STAR_HE_ZHI:
				iReturnInts = new int[1];
				iReturnInts[0] = iProgressGe;
				break;
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		switch (seekBar.getId()) {
		case R.id.ssc_wan_dialog_seekbar:
			if (progress < MIN) {
				progress = MIN;
				seekBar.setProgress(progress);
			}
			iProgressWan = seekBar.getProgress();
			iTVWan.setText("" + iProgressWan);
			updateDialogTips();
			break;
		case R.id.ssc_qian_dialog_seekbar:
			if (progress < MIN) {
				progress = MIN;
				seekBar.setProgress(progress);
			}
			iProgressQian = seekBar.getProgress();
			iTVQian.setText("" + iProgressQian);
			updateDialogTips();
			break;
		case R.id.ssc_bai_dialog_seekbar:
			if (progress < MIN) {
				progress = MIN;
				seekBar.setProgress(progress);
			}
			iProgressBai = seekBar.getProgress();
			iTVBai.setText("" + iProgressBai);
			updateDialogTips();
			break;
		case R.id.ssc_shi_dialog_seekbar:
			if (progress < MIN) {
				progress = MIN;
				seekBar.setProgress(progress);
			}
			iProgressShi = seekBar.getProgress();
			iTVShi.setText("" + iProgressShi);
			updateDialogTips();
			break;
		case R.id.ssc_ge_dialog_seekbar:
			if (progress < MIN) {
				progress = MIN;
				seekBar.setProgress(progress);
			}
			iProgressGe = seekBar.getProgress();
			iTVGe.setText("" + iProgressGe);
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

	/**
	 * whichGroup 判断当前是哪个seekbar 0是红球胆码或者红球 1是篮球 2是红球拖码
	 * 
	 * 
	 */
	private void setSeekWhenAddOrSub(ImageButton imageButton,
			final SeekBar seekBar, final boolean isAdd, final int whichGroup) {
		imageButton.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (whichGroup) {
				case 0:
					if (isAdd) {
						iProgressWan++;
						if (iProgressWan > MAX)
							iProgressWan = MAX;
						seekBar.setProgress(iProgressWan);
					} else {
						iProgressWan--;
						if (iProgressWan < MIN)
							iProgressWan = MIN;
						seekBar.setProgress(iProgressWan);
					}
					break;
				case 1:
					if (isAdd) {
						iProgressQian++;
						if (iProgressQian > MAX)
							iProgressQian = MAX;
						seekBar.setProgress(iProgressQian);
					} else {
						iProgressQian--;
						if (iProgressQian < MIN)
							iProgressQian = MIN;
						seekBar.setProgress(iProgressQian);
					}
					break;
				case 2:
					if (isAdd) {
						iProgressBai++;
						if (iProgressBai > MAX)
							iProgressBai = MAX;
						seekBar.setProgress(iProgressBai);
					} else {
						iProgressBai--;
						if (iProgressBai < MIN)
							iProgressBai = MIN;
						seekBar.setProgress(iProgressBai);
					}
					break;
				case 3:
					if (isAdd) {
						iProgressShi++;
						if (iProgressShi > MAX)
							iProgressShi = MAX;
						seekBar.setProgress(iProgressShi);
					} else {
						iProgressShi--;
						if (iProgressShi < MIN)
							iProgressShi = MIN;
						seekBar.setProgress(iProgressShi);
					}
					break;
				case 4:
					switch (iWhichLayout) {
					case PublicConst.SSC_TWO_STAR_BAO_DIAN:
					case PublicConst.SSC_TWO_STAR_HE_ZHI:
						if (isAdd) {
							iProgressGe++;
							if (iProgressGe > 19)
								iProgressGe = 19;
							seekBar.setProgress(iProgressGe);
						} else {
							iProgressGe--;
							if (iProgressGe < MIN)
								iProgressGe = MIN;
							seekBar.setProgress(iProgressGe);
						}
						break;
					case PublicConst.SSC_TWO_STAR_ZU_XUAN:
						if (isAdd) {
							iProgressGe++;
							if (iProgressGe > 7)
								iProgressGe = 7;
							seekBar.setProgress(iProgressGe);
						} else {
							iProgressGe--;
							if (iProgressGe < 2)
								iProgressGe = 2;
							seekBar.setProgress(iProgressGe);
						}
						break;
					default:
						if (isAdd) {
							iProgressGe++;
							if (iProgressGe > MAX)
								iProgressGe = MAX;
							seekBar.setProgress(iProgressGe);
						} else {
							iProgressGe--;
							if (iProgressGe < MIN)
								iProgressGe = MIN;
							seekBar.setProgress(iProgressGe);
						}
					}

					break;
				}
			}
		});

	}
}
