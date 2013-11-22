package com.ruyicai.activity.buy.zixuan;

import java.util.Vector;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.nmk3.Nmk3HeZhiActivity;
import com.ruyicai.activity.buy.nmk3.Nmk3ThreeDiffActivity;
import com.ruyicai.activity.buy.nmk3.Nmk3ThreeLinkActivity;
import com.ruyicai.activity.buy.nmk3.Nmk3ThreeSameActivty;
import com.ruyicai.activity.buy.nmk3.Nmk3TwoDiffActivity;
import com.ruyicai.activity.buy.nmk3.Nmk3TwoSameActivty;
import com.ruyicai.activity.buy.nmk3.NmkAnimation;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

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
	
	//...2013.10.30秘青强
	private NmkAnimation animation;
	OneBallView oneBallView;
	private Vector<OneBallView> ballViewVector = new Vector<OneBallView>();
	private static Vector<OneBallView> ballViewVector2 = new Vector<OneBallView>();

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
				// TODO Auto-generated method stub
				dialogOnclick();
			}
		});
		jxDwBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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

			// if (width == 240) {
			// btn.setTextSize(PublicMethod.getPxInt(20, activity));
			// } else {
			// btn.setTextSize(PublicMethod.getPxInt(11, activity));
			// }

			/** add by pengcx 20130802 start */
			int width = PublicMethod.getDisplayWidth(activity);
			float scale = 480.0f / width;
			float textSize = 11 * scale;
			btn.setTextSize(PublicMethod.getPxInt(textSize, activity));
			/** add by pengcx 20130802 end */

			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					choseNum = Integer.parseInt(btn.getText().toString());
					dialogOnclick();
					initBtn();
					if(popupwindow != null && popupwindow.isShowing()){
						popupwindow.dismiss();
					}
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
	
	int[] iBallId;
	
	public void setSelectBall() {
		for (int i = 0; i < iBallId.length; i++) {
			activity.isBallTable(iBallId[i]);
		}
		ballViewVector.removeAllElements();
		activity.showEditText();
	}
	
	/**
	 * 快三中不同的玩法选择不同数目的随机数
	 * @return
	 */
	private int chooseRandomNum(){
		if(activity instanceof Nmk3HeZhiActivity
				||activity instanceof Nmk3ThreeSameActivty
				||activity instanceof Nmk3TwoSameActivty){
			if(((ZixuanAndJiXuan)activity).highttype.equals("NMK3-TWOSAME-DAN")){
				choseNum = 2;
			}else{
				choseNum=1;
			}	
		}
		if(activity instanceof Nmk3ThreeDiffActivity){
			choseNum=3;
		}
		if(activity instanceof Nmk3TwoDiffActivity){
			choseNum=2;
		}
		return choseNum;
	}

	public void dialogOnclick() {
		if (activity instanceof Nmk3HeZhiActivity
				|| activity instanceof Nmk3ThreeDiffActivity
				|| activity instanceof Nmk3ThreeSameActivty
				|| activity instanceof Nmk3TwoDiffActivity
				|| activity instanceof Nmk3TwoSameActivty) {
			if (animation.flag) {
				activity.again(areaId);
				iBallId = table.randomChooseId(chooseRandomNum());
				for (int i = 0; i < iBallId.length; i++) {
					ballViewVector.add((OneBallView) table.getBallViews().get(
							iBallId[i]));
				}
				initAnimation(ballViewVector, this);
			}
		}
		else{
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
		
	}
	
	/**
	 * 摇骰子动画
	 * @param jiXuanBtn 
	 * @param activity2 
	 * @param table2 
	 */
	private void initAnimation(Vector<OneBallView> ballViewVector, JiXuanBtn jiXuanBtn){
		ImageView huaLanView=(ImageView)activity.findViewById(R.id.nmk_shaizihualan);
	    ImageView shaiZiFirst=(ImageView)activity.findViewById(R.id.nmk_shaizi1);
	    ImageView shaiZiSecond=(ImageView)activity.findViewById(R.id.nmk_shaizi2);
	    ImageView shaiZiThird=(ImageView)activity.findViewById(R.id.nmk_shaizi3);
		huaLanView.setVisibility(View.VISIBLE);
		if(chooseRandomNum()==2){
			shaiZiThird.setVisibility(View.INVISIBLE);
		}
		animation=new NmkAnimation(activity, jiXuanBtn,shaiZiFirst, shaiZiSecond, shaiZiThird, ballViewVector,huaLanView);

	}
	
	private void initAnimation(Vector<OneBallView> ballViewVector, JiXuanBtn jiXuanBtn,int i,int[] iHighlightBallId,BaseActivity activity){
		ImageView huaLanView=(ImageView)activity.findViewById(R.id.nmk_shaizihualan);
	    ImageView shaiZiFirst=(ImageView)activity.findViewById(R.id.nmk_shaizi1);
	    ImageView shaiZiSecond=(ImageView)activity.findViewById(R.id.nmk_shaizi2);
	    ImageView shaiZiThird=(ImageView)activity.findViewById(R.id.nmk_shaizi3);
		huaLanView.setVisibility(View.VISIBLE);
		if(chooseRandomNum()==2){
			shaiZiThird.setVisibility(View.INVISIBLE);
		}
		animation=new NmkAnimation(activity, jiXuanBtn,shaiZiFirst, shaiZiSecond, shaiZiThird, ballViewVector,huaLanView,i,iHighlightBallId);

	}

	public void onclickText(int i,int[] iHighlightBallId) {
		activity.showEditText();
		if(!(activity instanceof Nmk3TwoSameActivty)){
			activity.setAllBall(i, iHighlightBallId);
		}
		if (activity instanceof Dlc) {
			((Dlc) activity).showBetInfo("");
		} else if (activity instanceof ZixuanAndJiXuan) {
			if (activity instanceof Nmk3TwoSameActivty) {
				if (animation.flag) {
					activity.again(areaId);
					if(i == 0){
						ballViewVector2.clear();
					}
					ballViewVector2.add((OneBallView) table.getBallViews().get(
							iHighlightBallId[i]));
					if(i == 1){
						initAnimation(ballViewVector2, this,i,iHighlightBallId,activity);
					}	
				}
			}
			((ZixuanAndJiXuan) activity).showBetInfo("");
		} else {
			activity.changeTextSumMoney();
		}
	}

}
