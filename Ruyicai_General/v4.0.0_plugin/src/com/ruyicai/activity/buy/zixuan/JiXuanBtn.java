package com.ruyicai.activity.buy.zixuan;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 机选类
 * 
 * @author win
 * 
 */
public class JiXuanBtn {
	private boolean isRed = true;
	private Button jxDwBtn;
	private Button jxBtn;
	private int sizeNum;
	private PopupWindow popupwindow;
	private View parentView;
	public BallTable table;
	private int areaMin;
	private int choseNum;
	private final int LINE_NUM = 8;
	private int width, padding, leftPadd;
	private int areaId;
	BaseActivity activity;

	public JiXuanBtn(boolean isRed, Button jxBtn, Button jxDwBtn, int sizeNum,
			BaseActivity activity, View view, BallTable table, int areaMin,
			int areaId) {
		this.isRed = isRed;
		this.jxBtn = jxBtn;
		this.jxDwBtn = jxDwBtn;
		this.sizeNum = sizeNum;
		this.activity = activity;
		this.parentView = view;
		this.table = table;
		this.areaMin = areaMin;
		this.choseNum = areaMin;
		this.areaId = areaId;
		initBtnWidth();
		initBtn();
		onClickBtn();
	}

	public void initBtnWidth() {
		width = PublicMethod.getPxInt(30, activity);
		padding = PublicMethod.getPxInt(4, activity);
		leftPadd = (PublicMethod.getDisplayWidth(activity) - LINE_NUM
				* (width + 2 * padding)) / 2;
	}

	public void initBtn() {
		jxBtn.setText("机选号码");
		jxDwBtn.setText(choseNum + "个");
	}

	private void onClickBtn() {
		jxBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialogOnclick();
			}
		});
		jxDwBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				} else {
					createDialog();
				}
			}
		});
	}

	private void createDialog() {
		LayoutInflater inflate = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = (LinearLayout) inflate.inflate(
				R.layout.buy_jx_btn_window, null);
		LinearLayout layout = (LinearLayout) popupView
				.findViewById(R.id.buy_jx_btn_layout);
		popupwindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupwindow.setTouchable(true); // 设置PopupWindow可触摸
		popupwindow.setOutsideTouchable(true);
		popupView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		addBtn(layout, areaMin, sizeNum);
		popupwindow.showAsDropDown(jxBtn);
	}

	private void addBtn(LinearLayout layoutView, int startNum, int sizeNum) {
		int allNum = sizeNum - startNum + 1;
		int line = allNum / LINE_NUM;
		int lastLineNum = allNum % LINE_NUM;
		int lineStart = startNum - 1;
		for (int i = 0; i < line; i++) {
			lineStart = initLine(layoutView, lineStart, LINE_NUM, true);
		}
		if (lastLineNum > 0 && line == 0) {
			lineStart = initLine(layoutView, lineStart, lastLineNum, true);
		} else if ((lastLineNum > 0)) {
			lineStart = initLine(layoutView, lineStart, lastLineNum, false);
		}
	}

	private int initLine(LinearLayout layoutView, int startNum, int lineNum,
			boolean isCenter) {
		LinearLayout layout = new LinearLayout(activity);
		for (int j = 0; j < lineNum; j++) {
			final Button btn = new Button(activity);
			btn.setBackgroundResource(R.drawable.buy_jx_btn_bg_selector);
			startNum++;
			btn.setText(startNum + "");
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(padding, padding, padding, padding);
			btn.setWidth(width);
			btn.setHeight(width);

			/** add by pengcx 20130802 start */
			int width = PublicMethod.getDisplayWidth(activity);
			float scale = 480.0f / width;
			float textSize = 11 * scale;
			btn.setTextSize(PublicMethod.getPxInt(textSize, activity));
			/** add by pengcx 20130802 end */

			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					choseNum = Integer.parseInt(btn.getText().toString());
					dialogOnclick();
					initBtn();
					popupwindow.dismiss();
				}
			});
			layout.addView(btn, param);
		}
		if (isCenter) {
			layout.setGravity(Gravity.CENTER_HORIZONTAL);
			layoutView.addView(layout);
		} else {
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			param.setMargins(leftPadd, 0, 0, 0);
			layoutView.addView(layout, param);
		}
		layoutView.setGravity(Gravity.CENTER_VERTICAL);
		return startNum;
	}

	public void dialogOnclick() {
		activity.again(areaId);
		int[] iBallId = table.randomChooseId(choseNum);
		for (int i = 0; i < iBallId.length; i++) {
			activity.isBallTable(iBallId[i]);
		}
		activity.showEditText();
		if (activity instanceof Dlc) {
			((Dlc) activity).showBetInfo("");
		} else if (activity instanceof ZixuanAndJiXuan) {

			((ZixuanAndJiXuan) activity).showBetInfo("");
		} else {
			activity.changeTextSumMoney();
		}

	}

	public void onclickText() {
		activity.showEditText();
		if (activity instanceof Dlc) {
			((Dlc) activity).showBetInfo("");
		} else if (activity instanceof ZixuanAndJiXuan) {
			((ZixuanAndJiXuan) activity).showBetInfo("");
		} else {
			activity.changeTextSumMoney();
		}
	}

}
