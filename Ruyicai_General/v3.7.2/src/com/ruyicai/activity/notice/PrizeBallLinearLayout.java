package com.ruyicai.activity.notice;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicMethod;

/**
 * 初始化开奖号码小球
 * 
 * @author miao
 * 
 */
public class PrizeBallLinearLayout {

	Context context;
	int BALL_WIDTH = 46;
	public LinearLayout linear = null;
	public String Lotname = "";
	public String Batchcode = "";
	private static int[] aRedColorResId = { R.drawable.red };
	private static int[] aBlueColorResId = { R.drawable.blue };

	public PrizeBallLinearLayout() {
		// TODO Auto-generated constructor stub
	}

	public PrizeBallLinearLayout(Context context) {
		this.context = context;
		BALL_WIDTH = NoticeMainActivity.BALL_WIDTH;
		// TODO Auto-generated constructor stub
	}

	public PrizeBallLinearLayout(Context context, int ballwidth) {
		this.context = context;
		BALL_WIDTH = ballwidth;
	}

	public PrizeBallLinearLayout(Context context, LinearLayout linear,
			String Lotname, String Batchcode) {
		this.context = context;
		BALL_WIDTH = NoticeMainActivity.BALL_WIDTH;

		this.linear = linear;
		this.Lotname = Lotname;
		this.Batchcode = Batchcode;
		// TODO Auto-generated constructor stub
	}

	public void initLinear() {
		if (Lotname.equalsIgnoreCase(Constants.LOTNO_SSQ)) {
			initSSQBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_FC3D)) {
			init3DBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_QLC)) {
			initQLCBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_PL3)) {
			initPL3BallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_PL5)) {
			initPL5BallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_QXC)) {
			initQXCBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_DLT)) {
			initDltBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_SSC)) {
			initSSCBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_11_5)) {
			init115BallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_eleven)) {
			init11ydjBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_22_5)) {
			initTwentyjBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_SFC)) {
			initSFCBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_RX9)) {
			initSFCBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_LCB)) {
			initLCBBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_JQC)) {
			initJQCBallLinear();
		} else if (Lotname.equalsIgnoreCase(Constants.LOTNO_GD115)) {
			initSFCBallLinear();
		}

	}

	/**
	 * <b>初始化双色球中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String initSSQBallLinear() {
		// zlm 7.28 代码修改：添加号码排序
		StringBuffer str = new StringBuffer();// 用于分享显示开奖号码
		int i1, i2, i3;
		String iShowNumber;
		OneBallView tempBallView;
		int[] ssqInt01 = new int[6];
		int[] ssqInt02 = new int[6];
		String[] ssqStr = new String[6];

		for (i2 = 0; i2 < 6; i2++) {
			iShowNumber = Batchcode.substring(i2 * 2, i2 * 2 + 2);
			ssqInt01[i2] = Integer.valueOf(iShowNumber);
		}

		ssqInt02 = PublicMethod.sort(ssqInt01);

		for (i3 = 0; i3 < 6; i3++) {
			if (ssqInt02[i3] < 10) {
				ssqStr[i3] = "0" + ssqInt02[i3];
			} else {
				ssqStr[i3] = "" + ssqInt02[i3];
			}
		}
		for (i1 = 0; i1 < 6; i1++) {
			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ssqStr[i1],
					aRedColorResId);
			linear.addView(tempBallView);
			str.append(ssqStr[i1] + ",");
		}
		str.substring(0, str.length() - 1);
		str.append("|");
		iShowNumber = Batchcode.substring(12, 14);
		str.append(iShowNumber);
		tempBallView = new OneBallView(context, 1);
		tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
				aBlueColorResId);
		linear.addView(tempBallView);
		return str.toString();
	}

	/**
	 * <b>初始化大乐透中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String initDltBallLinear() {
		StringBuffer str = new StringBuffer();// 用于分享显示开奖号码
		int i1, i2, i3;
		String iShowNumber;
		OneBallView tempBallView;
		int[] cjdltInt01 = new int[5];
		int[] cjdltInt02 = new int[5];
		int[] cjdltInt03 = new int[2];
		int[] cjdltInt04 = new int[2];
		String[] cjdltStr = new String[5];
		String[] cjdltStr1 = new String[2];

		for (i2 = 0; i2 < 5; i2++) {
			iShowNumber = Batchcode.substring(i2 * 3, i2 * 3 + 2);
			cjdltInt01[i2] = Integer.valueOf(iShowNumber);
		}

		cjdltInt02 = PublicMethod.sort(cjdltInt01);

		for (i3 = 0; i3 < 5; i3++) {
			if (cjdltInt02[i3] < 10) {
				cjdltStr[i3] = "0" + cjdltInt02[i3];
			} else {
				cjdltStr[i3] = "" + cjdltInt02[i3];
			}
		}
		for (i1 = 0; i1 < 5; i1++) {

			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, cjdltStr[i1],
					aRedColorResId);
			str.append(cjdltStr[i1]);
			str.append(",");
			linear.addView(tempBallView);
		}

		try {
			for (i2 = 0; i2 < 2; i2++) {
				iShowNumber = Batchcode.substring(i2 * 3 + 15, i2 * 3 + 17);
				cjdltInt03[i2] = Integer.valueOf(iShowNumber);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		cjdltInt04 = PublicMethod.sort(cjdltInt03);

		for (i3 = 0; i3 < 2; i3++) {
			if (cjdltInt04[i3] < 10) {
				cjdltStr1[i3] = "0" + cjdltInt04[i3];
			} else {
				cjdltStr1[i3] = "" + cjdltInt04[i3];
			}
		}
		str.substring(0, str.length() - 1);
		str.append("|");
		for (i1 = 0; i1 < 2; i1++) {
			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, cjdltStr1[i1],
					aBlueColorResId);
			str.append(cjdltStr1[i1]);
			str.append(",");
			linear.addView(tempBallView);
		}
		str.substring(0, str.length() - 1);
		return str.toString();
	}

	/**
	 * <b>初始化福彩3D
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String init3DBallLinear() {
		StringBuffer str = new StringBuffer();// 用于分享显示开奖号码
		int i1;
		// zlm 7.30 代码修改：修改福彩3D号码
		int iShowNumber;
		OneBallView tempBallView;
		for (i1 = 0; i1 < 3; i1++) {
			iShowNumber = Integer.valueOf(Batchcode.substring(i1 * 2,
					i1 * 2 + 2));
			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, "" + iShowNumber,
					aRedColorResId);
			linear.addView(tempBallView);
			str.append(iShowNumber);
			str.append(",");
		}
		str.substring(0, str.length() - 1);
		return str.toString();
	}

	/**
	 * <b>初始化足彩
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String initZCBallLinear() {
		TextView tvFootball = new TextView(context);
		tvFootball.setTextColor(R.color.darkgreen);
		tvFootball.setTextSize(25);
		tvFootball.setGravity(Gravity.RIGHT);
		tvFootball.setText(Batchcode);
		linear.addView(tvFootball);
		return Batchcode;
	}

	/**
	 * <b>初始化排列五中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String initPL5BallLinear() {
		StringBuffer str = new StringBuffer();// 用于分享显示开奖号码
		int i1;
		int iShowNumber;
		OneBallView tempBallView;

		for (i1 = 0; i1 < 5; i1++) {
			iShowNumber = Integer.valueOf(Batchcode.substring(i1, i1 + 1));
			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, "" + iShowNumber,
					aRedColorResId);
			linear.addView(tempBallView);
			str.append(iShowNumber);
			str.append(",");
		}
		str.substring(0, str.length() - 1);
		return str.toString();
	}

	/**
	 * <b>初始化时时彩中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public void initSSCBallLinear() {
		int i1;
		int iShowNumber;
		OneBallView tempBallView;
		for (i1 = 0; i1 < 5; i1++) {
			iShowNumber = Integer.valueOf(Batchcode.substring(i1, i1 + 1));
			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, "" + iShowNumber,
					aRedColorResId);
			linear.addView(tempBallView);
		}
	}

	/**
	 * <b>初始化七星彩中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String initQXCBallLinear() {
		StringBuffer str = new StringBuffer();// 用于分享显示开奖号码
		int i1;
		int iShowNumber;
		OneBallView tempBallView;

		for (i1 = 0; i1 < 7; i1++) {
			iShowNumber = Integer.valueOf(Batchcode.substring(i1, i1 + 1));
			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, "" + iShowNumber,
					aRedColorResId);
			linear.addView(tempBallView);
			str.append(iShowNumber);
			str.append(",");
		}
		str.substring(0, str.length() - 1);
		return str.toString();
	}

	/**
	 * <b>初始化11选5中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public void init115BallLinear() {
		int i1;
		int iShowNumber;
		OneBallView tempBallView;
		for (i1 = 0; i1 < 5; i1++) {
			iShowNumber = Integer.valueOf(Batchcode.substring(i1 * 2,
					i1 * 2 + 2));
			String isNum = PublicMethod.getZhuMa(iShowNumber);
			tempBallView = new OneBallView(context, 1);
			tempBallView
					.initBall(BALL_WIDTH, BALL_WIDTH, isNum, aRedColorResId);
			linear.addView(tempBallView);
		}
	}

	/**
	 * <b>初始化22选5中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String initTwentyjBallLinear() {
		StringBuffer str = new StringBuffer();// 用于分享显示开奖号码
		int i1;
		int iShowNumber;
		OneBallView tempBallView;
		for (i1 = 0; i1 < 5; i1++) {
			iShowNumber = Integer.valueOf(Batchcode.substring(i1 * 2,
					i1 * 2 + 2));
			String isNum = PublicMethod.getZhuMa(iShowNumber);
			tempBallView = new OneBallView(context, 1);
			tempBallView
					.initBall(BALL_WIDTH, BALL_WIDTH, isNum, aRedColorResId);
			linear.addView(tempBallView);
			str.append(iShowNumber);
			str.append(",");
		}
		str.substring(0, str.length() - 1);
		return str.toString();
	}

	public void init11ydjBallLinear() {
		int i1;
		int iShowNumber;
		OneBallView tempBallView;
		for (i1 = 0; i1 < 5; i1++) {
			iShowNumber = Integer.valueOf(Batchcode.substring(i1 * 2,
					i1 * 2 + 2));
			String isNum = PublicMethod.getZhuMa(iShowNumber);
			tempBallView = new OneBallView(context, 1);
			tempBallView
					.initBall(BALL_WIDTH, BALL_WIDTH, isNum, aRedColorResId);
			linear.addView(tempBallView);
		}
	}

	/**
	 * <b>初始化排列3中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String initPL3BallLinear() {
		StringBuffer str = new StringBuffer();// 用于分享显示开奖号码
		int i1;
		int iShowNumber;
		OneBallView tempBallView;

		for (i1 = 0; i1 < 3; i1++) {
			iShowNumber = Integer.valueOf(Batchcode.substring(i1, i1 + 1));
			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, "" + iShowNumber,
					aRedColorResId);
			linear.addView(tempBallView);
			str.append(iShowNumber);
			str.append(",");
		}
		str.substring(0, str.length() - 1);
		return str.toString();
	}

	/**
	 * <b>初始化六场半中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public void initLCBBallLinear() {

		TextView tvFootball = new TextView(context);
		tvFootball.setTextColor(R.color.darkgreen);
		tvFootball.setTextSize(25);
		tvFootball.setGravity(Gravity.RIGHT);
		tvFootball.setText(Batchcode);
		linear.addView(tvFootball);
	}

	/**
	 * <b>初始化胜负彩中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public void initSFCBallLinear() {
		TextView tvFootball = new TextView(context);
		tvFootball.setTextColor(R.color.darkgreen);
		tvFootball.setTextSize(25);
		tvFootball.setGravity(Gravity.RIGHT);
		tvFootball.setText(Batchcode);
		linear.addView(tvFootball);
	}

	/**
	 * <b>初始化进球彩中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public void initJQCBallLinear() {
		TextView tvFootball = new TextView(context);
		tvFootball.setTextColor(R.color.darkgreen);
		tvFootball.setTextSize(25);
		tvFootball.setGravity(Gravity.RIGHT);
		tvFootball.setText(Batchcode);
		linear.addView(tvFootball);

	}

	/**
	 * <b>初始化七乐彩中奖小球
	 * 
	 * @param linear
	 * @param Lotname
	 * @param Batchcode
	 */
	public String initQLCBallLinear() {
		// zlm 7.28 代码修改：添加号码排序
		StringBuffer str = new StringBuffer();// 用于分享显示开奖号码
		int i1, i2, i3;
		String iShowNumber;
		OneBallView tempBallView;

		int[] ssqInt01 = new int[7];
		int[] ssqInt02 = new int[7];
		String[] ssqStr = new String[7];

		for (i2 = 0; i2 < 7; i2++) {
			iShowNumber = Batchcode.substring(i2 * 2, i2 * 2 + 2);
			ssqInt01[i2] = Integer.valueOf(iShowNumber);
		}

		ssqInt02 = PublicMethod.sort(ssqInt01);

		for (i3 = 0; i3 < 7; i3++) {
			if (ssqInt02[i3] < 10) {
				ssqStr[i3] = "0" + ssqInt02[i3];
			} else {
				ssqStr[i3] = "" + ssqInt02[i3];
			}
		}
		for (i1 = 0; i1 < 7; i1++) {

			tempBallView = new OneBallView(context, 1);
			tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ssqStr[i1],
					aRedColorResId);

			linear.addView(tempBallView);
			str.append(ssqStr[i1]);
			str.append(",");
		}
		// zlm 8.3 代码修改 ：添加七乐彩蓝球
		str.substring(0, str.length() - 1);
		iShowNumber = Batchcode.substring(14, 16);
		tempBallView = new OneBallView(context, 1);
		tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
				aBlueColorResId);

		linear.addView(tempBallView);
		str.append(iShowNumber);
		return str.toString();
	}

	/**
	 * 清空小球
	 * 
	 */
	public void removeAllBalls() {
		linear.removeAllViews();
	}

}
