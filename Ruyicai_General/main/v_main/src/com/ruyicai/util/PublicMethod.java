package com.ruyicai.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView.HitTestResult;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.account.DirectPayActivity;
import com.ruyicai.activity.buy.cq11x5.Cq11Xuan5;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.dlt.Dlt;
import com.ruyicai.activity.buy.eleven.Eleven;
import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.activity.buy.gdeleven.GdEleven;
import com.ruyicai.activity.buy.jc.lq.LqMainActivity;
import com.ruyicai.activity.buy.jc.zq.ZqMainActivity;
import com.ruyicai.activity.buy.nmk3.Nmk3Activity;
import com.ruyicai.activity.buy.pl3.PL3;
import com.ruyicai.activity.buy.pl5.PL5;
import com.ruyicai.activity.buy.qlc.Qlc;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.buy.ten.TenActivity;
import com.ruyicai.activity.buy.twentytwo.TwentyTwo;
import com.ruyicai.activity.buy.zc.FootBallMainActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.net.newtransaction.SoftwareUpdateInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
/*Add by fansm 20130412 start*/
/*add debug switch*/
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.MyHandler;
/*Add by fansm 20130412 end*/
import com.umeng.analytics.MobclickAgent;

/**
 * 共用方法类
 */

public class PublicMethod {
	public final static int MAXICON = 8;
	public static int icons[] = { R.drawable.crown, R.drawable.cup,
			R.drawable.diamond, R.drawable.star };
	public static int grayIcons[] = { R.drawable.crown_gray,
			R.drawable.cup_gray, R.drawable.diamond_gray, R.drawable.star_gray };
	public static int nums[] = { R.drawable.one, R.drawable.two,
			R.drawable.three, R.drawable.four };
	/* Add by fansm 20130412 start */
	private static String CLASSNAME = "className";
	private static String METHODNAME = "methodName";

	/* Add by fansm 20130412 end */

	/**
	 * 求组合
	 * 
	 * @param m
	 *            每一注小球个数
	 * @param n
	 *            真实小球个数(即点击小球的个数)
	 * @return
	 */
	public static long zuhe(int m, int n) {
		long t_a = 0L;
		long total = 1L;
		int temp = n;
		for (int i = 0; i < m; i++) {
			total = total * temp;
			temp--;
		}
		t_a = total / jiec(m);
		return t_a;
	}

	/**
	 * 求阶乘
	 * 
	 * @param a
	 * @return
	 */
	public static long jiec(int a) {
		long t_a = 1L;
		for (long i = 1; i <= a; i++) {
			t_a = t_a * i;
		}
		return t_a;
	}

	/**
	 * 获取 单个随机数
	 */
	static Random random = new Random();

	public static int getRandomByRange(int aFrom, int aTo) {
		return (random.nextInt() >>> 1) % (aTo - aFrom + 1) + aFrom;
	}

	/**
	 * 检查数组碰撞
	 * 
	 * @param aNums
	 * @param aTo
	 * @param aCheckNum
	 * @return
	 */
	public static boolean checkCollision(int[] aNums, int aTo, int aCheckNum) {
		boolean returnValue = false;
		for (int i = 0; i < aTo; i++) {
			if (aNums[i] == aCheckNum) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	/**
	 * 检查数组碰撞
	 * 
	 * @param aNums
	 * @param aTo
	 * @param aCheckNum
	 * @return
	 */
	public static boolean checkCollision(String[] aStr, int aTo,
			String aCheckStr) {
		boolean returnValue = false;
		for (int i = 0; i < aTo; i++) {
			if (aStr[i] == aCheckStr) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	/**
	 * 获取 多个随机数
	 * 
	 * @param aNum
	 * @param aFrom
	 * @param aTo
	 * @return
	 */
	public static int[] getRandomsWithoutCollision(int aNum, int aFrom, int aTo) {
		int[] iReturnNums = new int[aNum];
		for (int i = 0; i < aNum; i++) {
			int iCurrentNum = getRandomByRange(aFrom, aTo);
			while (checkCollision(iReturnNums, i, iCurrentNum)) {
				iCurrentNum = getRandomByRange(aFrom, aTo);
			}
			iReturnNums[i] = iCurrentNum;
		}
		return iReturnNums;
	}

	/**
	 * 获取多个随机数
	 * 
	 * @param aRandomNums
	 * @param list
	 * @return int[]
	 */
	public static String[] getRandomsWithoutCollision(int aRandomNums,
			List<String> list) {
		String deta[] = new String[aRandomNums];
		for (int i = 0; i < aRandomNums; i++) {
			int randomIndex = PublicMethod.getRandomByRange(0, list.size() - 1);
			String randomStr = list.get(randomIndex);
			while (PublicMethod.checkCollision(deta, i, randomStr)) {
				randomIndex = PublicMethod.getRandomByRange(0, list.size() - 1);
			}
			deta[i] = list.get(randomIndex);
		}
		return deta;
	}

	/**
	 * 获取当前页面的屏幕高度
	 * 
	 * @param cx
	 * @return
	 */
	public static int getDisplayHeight(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * 获取当前页面的屏幕宽度
	 * 
	 * @param cx
	 * @return
	 */
	public static int getDisplayWidth(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;

		return screenWidth;
	}

	/**
	 * 修改余额格式
	 * 
	 * @param str
	 * @return
	 */
	public static String changeMoney(String str) {
		if (str.length() > 2) {
			if (str.substring(str.length() - 2, str.length()).equals("00")) {
				str = str.substring(0, str.length() - 2);
			} else {
				str = str.substring(0, str.length() - 2) + "."
						+ str.substring(str.length() - 2, str.length());
			}
		} else if (str.length() == 2) {
			str = "0" + "." + str;
		} else if (str.length() == 1) {
			str = "0.0" + str;
		}
		return str;
	}

	/**
	 * 发短信
	 * 
	 * @param phoneNumber
	 * @param message
	 * @return
	 */
	public static boolean sendSMS(String phoneNumber, String message) {
		try {
			SmsManager sms = SmsManager.getDefault();
			List<String> iContents = sms.divideMessage(message);
			for (int i = 0; i < iContents.size(); i++)
				sms.sendTextMessage(phoneNumber, null, iContents.get(i), null,
						null);

		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	public static void myOutLog(String tag, String msg) {
		Log.e(tag, msg);
	}

	/* Modify by fansm 20130412 start */
	/* add log */
	/**
	 * 输出信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void outLog(String className, String methodName) {
		// Log.e(tag, msg);
		Log.d(Constants.TAG, CLASSNAME + " = " + className + "; " + METHODNAME
				+ " = " + methodName);
	}
	/**
	 * 获得栈activity
	 * @param context
	 */
	public static void getActivityFromStack(Activity context) {

		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(100);

		if (runningTaskInfos != null) {
			for (int i = 0; i < runningTaskInfos.size(); i++) {
				Log.d(Constants.TAG,
						(runningTaskInfos.get(i).topActivity).toString() + " TaskId=" + context.getTaskId());
			}
		}
	}

	/**
	 * 输出信息
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void outLog(String className, String methodName, String msg) {
		// Log.e(tag, msg);
		Log.d(Constants.TAG, CLASSNAME + " = " + className + "; " + METHODNAME
				+ " = " + methodName + "; " + "MSG = " + msg);
	}

	/* Modify by fansm 20130412 end */
	/**
	 * 设置listView的间距
	 * 
	 * @param listview
	 */
	public static void setmydividerHeight(ListView listview) {
		listview.setDivider(new ColorDrawable(Color.GRAY));
		listview.setDividerHeight(1);

	}

	/**
	 * 获得当前期和截止时间
	 * 
	 * @param string
	 * @param context
	 * @return
	 */
	public static String[] getLotno(String string, Context context) {

		return null;

	}

	/**
	 * 排序
	 * 
	 * @param nums
	 * @param str
	 * @return
	 */
	public static int[] orderby(int[] nums, String str) {
		// 从大到小排
		if (str.equalsIgnoreCase("cba")) {
			for (int i = 0; i < nums.length; i++) {
				for (int j = i + 1; j < nums.length; j++) {
					if (nums[i] < nums[j]) {
						int tem = nums[i];
						nums[i] = nums[j];
						nums[j] = tem;
					}
				}
			}
		}
		// 从小到大排
		else if (str.equalsIgnoreCase("abc")) {
			for (int i = 0; i < nums.length; i++) {
				for (int j = i + 1; j < nums.length; j++) {
					if (nums[i] > nums[j]) {
						int tem = nums[i];
						nums[i] = nums[j];
						nums[j] = tem;
					}
				}
			}
		}
		return nums;
	}

	/**
	 * 将注码1转换成字符01的方法
	 * 
	 * @param num
	 * @return
	 */
	public static String getZhuMa(int num) {
		String str = "";

		if (num < 10) {
			str = "0" + num;
		} else {
			str = "" + num;
		}
		return str;

	}

	public static String getbigsmalZhumastr(int num) {
		String str = "";
		switch (num) {
		case 1:
			str = "大";// 大
			break;
		case 2:
			str = "小";// 小
			break;
		case 3:
			str = "单";// 单
			break;
		case 4:
			str = "双";// 双
			break;
		}
		return str;
	}

	/**
	 * 数组排序
	 * 
	 * @param t
	 * @return
	 */
	public static int[] sort(int t[]) {
		int t_s[] = t;
		int temp;
		for (int i = 0; i < t_s.length; i++) {
			for (int j = i + 1; j < t_s.length; j++) {
				if (t_s[i] > t_s[j]) {
					temp = t_s[i];
					t_s[i] = t_s[j];
					t_s[j] = temp;
				}
			}
		}
		return t_s;
	}

	/**
	 * 机选提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */

	public static void alertJiXuan(String string, Context context) {
		Builder dialog = new AlertDialog.Builder(context).setTitle("请选择号码")
				.setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		dialog.show();

	}


	// /**
	// * 打开网址
	// * @param cx 本地的context
	// * @param a 网址字符串
	// */
	// public static void openUrlByString(Context cx, String a) {
	// Uri myUri = Uri.parse(a);
	// Intent returnIt = new Intent(Intent.ACTION_VIEW, myUri);
	// cx.startActivity(returnIt);
	// }
	//
	//
	/**
	 * 投注成功后显示Dialog
	 * 
	 * @param context
	 */
	public static void showDialog(final Context context, final String shareCode) {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.touzhu_succe, null);
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
		Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		Button share = (Button) view
				.findViewById(R.id.touzhu_succe_button_share);
		image.setImageResource(R.drawable.succee);
		ok.setBackgroundResource(R.drawable.loginselecter);
		share.setBackgroundResource(R.drawable.loginselecter);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		share.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享给朋友");
				intent.putExtra(Intent.EXTRA_TEXT, "我刚刚使用如意彩手机客户端购买了一张彩票:"
						+ shareCode + "快来参与吧！官网www.ruyicai.com");
				context.startActivity(Intent.createChooser(intent, "请选择分享方式"));
			}
		});

		dialog.show();
		dialog.getWindow().setContentView(view);

	}

	/**
	 * 投注成功后显示Dialog
	 * 
	 * @param context
	 */
	public static void showDialog(final Activity activity) {
		LayoutInflater inflate = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.touzhu_succe, null);
		final AlertDialog dialog = new AlertDialog.Builder(activity).create();
		MobclickAgent.onEvent(activity, "goucaichenggong");// BY贺思明 购买彩票成功的提示
		ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
		Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		Button share = (Button) view
				.findViewById(R.id.touzhu_succe_button_share);
		image.setImageResource(R.drawable.succee);
		share.setVisibility(View.GONE);
		ok.setText("继续购买");
		ok.setBackgroundResource(R.drawable.loginselecter);
		share.setBackgroundResource(R.drawable.loginselecter);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				activity.finish();
			}
		});

		dialog.show();
		dialog.getWindow().setContentView(view);

	}

	/**
	 * 投注成功后显示Dialog
	 * 
	 * @param context
	 */
	public static void showDialog(final Context context) {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.touzhu_succe, null);
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
		Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		Button share = (Button) view
				.findViewById(R.id.touzhu_succe_button_share);
		image.setImageResource(R.drawable.succee);
		ok.setBackgroundResource(R.drawable.loginselecter);
		share.setBackgroundResource(R.drawable.loginselecter);
		share.setVisibility(Button.GONE);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);

	}

	public static void showDialogOfDirectPay(final Context context,
			final BetAndGiftPojo betAndGift) {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.touzhu_succe, null);
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
		Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		Button share = (Button) view
				.findViewById(R.id.touzhu_succe_button_share);
		image.setImageResource(R.drawable.succee);
		ok.setBackgroundResource(R.drawable.loginselecter);
		share.setBackgroundResource(R.drawable.loginselecter);
		share.setVisibility(Button.GONE);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (context instanceof DirectPayActivity) {

					backToTouZhu(context, betAndGift.getLotno().toString());
				}
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);

	}

	public static void backToTouZhu(Context context, String lotNo) {
		Intent intent = null;

		if (lotNo.equals(Constants.LOTNO_SSQ)) {
			intent = new Intent(context, Ssq.class);
		} else if (lotNo.equals(Constants.LOTNO_DLT)) {
			intent = new Intent(context, Dlt.class);
		} else if (lotNo.equals(Constants.LOTNO_FC3D)) {
			intent = new Intent(context, Fc3d.class);
		} else if (lotNo.equals(Constants.LOTNO_11_5)) {
			intent = new Intent(context, Dlc.class);
		} else if (lotNo.equals(Constants.LOTNO_SSC)) {
			intent = new Intent(context, Ssc.class);
		} else if (lotNo.equals(Constants.LOTNO_JCZQ)
				|| lotNo.equals(Constants.LOTNO_JCZQ_BF)
				|| lotNo.equals(Constants.LOTNO_JCZQ_ZQJ)
				|| lotNo.equals(Constants.LOTNO_JCZQ_BQC)) {
			intent = new Intent(context, ZqMainActivity.class);
		} else if (lotNo.equals(Constants.LOTNO_ten)) {
			intent = new Intent(context, TenActivity.class);
		} else if (lotNo.equals(Constants.LOTNO_eleven)) {
			intent = new Intent(context, Eleven.class);
		} else if (lotNo.equals(Constants.LOTNO_GD115)) {
			intent = new Intent(context, GdEleven.class);
		} else if (lotNo.equals(Constants.LOTNO_PL3)) {
			intent = new Intent(context, PL3.class);
		} else if (lotNo.equals(Constants.LOTNO_QLC)) {
			intent = new Intent(context, Qlc.class);
		} else if (lotNo.equals(Constants.LOTNO_22_5)) {
			intent = new Intent(context, TwentyTwo.class);
		} else if (lotNo.equals(Constants.LOTNO_PL5)) {
			intent = new Intent(context, PL5.class);
		} else if (lotNo.equals(Constants.LOTNO_QXC)) {
			intent = new Intent(context, QXC.class);
		} else if (lotNo.equals(Constants.LOTNO_SFC)
				|| lotNo.equals(Constants.LOTNO_RX9)
				|| lotNo.equals(Constants.LOTNO_JQC)
				|| lotNo.equals(Constants.LOTNO_LCB)) {
			intent = new Intent(context, FootBallMainActivity.class);
		} else if (lotNo.equals(Constants.LOTNO_JCLQ)
				|| lotNo.equals(Constants.LOTNO_JCLQ_RF)
				|| lotNo.equals(Constants.LOTNO_JCLQ_SFC)
				|| lotNo.equals(Constants.LOTNO_JCLQ_DXF)) {
			intent = new Intent(context, LqMainActivity.class);
		}else if(lotNo.equals(Constants.LOTNO_CQ_ELVEN_FIVE)){
			intent = new Intent(context,Cq11Xuan5.class);
		}else if(lotNo.equals(Constants.LOTNO_NMK3)){
			intent = new Intent(context,Nmk3Activity.class);
		}

		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	/**
	 * 创建机选Table type 0为时时彩,1位其他
	 */
	public static TableLayout makeBallTableJiXuan(TableLayout tableLayout,
			int aFieldWidth, int[] aResId, int[] iTotalRandoms, Context context) {
		// int iBallNum = aBallNum;
		TableLayout table;
		if (tableLayout == null) {
			table = new TableLayout(context);
		} else {
			table = tableLayout;
		}

		int iBallNum = iTotalRandoms.length;
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int viewNumPerLine = 9;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine
				- 2;

		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);
			for (int col = 0; col < viewNumPerLine; col++) {
				int num = iTotalRandoms[(row * viewNumPerLine) + col];
				String iStrTemp = "" + num;
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				// tempBallView.setOnClickListener(this);
				tempBallView.changeBallColor();
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin + 1, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			table.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(context);
			for (int col = 0; col < lastLineViewNum; col++) {
				int num = iTotalRandoms[iBallViewNo];

				String iStrTemp = "" + num;
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				// tempBallView.setOnClickListener(this);
				tempBallView.changeBallColor();
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin + 1, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			table.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}

		return table;
	}

	/**
	 * 创建机选Table,大小双单专用 type 0为时时彩,1位其他
	 */
	public static TableLayout makeBallTableJiXuanbigsmall(
			TableLayout tableLayout, int aFieldWidth, int[] aResId,
			int[] iTotalRandoms, Context context) {
		// int iBallNum = aBallNum;
		TableLayout table;
		if (tableLayout == null) {
			table = new TableLayout(context);
		} else {
			table = tableLayout;
		}

		int iBallNum = iTotalRandoms.length;
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int viewNumPerLine = 8;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine
				- 2;

		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);
			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "";
				switch (col) {
				case 0:
					iStrTemp = "大";
					break;
				case 1:
					iStrTemp = "小";
					break;
				case 2:
					iStrTemp = "单";
					break;
				case 3:
					iStrTemp = "双";
					break;
				}
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				tempBallView.changeBallColor();
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin + 1, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			table.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(context);
			for (int col = 0; col < lastLineViewNum; col++) {
				String iStrTemp = "";
				int num = iTotalRandoms[iBallViewNo];
				switch (num) {
				case 0:
					iStrTemp = "大";
					break;
				case 1:
					iStrTemp = "小";
					break;
				case 2:
					iStrTemp = "单";
					break;
				case 3:
					iStrTemp = "双";
					break;
				}
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				// tempBallView.setOnClickListener(this);
				tempBallView.changeBallColor();
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin + 1, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			table.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}

		return table;
	}

	/**
	 * 创建自选BallTable
	 * 
	 * @param LinearLayout
	 *            aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	public static BallTable makeBallTable(TableLayout tableLayout,
			int aFieldWidth, int aBallNum, int[] aResId, int aIdStart,
			int aBallViewText, Context context, OnClickListener onclick,
			int isNum) {
		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart, context);
		int iBallNum = aBallNum;
		int viewNumPerLine = 8;
		viewNumPerLine = isNum;
		// 定义没行小球的个数为7
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine
				- 2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		iBallTable.lineNum = lineNum;
		iBallTable.lieNum = viewNumPerLine;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);

			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "";
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);// 小球从0开始
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				if (onclick != null) {
					tempBallView.setOnClickListener(onclick);
				}
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			tabble.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			iBallTable.lineNum++;
			TableRow tableRow = new TableRow(context);
			for (int col = 0; col < lastLineViewNum; col++) {
				String iStrTemp = "";
				// PublicMethod.myOutput("-----------"+iBallViewNo);
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				if (onclick != null) {
					tempBallView.setOnClickListener(onclick);
				}
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			tabble.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}

	/**
	 * 计算选区高度
	 * 
	 * @param aFieldWidth屏幕宽度
	 * @param aBallNum选区总球数
	 * @param viewNumPerLine每行个数
	 * @return 选区高度
	 */
	public static int areaHeight(int aFieldWidth, int iBallNum,
			int viewNumPerLine, boolean isMiss) {
		// 定义没行小球的个数为7
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine
				- 2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		if (lastLineViewNum > 0) {
			lineNum++;
		}
		if (isMiss) {
			lineNum *= 2;
		}
		return (lineNum + 1) * iBallViewWidth + 10;// +1代表标题
	}

	/**
	 * 创建自选BallTable和遗漏值
	 * 
	 * @param LinearLayout
	 *            aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	public static BallTable makeBallTable(TableLayout tableLayout,
			int aFieldWidth, int aBallNum, int[] aResId, int aIdStart,
			int aBallViewText, boolean isplw, Context context, int isNum,
			List<String> missValues, OnClickListener onclick) {
		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart, context);
		int iBallNum = aBallNum;
		int viewNumPerLine = 0;
		viewNumPerLine = isNum;
		// 定义没行小球的个数为7
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine
				- 2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		iBallTable.lineNum = lineNum;
		iBallTable.lieNum = viewNumPerLine;
		int[] rankInt = null;
		if (missValues != null) {
			rankInt = rankList(missValues);
		}
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);
			TableRow tableRowText = new TableRow(context);
			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "";
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);// 小球从0开始
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				int id = aIdStart + iBallViewNo;
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(id);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				if (onclick != null) {
					tempBallView.setOnClickListener(onclick);
				}
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				TextView textView = new TextView(context);
				if (missValues != null) {
					String missValue = missValues.get(iBallViewNo);
					textView.setText(missValue);
					if (rankInt[0] == Integer.parseInt(missValue)
							|| rankInt[1] == Integer.parseInt(missValue)) {
						textView.setTextColor(Color.RED);
					}
				} else {
					textView.setText("0");
				}
				iBallViewNo++;
				textView.setGravity(Gravity.CENTER);
				tableRowText.addView(textView, lp);
				iBallTable.textList.add(textView);
				if (iBallTable.textView == null) {
					iBallTable.textView = textView;
				}
			}
			tabble.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(
					PublicConst.WC, PublicConst.WC));

		}
		if (lastLineViewNum > 0) {
			iBallTable.lineNum++;
			TableRow tableRow = new TableRow(context);
			TableRow tableRowText = new TableRow(context);
			for (int col = 0; col < lastLineViewNum; col++) {
				String iStrTemp = "";
				// PublicMethod.myOutput("-----------"+iBallViewNo);
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				int id = aIdStart + iBallViewNo;
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(id);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				if (onclick != null) {
					tempBallView.setOnClickListener(onclick);
				}
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				TextView textView = new TextView(context);
				if (missValues != null) {
					String missValue = missValues.get(iBallViewNo);
					textView.setText(missValue);
					if (rankInt[0] == Integer.parseInt(missValue)
							|| rankInt[1] == Integer.parseInt(missValue)) {
						textView.setTextColor(Color.RED);
					}
				} else {
					textView.setText("0");
				}
				iBallViewNo++;
				textView.setGravity(Gravity.CENTER);
				tableRowText.addView(textView, lp);
				iBallTable.textList.add(textView);
			}
			// 新建的TableRow添加到TableLayout
			tabble.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}

	/**
	 * 遗漏值赋值
	 * 
	 * @param highttype
	 */
	public static void setMissText(List<TextView> textList,
			List<String> missValues, String highttype, Context context) {
		int[] rankInt = null;
		if (missValues != null) {
			rankInt = rankList(missValues);
		}
		for (int i = 0; i < textList.size(); i++) {
			String missValue = missValues.get(i);
			textList.get(i).setText(missValue);
			if (rankInt[0] == Integer.parseInt(missValue)
					|| rankInt[1] == Integer.parseInt(missValue)) {
				if (highttype.equals("CQ_ELEVEN_FIVE")
						|| highttype.equals("CQ11X5_PT_QZ1")
						|| highttype.equals("CQ11X5_PT_QZ2")
						|| highttype.equals("CQ11X5_PT_QZ3")) {
					textList.get(i).setTextColor(
							context.getResources().getColor(
									R.color.cq_11_5_hot_miss));
				} else {
					textList.get(i).setTextColor(Color.RED);
				}

			} else {
				if (highttype.equals("CQ_ELEVEN_FIVE")
						|| highttype.equals("CQ11X5_PT_QZ1")
						|| highttype.equals("CQ11X5_PT_QZ2")
						|| highttype.equals("CQ11X5_PT_QZ3")) {
					textList.get(i).setTextColor(
							context.getResources().getColor(
									R.color.cq_11_5_coll_miss));
				}
			}
		}
	}

	/**
	 * 遗漏值赋值
	 * 
	 * @param highttype
	 */
	public static void setMissText(List<TextView> textList,
			List<String> missValues) {
		int[] rankInt = null;
		if (missValues != null) {
			rankInt = rankList(missValues);
		}
		for (int i = 0; i < textList.size(); i++) {
			String missValue = missValues.get(i);
			textList.get(i).setText(missValue);
			if (rankInt[0] == Integer.parseInt(missValue)
					|| rankInt[1] == Integer.parseInt(missValue)) {
				textList.get(i).setTextColor(Color.RED);
			}
		}
	}

	/**
	 * 冒泡排序
	 * 
	 * @param myArray
	 * @return
	 */
	public static int[] rankList(List<String> myArray) {
		int[] rankInt = new int[myArray.size()];
		for (int n = 0; n < myArray.size(); n++) {
			rankInt[n] = Integer.parseInt(myArray.get(n));
		}
		// 取长度最长的词组 -- 冒泡法
		for (int j = 1; j < rankInt.length; j++) {
			for (int i = 0; i < rankInt.length - 1; i++) {
				// 如果 myArray[i] > myArray[i+1] ，则 myArray[i] 上浮一位
				if (rankInt[i] < rankInt[i + 1]) {
					int temp = rankInt[i];
					rankInt[i] = rankInt[i + 1];
					rankInt[i + 1] = temp;
				}
			}
		}
		return rankInt;
	}

	/**
	 * 冒泡排序
	 * 
	 * @param myArray
	 * @return
	 */
	public static List<Integer> rankIntList(List<Integer> rankInt) {
		// 取长度最长的词组 -- 冒泡法
		for (int j = 1; j < rankInt.size(); j++) {
			for (int i = 0; i < rankInt.size() - 1; i++) {
				// 如果 myArray[i] > myArray[i+1] ，则 myArray[i] 上浮一位
				if (rankInt.get(i) > rankInt.get(i + 1)) {
					int temp = rankInt.get(i);
					rankInt.set(i, rankInt.get(i + 1));
					rankInt.set(i + 1, temp);
				}
			}
		}
		return rankInt;
	}

	/**
	 * 中奖查询，投注查询注码解析
	 * 
	 * @param betcode
	 * @param play_name
	 * @return
	 */
	public static String[] getBetcode(String betcode, String play_name) {
		String lotNo = "";
		String betCode = "";
		String beishu = "";
		int wayCode = 0;
		if (play_name.equals("B001") || play_name.equals("F47104")
				|| play_name.equals("QL730") || play_name.equals("F47102")
				|| play_name.equals("D3") || play_name.equals("F47103")) {
			wayCode = Integer.parseInt(betcode.substring(0, 2));
			beishu = betcode.substring(2, 4);
		}

		if (play_name.equals("B001") || play_name.equals("F47104")) {
			if (wayCode == 00) {

				lotNo = "双色球单式";
				String mp[] = GT.splitBetCode(betcode);
				betCode = "";
				for (int i = 0; i < mp.length; i++) {

					betCode += (GT.makeString("F47104", wayCode,
							mp[i].substring(4)) + "\n");
				}
			} else if (wayCode == 40 || wayCode == 50) {
				lotNo = "双色球胆拖复式";
				int index1 = 0;
				for (int i = 0; i < betcode.length(); i++) {
					if (betcode.charAt(i) == '*') {
						index1 = i;
					}
				}
				int index2 = 0;// 查找“~”
				for (int i = 0; i < betcode.length(); i++) {
					if (betcode.charAt(i) == '~') {
						index2 = i;
					}
				}
				String danma = GT.makeString("F47104", wayCode,
						betcode.substring(4, index1));
				String tuoma = GT.makeString("F47104", wayCode,
						betcode.substring(index1 + 1, index2));
				String lanqiu = GT.makeString("F47104", wayCode,
						betcode.substring(index2 + 1, betcode.length() - 1));
				betCode = "红球胆码: " + danma + "\n" + "红球拖码: " + tuoma + "\n"
						+ "蓝球：" + lanqiu + "\n";

			} else {
				lotNo = "双色球红蓝复式";
				int index1 = 0;// 查找“*”
				for (int i = 0; i < betcode.length(); i++) {
					if (betcode.charAt(i) == '~') {
						index1 = i;
					}
				}

				String redball = GT.makeString("F47104", wayCode,
						betcode.substring(5, index1));
				String blueball = GT.makeString("F47104", wayCode,
						betcode.substring(index1 + 1, betcode.length() - 1));

				betCode = "红球: " + redball + "\n" + "蓝球: " + blueball + "\n";
			}
		} else if (play_name.equals("D3") || play_name.equals("F47103")) {
			if (wayCode == 54) {
				lotNo = "福彩3D胆拖";
				int index1 = 0;
				for (int i = 0; i < betcode.length(); i++) {
					if (betcode.charAt(i) == '*') {
						index1 = i;
					}
				}
				String danma = GT.makeString("F47103", wayCode,
						betcode.substring(4, index1));
				String tuoma = GT.makeString("F47103", wayCode,
						betcode.substring(index1 + 1, betcode.length() - 1));
				betCode = "胆码: " + danma + "\n" + "拖码: " + tuoma + "\n";
				// 单选复式码解析 2010/7/5 陈晨
			} else if (wayCode == 00) {
				// 3D单选注码格式 解析
				lotNo = "福彩3D直选";
				String mp[] = GT.splitBetCode(betcode);
				betcode = "";
				for (int i = 0; i < mp.length; i++) {
					betCode += (GT.makeString("F47103", wayCode,
							mp[i].substring(4)) + "\n");
				}
			} else if (wayCode == 20) {
				lotNo = "福彩3D直选";
				int index1 = 0;
				int index2 = 0;
				for (int i = 0; i < betcode.length(); i++) {
					if (betcode.charAt(i) == '^') {
						index1 = i;
						i = betcode.length();
					}
				}
				for (int j = index1 + 1; j < betcode.length(); j++) {
					if (betcode.charAt(j) == '^') {
						index2 = j;
						j = betcode.length();
					}
				}
				String baiwei = GT.makeString("F47103", wayCode,
						betcode.substring(6, index1 + 1));
				String shiwei = GT.makeString("F47103", wayCode,
						betcode.substring(index1 + 3, index2));
				String gewei = GT.makeString("F47103", wayCode,
						betcode.substring(index2 + 3, betcode.length() - 1));
				betCode = "百位: " + baiwei + "\n" + "十位: " + shiwei + "\n"
						+ "个位: " + gewei + "\n";
				// 3D直选单式 注码解析
			}

			else {
				if (wayCode == 01) {
					lotNo = "福彩3D组3";
				} else if (wayCode == 02 || wayCode == 32) {
					lotNo = "福彩3D组6";
				} else if (wayCode == 10) {
					lotNo = "福彩3D直选和值";
				} else if (wayCode == 11) {
					lotNo = "福彩3D组3和值";
				} else if (wayCode == 12) {
					lotNo = "福彩3D组6和值";
				} else if (wayCode == 31) {
					lotNo = "福彩3D组3复式";
				} else if (wayCode == 31) {
					lotNo = "福彩3D组6复式";
				}
				String mp[] = GT.splitBetCode(betcode);
				betCode = "";
				for (int i = 0; i < mp.length; i++) {
					betCode += (GT.makeString("F47103", wayCode,
							mp[i].substring(4)) + "\n");
				}
			}
		} else if (play_name.equals("QL730") || play_name.equals("F47102")) {
			if (wayCode == 00) {
				int index_q;
				String mp[] = GT.splitBetCode(betcode);
				lotNo = "七乐彩单式";
				betCode = "";
				for (int i = 0; i < mp.length; i++) {
					betCode += (GT.makeString("F47102", wayCode,
							mp[i].substring(4)) + "\n");
				}
			} else if (wayCode == 10) {
				lotNo = "七乐彩复式";
				betCode = GT.makeString("F47102", wayCode,
						betcode.substring(5, betcode.length() - 1))
						+ "\n";
			} else if (wayCode == 20) {
				lotNo = "七乐彩胆拖";
				int index1 = 0;
				for (int i = 0; i < betcode.length(); i++) {
					if (betcode.charAt(i) == '*') {
						index1 = i;
					}
				}

				String danma = GT.makeString("F47102", wayCode,
						betcode.substring(4, index1));
				String tuoma = GT.makeString("F47102", wayCode,
						betcode.substring(index1 + 1, betcode.length() - 1));
				betCode = "胆码: " + danma + "\n" + "拖码: " + tuoma + "\n";
			}
		} else if (play_name.equals("T01001") || play_name.equals("DLT_23529")) {
			// String play_name="";
			String[] checkType = new String[2];
			String[] headArea = new String[2];
			String[] rearArea = new String[2];
			// String betCode = "";
			boolean check01 = false; // 检测是否有"-"
			boolean check02 = false; // 检测是否有"$"
			int index = 0;
			for (int i = 0; i < betcode.length(); i++) {
				if (betcode.charAt(i) == '-' || betcode.charAt(i) == '+') {
					check01 = true;
					index = i;
					i = betcode.length();
				}
			}
			for (int i = 0; i < betcode.length(); i++) {
				if (betcode.charAt(i) == '$') {
					check02 = true;
				}
			}
			checkType[0] = betcode.substring(0, index);
			checkType[1] = betcode.substring(index + 1);

			if (check01) {
				if (check02) {
					int index01 = 0;
					int index02 = 0;
					boolean check03 = false;
					boolean check04 = false;
					lotNo = "超级大乐透胆拖";

					for (int i = 0; i < checkType[0].length(); i++) {
						if (checkType[0].charAt(i) == '$') {
							index01 = i;
							check03 = true;
						}
					}
					if (check03) {
						if (index01 != 0) {
							headArea[0] = checkType[0].substring(0, index01);
							headArea[1] = checkType[0].substring(index01 + 1);
						} else {
							headArea[0] = " ";
							headArea[1] = checkType[0].substring(1);
						}
					}

					for (int i = 0; i < checkType[1].length(); i++) {
						if (checkType[1].charAt(i) == '$') {
							index02 = i;
							check04 = true;
						}
					}

					if (check04) {
						if (index02 != 0) {
							rearArea[0] = checkType[1].substring(0, index02);
							rearArea[1] = checkType[1].substring(index02 + 1);
						} else {
							rearArea[0] = " ";
							rearArea[1] = checkType[1].substring(1);
						}
					}

					betCode = "前区胆码： " + headArea[0] + "\n" + "前区拖码： "
							+ headArea[1] + "\n" + "后区胆码： " + rearArea[0]
							+ "\n" + "后区拖码： " + rearArea[1] + "\n";
				} else {
					String[] mp = GT.splitBetCodeTC(betcode);

					int iStr = 0;
					for (int i = 0; i < mp[0].length(); i++) {
						if (mp[0].charAt(i) == '-') {
							iStr = i;
							i = mp[0].length();
						}
					}
					if (mp[0].substring(0, iStr).length() == 14
							&& mp[0].substring(iStr + 1).length() == 5) {
						lotNo = "超级大乐透单式";
						// betCode = checkType[0] + " | " + checkType[1]+"\n";
						for (int i = 0; i < mp.length; i++) {
							betCode += (GT.makeString("T01001", 0, mp[i]) + "\n");
						}

					} else if (checkType[0].length() != 14
							|| checkType[1].length() != 5) {
						if (checkType[1].contains("-")) {
							lotNo = "超级大乐透直选";
							String strs[] = betcode.split(";");
							for (int i = 0; i < strs.length; i++) {
								String str[] = strs[i].split("-");
								betCode += str[0] + " | " + str[1] + "\n";
							}
						} else {
							lotNo = "超级大乐透复式";
							betCode = checkType[0] + " | " + checkType[1]
									+ "\n";
						}

					}
				}
			} else {
				// betcode = betcode01;
				if (betcode.length() == 5) {
					lotNo = "生肖乐单式";
					betCode = betcode + "\n";
				} else {
					lotNo = "生肖乐复式";
					String strs[] = betcode.split(";");
					for (int i = 0; i < strs.length; i++) {
						betCode += strs[i] + "\n";
					}
				}
			}
		} else if (play_name.equals("T01002") || play_name.equals("PL3_33")) {
			String[] betcodes = betcode.split("\\;");
			for (int m = 0; m < betcodes.length; m++) {
				String[] checkType = new String[2];
				/*
				 * try{ JSONObject obj = jsonArray.getJSONObject(index); betcode
				 * = obj.getString("betcode"); } catch (JSONException e) {
				 * e.printStackTrace(); }
				 */
				int index = 0;
				for (int i = 0; i < betcodes[m].length(); i++) {
					if (betcodes[m].charAt(i) == '|') {
						index = i;
						i = betcodes[m].length();
					}
				}
				checkType[0] = betcodes[m].substring(0, index);
				checkType[1] = betcodes[m].substring(index + 1);

				if (checkType[0].equalsIgnoreCase("1")) {
					String[] mp = GT.splitBetCodeTC(betcodes[m]);

					if (mp[0].length() == 7) {
						lotNo = "排列三直选单式";
						for (int i = 0; i < mp.length; i++) {
							betCode += (GT.makeString("T01002", 0, mp[i]) + "\n");
						}
						// String subStr = checkType[1];
						// String[] subStrSplit = new String[3];
						// for(int i=0 ;i<3;i++){
						// subStrSplit[i] = subStr.substring(2*i, 2*i+1);
						// }
						// betCode = "百位: " + subStrSplit[0] + "\n" + "十位: " +
						// subStrSplit[1] + "\n" + "个位: " + subStrSplit[2]+"\n";
					} else if (checkType[1].length() > 5) {
						lotNo = "排列三直选复式";
						String subStr = checkType[1]; // 分割后的号码
						String[] subStrSplit = subStr.split(",", 3); // 将百位、十位、个位分开
						String[] subStrSplitLast = new String[3];

						for (int i = 0; i < 3; i++) {
							String str03 = "";
							String[] str02 = new String[subStrSplit[i].length()];
							String str01 = subStrSplit[i];
							for (int j = 0; j < str01.length(); j++) {
								str02[j] = str01.substring(j, j + 1);
								str03 += str02[j] + " ";
							}
							subStrSplitLast[i] = str03;
						}

						betCode = "百位： " + subStrSplitLast[0] + "\n" + "十位： "
								+ subStrSplitLast[1] + "\n" + "个位： "
								+ subStrSplitLast[2] + "\n";
					}
				} else if (checkType[0].equalsIgnoreCase("6")) {
					String subStr = null;
					subStr = checkType[1];
					int[] subStrLast = new int[3];
					for (int i = 0; i < 3; i++) {
						subStrLast[i] = Integer.valueOf(subStr.substring(2 * i,
								2 * i + 1));
					}
					if (subStrLast[0] == subStrLast[1]
							|| subStrLast[1] == subStrLast[2]) {
						lotNo = "排列三组三";
						betCode += subStr + "\n";
					} else {
						lotNo = "排列三组六";
						betCode += subStr + "\n";
					}

				} else {
					String[] gameType = { "S1", "S9", "S3", "S6" };
					String[] gameType01 = { "F3", "F6" };
					String[] gameTitle = { "排列三直选和值", "排列三组选和值", "排列三组三和值",
							"排列三组六和值" };
					String[] gameTitle01 = { "排列三组三包号", "排列三组六包号" };
					for (int i = 0; i < 4; i++) {
						if (checkType[0].equalsIgnoreCase(gameType[i])) {
							String subStr = checkType[1];
							lotNo = gameTitle[i];
							betCode = subStr + "\n";
						}
					}
					for (int i = 0; i < 2; i++) {
						if (checkType[0].equalsIgnoreCase(gameType01[i])) {
							String subStr = checkType[1];
							String[] subStrLast = new String[subStr.length()];
							String finalStr = "";
							lotNo = gameTitle01[i];
							for (int j = 0; j < subStr.length(); j++) {
								subStrLast[j] = subStr.substring(j, j + 1);
								finalStr += subStrLast[j] + " ";
							}
							betCode = finalStr + "\n";
						}
					}
				}
			}
		} else if (play_name.equals("T01007") || play_name.equals("SSC_10401")) {
			String betType = "";
			String betCodeView = "";

			String[] bet_codes = betcode.split(";");
			for (String bet_code : bet_codes) {
				if (bet_code.length() > 0 && !bet_code.contains("DD")) {
					betCodeView = betCodeView
							+ bet_code.replace("+", "-").substring(3) + "\n";
				} else {

					char c[] = bet_code.substring(3).toCharArray();

					for (int i = 0; i < c.length; i++) {

						switch (c[i]) {
						case '2':
							betCodeView = betCodeView + "大";

							break;
						case '1':
							betCodeView = betCodeView + "小";
							break;
						case '5':
							betCodeView = betCodeView + "单";
							break;
						case '4':
							betCodeView = betCodeView + "双";
							break;
						}
					}
					betCodeView += "\n";

				}
			}

			betType = getSSCBetType(betcode);
			betCode = betCodeView;
			lotNo = betType;
		} else if (play_name.equals("T01003")) {// 胜负彩
			String betType = "";
			String betCodeView = "";

			String[] bet_codes = betcode.split(";");
			for (String bet_code : bet_codes) {
				if (bet_code.length() > 0) {
					betCodeView = betCodeView + bet_code + "\n";
				}
			}
			betCode = betCodeView;
			lotNo = "胜负彩";
		} else if (play_name.equals("T01004")) {// 任选九
			String betType = "";
			String betCodeView = "";

			String[] bet_codes = betcode.split(";");
			for (String bet_code : bet_codes) {
				if (bet_code.length() > 0) {
					betCodeView = betCodeView + bet_code + "\n";
				}
			}
			betCode = betCodeView;
			lotNo = "任选九";
		} else if (play_name.equals("T01005")) {// 进球彩
			String betType = "";
			String betCodeView = "";

			String[] bet_codes = betcode.split(";");
			for (String bet_code : bet_codes) {
				if (bet_code.length() > 0) {
					betCodeView = betCodeView + bet_code + "\n";
				}
			}

			betCode = betCodeView;
			lotNo = "进球彩";
		} else if (play_name.equals("T01006")) {// 六场半
			String betType = "";
			String betCodeView = "";

			String[] bet_codes = betcode.split(";");
			for (String bet_code : bet_codes) {
				if (bet_code.length() > 0) {
					betCodeView = betCodeView + bet_code + "\n";
				}
			}
			betCode = betCodeView;
			lotNo = "六场半";
		} else if (play_name.equals("T01009")) {
			lotNo = "七星彩";
		} else if (play_name.equals("T01008")) {
			lotNo = "北京单场";
		} else if (play_name.equals("T01010")) {
			lotNo = "11选5";
		} else if (play_name.equals("T01011")) {
			lotNo = "排列五";
		}
		String[] str = { lotNo, betCode, beishu };
		return str;
	}

	/**
	 * 获得时时彩的投注方式
	 * 
	 * @param betCode
	 * @return
	 */
	public static String getSSCBetType(String betCode) {
		String betType = "";
		if (betCode.contains("1D")) {
			betType = "一星";
		} else if (betCode.contains("2D")) {
			betType = "二星";
		} else if (betCode.contains("3D")) {
			betType = "三星";
		} else if (betCode.contains("5D")) {
			betType = "五星";
		} else if (betCode.contains("5F")) {
			betType = "五星复选";
		} else if (betCode.contains("5T")) {
			betType = "五星通选";
		} else if (betCode.contains("3F")) {
			betType = "三星复选";
		} else if (betCode.contains("2F")) {
			betType = "二星复选";
		} else if (betCode.contains("H2")) {
			betType = "二星和值";
		} else if (betCode.contains("S2")) {
			betType = "二星包点";
		} else if (betCode.contains("DD")) {
			betType = "大小单双";
		} else if (betCode.contains("Z2")) {
			betType = "二星组选";
		} else if (betCode.contains("F2")) {
			betType = "二星组选";
		}
		return betType;
	}

	/**
	 * 将数组转换成注码串
	 * 
	 * @param balls
	 * @return
	 */
	public static String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += isTen(balls[i]);
			if (i != (balls.length - 1))
				str += ",";
		}
		return str;

	}

	public static String toLotno(String type) {
		String title = "";
		if (type != null) {
			if (type.equals(Constants.LOTNO_SSQ)) {
				title = "双色球";
			} else if (type.equals(Constants.LOTNO_FC3D)) {
				title = "福彩3D";
			} else if (type.equals(Constants.LOTNO_QLC)) {
				title = "七乐彩";
			} else if (type.equals(Constants.LOTNO_PL3)) {
				title = "排列三";
			} else if (type.equals(Constants.LOTNO_DLT)) {
				title = "大乐透";
			} else if (type.equals(Constants.LOTNO_SFC)) {
				title = "胜负彩";
			} else if (type.equals(Constants.LOTNO_JQC)) {
				title = "进球彩";
			} else if (type.equals(Constants.LOTNO_LCB)) {
				title = "六场半";
			} else if (type.equals(Constants.LOTNO_RX9)) {
				title = "任选九";
			} else if (type.equals(Constants.LOTNO_QXC)) {
				title = "七星彩";
			} else if (type.equals(Constants.LOTNO_SSC)) {
				title = "时时彩";
			} else if (type.equals(Constants.LOTNO_11_5)) {
				title = "江西11选5";
			} else if (type.equals(Constants.LOTNO_QXC)) {
				title = "七星彩";
			} else if (type.equals(Constants.LOTNO_PL5)) {
				title = "排列五";
			} else if (type.equals(Constants.LOTNO_JCLQ)) {
				title = "竞彩篮球胜负";
			} else if (type.equals(Constants.LOTNO_JCLQ_RF)) {
				title = "竞彩篮球让分胜负";
			} else if (type.equals(Constants.LOTNO_JCLQ_SFC)) {
				title = "竞彩篮球胜分差";
			} else if (type.equals(Constants.LOTNO_JCLQ_DXF)) {
				title = "竞彩篮球大小分";
			} else if (type.equals(Constants.LOTNO_22_5)) {
				title = "22选5";
			} else if (type.equals(Constants.LOTNO_eleven)) {
				title = "11运夺金";
			} else if (type.equals(Constants.LOTNO_JCL)) {
				title = "竞彩篮球";
			} else if (type.equals(Constants.LOTNO_JCZ)) {
				title = "竞彩足球";
			} else if (type.equals(Constants.LOTNO_JCZQ)) {
				title = "竞彩足球胜负";
			} else if (type.equals(Constants.LOTNO_JCZQ_RQSPF)) {
				title = "竞彩足球让球胜平负";
			} else if (type.equals(Constants.LOTNO_JCZQ_ZQJ)) {
				title = "竞彩足球总进球";
			} else if (type.equals(Constants.LOTNO_JCZQ_BQC)) {
				title = "竞彩足球半全场";
			} else if (type.equals(Constants.LOTNO_JCZQ_BF)) {
				title = "竞彩足球比分";
			} else if (type.equals(Constants.LOTNO_GD_11_5)) {
				title = "广东11选5";
			} else if (type.equals(Constants.LOTNO_ten)) {
				title = "广东快乐十分";
			} else if (type.equals(Constants.LOTNO_JCLQ_HUN)) {
				title = "竞彩篮球混合";
			} else if (type.equals(Constants.LOTNO_JCZQ_HUN)) {
				title = "竞彩足球混合";
			} else if (type.equals(Constants.LOTNO_NMK3)) {
				title = "快三";
			} else if (type
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS)) {
				title = "北京单场胜平负";
			} else if (type
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS)) {
				title = "北京单场总进球";
			} else if (type.equals(Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL)) {
				title = "北京单场全场总比分";
			} else if (type
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE)) {
				title = "北京单场半全场";
			} else if (type
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE)) {
				title = "北京单场上下单双";
			} else if (type.equals(Constants.LOTNO_BJ_SINGLE)) {
				title = "北京单场";
			} else if (type.equals(Constants.LOTNO_ZC)) {
				title = "足彩";
			}else if(type.equals(Constants.LOTNO_CQ_ELVEN_FIVE)){
				title = "重庆11选5";
			}else {
				title = "所有彩种";
			}
		}
		return title;

	}

	public static String toLotnohemai(String type) {
		String title = "";
		if (type != null) {
			if (type.equals(Constants.LOTNO_SSQ)) {
				title = "双色球";
			} else if (type.equals(Constants.LOTNO_FC3D)) {
				title = "福彩3D";
			} else if (type.equals(Constants.LOTNO_QLC)) {
				title = "七乐彩";
			} else if (type.equals(Constants.LOTNO_PL3)) {
				title = "排列三";
			} else if (type.equals(Constants.LOTNO_DLT)) {
				title = "大乐透";
			} else if (type.equals(Constants.LOTNO_SFC)) {
				title = "胜负彩";
			} else if (type.equals(Constants.LOTNO_JQC)) {
				title = "进球彩";
			} else if (type.equals(Constants.LOTNO_LCB)) {
				title = "六场半";
			} else if (type.equals(Constants.LOTNO_RX9)) {
				title = "任选九";
			} else if (type.equals(Constants.LOTNO_QXC)) {
				title = "七星彩";
			} else if (type.equals(Constants.LOTNO_SSC)) {
				title = "时时彩";
			} else if (type.equals(Constants.LOTNO_11_5)) {
				title = "11选5";
			} else if (type.equals(Constants.LOTNO_QXC)) {
				title = "七星彩";
			} else if (type.equals(Constants.LOTNO_PL5)) {
				title = "排列五";
			} else if (type.equals(Constants.LOTNO_JC)) {
				title = "竞彩足球";
			} else if (type.equals(Constants.LOTNO_JCLQ)) {
				title = "竞彩篮球";
			} else if (type.equals(Constants.LOTNO_JCLQ_RF)) {
				title = "竞彩篮球";
			} else if (type.equals(Constants.LOTNO_JCLQ_SFC)) {
				title = "竞彩篮球";
			} else if (type.equals(Constants.LOTNO_JCLQ_DXF)) {
				title = "竞彩篮球";
			} else if (type.equals(Constants.LOTNO_22_5)) {
				title = "22选5";
			} else if (type.equals(Constants.LOTNO_eleven)) {
				title = "11运夺金";
			} else if (type.equals(Constants.LOTNO_JCL)) {
				title = "竞彩篮球";
			} else if (type.equals(Constants.LOTNO_JCZ)) {
				title = "竞彩足球";
			} else if (type.equals(Constants.LOTNO_JCZQ)) {
				title = "竞彩足球";
			} else if (type.equals(Constants.LOTNO_JCZQ_ZQJ)) {
				title = "竞彩足球";
			} else if (type.equals(Constants.LOTNO_JCZQ_BF)) {
				title = "竞彩足球";
			} else if (type.equals(Constants.LOTNO_JCZQ_BQC)) {
				title = "竞彩足球";
			} else {
				title = "所有彩种";
			}
		}
		return title;

	}
	public static String infoToLotnoName(String type) {
		String title = "";
		if (type != null) {
			if (type.equals(Constants.LOTNO_SSQ)) {
				title = "双色球";
			} else if (type.equals(Constants.LOTNO_FC3D)) {
				title = "福彩3D";
			} else if (type.equals(Constants.LOTNO_QLC)) {
				title = "七乐彩";
			} else if (type.equals(Constants.LOTNO_PL3)) {
				title = "排列三";
			} else if (type.equals(Constants.LOTNO_DLT)) {
				title = "大乐透";
			} else if (type.equals(Constants.LOTNO_ZC)
					||type.equals(Constants.LOTNO_SFC)
					||type.equals(Constants.LOTNO_JQC)
					||type.equals(Constants.LOTNO_LCB)
					||type.equals(Constants.LOTNO_RX9)) {
				title = "足彩";
			} else if (type.equals(Constants.LOTNO_QXC)) {
				title = "七星彩";
			} else if (type.equals(Constants.LOTNO_SSC)) {
				title = "时时彩";
			} else if (type.equals(Constants.LOTNO_11_5)) {
				title = "江西11选5";
			} else if (type.equals(Constants.LOTNO_PL5)) {
				title = "排列五";
			} else if (type.equals(Constants.LOTNO_JCLQ_HUN)
					||type.equals(Constants.LOTNO_JCLQ)
					||type.equals(Constants.LOTNO_JCLQ_RF)
					||type.equals(Constants.LOTNO_JCLQ_SFC)
					||type.equals(Constants.LOTNO_JCLQ_DXF)
					||type.equals(Constants.LOTNO_JCL)) {
				title = "竞彩篮球";
			} else if (type.equals(Constants.LOTNO_22_5)) {
				title = "22选5";
			} else if (type.equals(Constants.LOTNO_eleven)) {
				title = "11运夺金";
			}else if (type.equals(Constants.LOTNO_JCZQ_HUN)
					||type.equals(Constants.LOTNO_JCZ)
					||type.equals(Constants.LOTNO_JCZQ)
					||type.equals(Constants.LOTNO_JCZQ_RQSPF)
					||type.equals(Constants.LOTNO_JCZQ_ZQJ)
					||type.equals(Constants.LOTNO_JCZQ_BQC)
					||type.equals(Constants.LOTNO_JCZQ_BF)) {
				title = "竞彩足球";
			} else if (type.equals(Constants.LOTNO_GD_11_5)) {
				title = "广东11选5";
			} else if (type.equals(Constants.LOTNO_ten)) {
				title = "广东快乐十分";
			}else if (type.equals(Constants.LOTNO_NMK3)) {
				title = "快三";
			} else if (type.equals(Constants.LOTNO_BJ_SINGLE)
					||type.equals(Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE)
					||type.equals(Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE)
					||type.equals(Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL)
					||type.equals(Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS)
					||type.equals(Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS)) {
				title = "北京单场";
			}else {
				title = "所有彩种";
			}
		}
		return title;

	}
	
	public static String infoToLotno(String type) {
		String title = "";
		if (type != null) {
			if (type.equals(Constants.LOTNO_SSQ)) {
				title = "双色球";
			} else if (type.equals(Constants.LOTNO_FC3D)) {
				title = "福彩3D";
			} else if (type.equals(Constants.LOTNO_QLC)) {
				title = "七乐彩";
			} else if (type.equals(Constants.LOTNO_PL3)) {
				title = "排列三";
			} else if (type.equals(Constants.LOTNO_DLT)) {
				title = "大乐透";
			} else if (type.equals(Constants.LOTNO_SFC)) {
				title = "胜负彩";
			} else if (type.equals(Constants.LOTNO_JQC)) {
				title = "进球彩";
			} else if (type.equals(Constants.LOTNO_LCB)) {
				title = "六场半";
			} else if (type.equals(Constants.LOTNO_RX9)) {
				title = "任选九";
			} else if (type.equals(Constants.LOTNO_QXC)) {
				title = "七星彩";
			} else if (type.equals(Constants.LOTNO_SSC)) {
				title = "时时彩";
			} else if (type.equals(Constants.LOTNO_11_5)) {
				title = "江西11选5";
			} else if (type.equals(Constants.LOTNO_QXC)) {
				title = "七星彩";
			} else if (type.equals(Constants.LOTNO_PL5)) {
				title = "排列五";
			} else if (type.equals(Constants.LOTNO_JCLQ)) {
				title = "胜负";
			} else if (type.equals(Constants.LOTNO_JCLQ_RF)) {
				title = "让分胜负";
			} else if (type.equals(Constants.LOTNO_JCLQ_SFC)) {
				title = "胜分差";
			} else if (type.equals(Constants.LOTNO_JCLQ_DXF)) {
				title = "大小分";
			} else if (type.equals(Constants.LOTNO_22_5)) {
				title = "22选5";
			} else if (type.equals(Constants.LOTNO_eleven)) {
				title = "11运夺金";
			} else if (type.equals(Constants.LOTNO_JCL)) {
				title = "竞彩篮球";
			} else if (type.equals(Constants.LOTNO_JCZ)) {
				title = "竞彩足球";
			} else if (type.equals(Constants.LOTNO_JCZQ)) {
				title = "胜负";
			} else if (type.equals(Constants.LOTNO_JCZQ_RQSPF)) {
				title = "让球胜平负";
			} else if (type.equals(Constants.LOTNO_JCZQ_ZQJ)) {
				title = "总进球";
			} else if (type.equals(Constants.LOTNO_JCZQ_BQC)) {
				title = "半全场";
			} else if (type.equals(Constants.LOTNO_JCZQ_BF)) {
				title = "比分";
			} else if (type.equals(Constants.LOTNO_GD_11_5)) {
				title = "广东11选5";
			} else if (type.equals(Constants.LOTNO_ten)) {
				title = "广东快乐十分";
			} else if (type.equals(Constants.LOTNO_JCLQ_HUN)) {
				title = "混合";
			} else if (type.equals(Constants.LOTNO_JCZQ_HUN)) {
				title = "混合";
			} else if (type.equals(Constants.LOTNO_NMK3)) {
				title = "快三";
			} else if (type
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS)) {
				title = "胜平负";
			} else if (type
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS)) {
				title = "总进球";
			} else if (type.equals(Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL)) {
				title = "全场总比分";
			} else if (type
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE)) {
				title = "半全场";
			} else if (type
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE)) {
				title = "上下单双";
			} else if (type.equals(Constants.LOTNO_BJ_SINGLE)) {
				title = "北京单场";
			} else if (type.equals(Constants.LOTNO_ZC)) {
				title = "足彩";
			}else {
				title = "所有彩种";
			}
		}
		return title;

	}

	/**
	 * 获得彩种开关的key
	 * 
	 * @param type
	 * @return
	 */
	public static String getCloseKeyName(String type) {
		String closeKeyName = "";
		if (type != null) {
			if (type.equals(Constants.LOTNO_22_5)) {
				closeKeyName = Constants.TWENTYBEL;
			}
		}
		return closeKeyName;
	}

	/**
	 * 获得彩种预售key
	 * 
	 * @param type
	 * @return
	 */
	public static String getWillsaleName(String type) {
		String willKeyName = "";
		if (type != null) {
			if (type.equals(Constants.LOTNO_22_5)) {
				willKeyName = Constants.TWENWILLSALES;
			} else if (type.equals(Constants.LOTNO_NMK3)) {
				willKeyName = Constants.NMK3WILLSALES;
			} else if (type.equals(Constants.LOTNO_BJ_SINGLE)) {
				willKeyName = Constants.BDWILLSATES;
			}
		}
		return willKeyName;
	}

	/**
	 * 获得关闭彩种key
	 * 
	 * @param type
	 * @return
	 */
	public static String getCloseTicketFLG(String type) {
		String closeTicketKeyName = "";
		if (type != null) {
			if (type.equals(Constants.LOTNO_22_5)) {
				closeTicketKeyName = Constants.TWENCLOSED;
			}
		}
		return closeTicketKeyName;
	}



	/**
	 * 转换成分
	 * 
	 */
	public static String toFen(String amt) {

		return Integer.toString(Integer.parseInt(amt) * 100);

	}

	/**
	 * 带小数的金额转换成分
	 * 
	 */
	public static String doubleToFen(String amt) {
		BigDecimal amt3 = new BigDecimal(amt);
		BigDecimal shu = new BigDecimal("100");
		// double amt2 = Double.parseDouble(amt) * 100;
		return amt3.multiply(shu).toString();
		// return String.valueOf(Integer.parseInt(amt)*100);
	}

	/**
	 * 带小数的金额转换成分
	 * 
	 */
	public static String intToFen(String amt) {
		BigDecimal amt3 = new BigDecimal(amt);
		BigDecimal shu = new BigDecimal("100");
		// double amt2 = Double.parseDouble(amt) * 100;
		return amt3.multiply(shu).toBigInteger().toString();
		// return String.valueOf(Integer.parseInt(amt)*100);

	}

	/**
	 * 转换成元
	 * 
	 */
	public static String toIntYuan(String amt) {
		String money = "";
		try {
			money = Long.toString(Long.parseLong(amt) / 100);
		} catch (Exception e) {

		}
		return money;

	}

	/**
	 * 转换成元
	 * 
	 */
	public static String toYuan(String amt) {
		double target = Double.parseDouble(amt) / 100;
		String result = formatStringToTwoPoint(target);
		return result;

	}

	public static String formatStringToTwoPoint(double num) {
		DecimalFormat df1 = new DecimalFormat("###0.00");
		String result = df1.format(num);
		return result;
	}

	/**
	 * 将1转换成01
	 * 
	 * @param time
	 * @return
	 */
	public static String isTen(int time) {
		String timeStr = "";
		if (time < 10) {
			timeStr += "0" + time;
		} else {
			timeStr += time;
		}
		return timeStr;
	}

	public static String isTenSpace(int time) {
		String timeStr = "";
		if (time < 10) {
			timeStr += "  " + time;
		} else {
			timeStr += time;
		}
		return timeStr;
	}

	/**
	 * 创建战绩效果
	 * 
	 * @param recordLayout
	 *            战绩显示布局
	 * @param crown
	 *            皇冠
	 * @param grayCrown
	 *            灰皇冠
	 * @param cup
	 *            奖杯
	 * @param grayCup
	 *            灰奖杯
	 * @param diamond
	 *            砖石
	 * @param grayDiamon
	 *            灰砖石
	 * @param star
	 *            星星
	 * @param grayStar
	 *            灰星星
	 */
	public static void createStar(LinearLayout recordLayout, String crown,
			String grayCrown, String cup, String grayCup, String diamond,
			String grayDiamon, String star, String grayStar, Context context,
			int isAbleNum) {
		recordLayout.removeAllViews();

		int crownNum = toInt(crown);
		int grayCrownNum = toInt(grayCrown);
		int cupNum = toInt(cup);
		int grayCupNum = toInt(grayCup);
		int diamondNum = toInt(diamond);
		int grayDiamonNum = toInt(grayDiamon);
		int starNum = toInt(star);
		int grayStarNum = toInt(grayStar);

		if (crownNum > 0) {
			initCrown(recordLayout, context, crownNum);
		}
		if (grayCrownNum > 0) {
			initGrayCrown(recordLayout, context, grayCrownNum);
		}
		if (cupNum > 0) {
			initCup(recordLayout, context, cupNum);
		}
		if (grayCupNum > 0) {
			initGrayCup(recordLayout, context, grayCupNum);
		}
		if (diamondNum > 0) {
			initDiamon(recordLayout, context, diamondNum);
		}
		if (grayDiamonNum > 0) {
			initGrayDiamon(recordLayout, context, grayDiamonNum);
		}
		if (starNum > 0) {
			initStar(recordLayout, context, starNum);
		}
		if (grayStarNum > 0) {
			initGrayStar(recordLayout, context, grayStarNum);
		}
	}

	private static void initStar(LinearLayout recordLayout, Context context,
			int starNum) {
		initIcon(recordLayout, context, starNum, 3, icons);
	}

	private static void initGrayStar(LinearLayout recordLayout,
			Context context, int grayStarNum) {
		initIcon(recordLayout, context, grayStarNum, 3, grayIcons);
	}

	private static void initDiamon(LinearLayout recordLayout, Context context,
			int diamondNum) {
		initIcon(recordLayout, context, diamondNum, 2, icons);
	}

	private static void initGrayDiamon(LinearLayout recordLayout,
			Context context, int grayDiamondNum) {
		initIcon(recordLayout, context, grayDiamondNum, 2, grayIcons);
	}

	private static void initCup(LinearLayout recordLayout, Context context,
			int cupNum) {
		initIcon(recordLayout, context, cupNum, 1, icons);
	}

	private static void initGrayCup(LinearLayout recordLayout, Context context,
			int grayCupNum) {
		initIcon(recordLayout, context, grayCupNum, 1, grayIcons);
	}

	private static void initCrown(LinearLayout recordLayout, Context context,
			int crownNum) {
		initIcon(recordLayout, context, crownNum, 0, icons);
	}

	private static void initGrayCrown(LinearLayout recordLayout,
			Context context, int grayCrownNum) {
		initIcon(recordLayout, context, grayCrownNum, 0, grayIcons);
	}

	/**
	 * 初始化战绩图标
	 * 
	 * @param recordLayout
	 *            战绩图标布局
	 * @param context
	 *            上下文对象
	 * @param iconNum
	 *            图标的个数
	 * @param index
	 *            图标的索引
	 * @param icons
	 *            图标的数组
	 */
	private static void initIcon(LinearLayout recordLayout, Context context,
			int iconNum, int index, int[] icons) {
		RelativeLayout relativeLayout = new RelativeLayout(context);
		ImageView image = new ImageView(context);
		ImageView num = new ImageView(context);

		image.setBackgroundResource(icons[index]);

		switch (iconNum) {
		case 1:
			num.setBackgroundResource(nums[0]);
			break;
		case 2:
			num.setBackgroundResource(nums[1]);
			break;
		case 3:
			num.setBackgroundResource(nums[2]);
			break;
		case 4:
			num.setBackgroundResource(nums[3]);
			break;
		}

		RelativeLayout.LayoutParams numParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		numParams.setMargins(8, 8, 0, 0);
		relativeLayout.addView(image);
		relativeLayout.addView(num, numParams);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(5, 0, 0, 0);
		recordLayout.addView(relativeLayout, layoutParams);
	}

	public static int toInt(String string) {
		int num = 0;
		if (string != null) {
			if (!string.equals("")) {
				num = Integer.parseInt(string);
			}
		}
		return num;
	}

	/**
	 * 把金额格式化成"*元"
	 * 
	 * @param money
	 * @return
	 */
	public static String formatMoney(String money) {
		StringBuffer formatMoney = new StringBuffer();
		formatMoney.append(PublicMethod.toYuan(money));
		formatMoney.append("元");
		return formatMoney.toString();
	}

	/**
	 * 判断是否是模拟器
	 * 
	 * @return
	 */
	public static boolean isEmulator() {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec("/system/bin/cat /proc/cpuinfo");
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		InputStream in = process.getInputStream();
		BufferedReader boy = new BufferedReader(new InputStreamReader(in));
		String mystring = null;
		try {
			mystring = boy.readLine();
			while (mystring != null) {
				mystring = mystring.trim().toLowerCase();
				if ((mystring.startsWith("hardware"))
						&& mystring.endsWith("goldfish")) {
					return true;
				}
				mystring = boy.readLine();
			}

		} catch (IOException e) {
			return false;
		}

		return false;
	}

	public static String getzhumainfo(String lotno, int beishu, String bet_code) {
		String zhuma = "";
		String beishuzhuma = "";
		if (beishu < 10) {
			beishuzhuma = "0" + beishu;
		} else {
			beishuzhuma = beishu + "";
		}
		if (lotno.equals("F47104")) {
			zhuma = "00" + beishuzhuma + bet_code;
		} else if (lotno.equals("F47102")) {
			zhuma = "00" + beishuzhuma + bet_code;
		} else if (lotno.equals("F47103")) {
			zhuma = "20" + beishuzhuma + bet_code;
		} else {
			zhuma = bet_code;
		}
		return zhuma;
	}

	/**
	 * gzip
	 * 
	 * @param data
	 * @return byte[]
	 */
	public static byte[] decompress2(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}

	// 检查是否11位电话号码
	public static boolean isphonenum(String phonenum) {
		Pattern p = Pattern.compile("^\\d{11}");
		Matcher m = p.matcher(phonenum);
		return m.matches();
	}

	// 检查是否数字
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 返回总注数竞彩多串过关投注计算注数
	 * 
	 * @param teamNum
	 *            多串过关3*3 teamNum = 3
	 * @param select
	 *            2*1 select=2
	 * @return 将几场比赛分成几组
	 */
	public static int getDouZhushu(int teamNum, List<String> betcodes,
			int select, List<Boolean> isDanList, int isDanNum) {
		// 初始化原始数据
		int[] a = new int[betcodes.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[teamNum];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		combine(a, a.length, teamNum, b, teamNum, list);
		int resultInt = 0;
		for (int[] result : list) {
			List<String> betcode = new ArrayList<String>();
			int danNum = 0;
			for (int p : result) {
				betcode.add(betcodes.get(p));
				if (isDanNum > 0 && isDanList.get(p)) {
					danNum++;
				}
			}
			if (isDanNum == 0 || danNum == isDanNum) {
				resultInt += getAllAmt(betcode, select, isDanList, 0);
			}
		}

		return resultInt;
	}

	/**
	 * 返回总注数竞彩自由过关投注计算注数
	 * 
	 * @param betcodes
	 * @param select
	 * @return
	 */
	public static int getAllAmt(List<String> betcodes, int select,
			List<Boolean> isDanList, int isDanNum) {
		// 初始化原始数据
		int[] a = new int[betcodes.size()];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		// 接收数据
		int[] b = new int[select];

		List<int[]> list = new ArrayList<int[]>();

		// 进行组合
		combine(a, a.length, select, b, select, list);

		// 返回数据对象
		int resultInt = 0;
		for (int[] result : list) {
			int itemNum = 1;
			int danNum = 0;
			for (int p : result) {
				itemNum *= Integer.parseInt(betcodes.get(p));
				if (isDanNum > 0 && isDanList.get(p)) {
					danNum++;
				}
			}
			if (isDanNum == 0 || danNum == isDanNum) {
				resultInt += itemNum;
			}
		}

		return resultInt;
	}

	public static int getDanAAmt(List<String> betcodes) {
		int zhushu = 0;
		for (int i = 0; i < betcodes.size(); i++) {
			zhushu += Integer.valueOf(betcodes.get(i));
		}

		return zhushu;
	}

	/**
	 * 组合的递归算法
	 * 
	 * @param a
	 *            原始数据
	 * @param n
	 *            原始数据个数
	 * @param m
	 *            选择数据个数
	 * @param b
	 *            存放被选择的数据
	 * @param M
	 *            常量，选择数据个数
	 * @param list
	 *            存放计算结果
	 */
	public static void combine(int a[], int n, int m, int b[], final int M,
			List<int[]> list) {
		for (int i = n; i >= m; i--) {
			b[m - 1] = i - 1;
			if (m > 1)
				combine(a, i - 1, m - 1, b, M, list);
			else {
				int[] result = new int[M];
				for (int j = M - 1; j >= 0; j--) {
					result[j] = a[b[j]];
				}
				list.add(result);
			}
		}
	}

	/**
	 * 提现时金额输入框保留两位小数
	 */
	public static TextWatcher twoDigitsDecimal = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable edt) {
			// TODO Auto-generated method stub
			String temp = edt.toString();
			int posDot = temp.indexOf(".");
			if (posDot <= 0)
				return;
			if (temp.length() - posDot - 1 > 2) {
				edt.delete(posDot + 3, posDot + 4);
			}
		}
	};

	/**
	 * 将dip转换成px
	 * 
	 * @param dip
	 * @return
	 */
	public static int getPxInt(float dip, Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, context.getResources().getDisplayMetrics());
	}
	
	/**
	 * 设置listview 高度
	 * @param listView
	 * @param dip
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView, int dip, Context context) {
		ListAdapter listAdapter; // 取得listview绑定的适配器
		if (listView.getAdapter() == null) {
			return;
		}
		listAdapter = listView.getAdapter();
		ViewGroup.LayoutParams params = listView.getLayoutParams(); // 取得listview所在布局的参数
		params.height = PublicMethod.getPxInt(dip, context)
				* (listAdapter.getCount());
		listView.setLayoutParams(params); // 改变listview所在布局的参数
	}

	/**
	 * 将字符串中的“\n”转化为“<br>
	 * ”用于字符串的Html的换行操作
	 * 
	 * @param str
	 * @return
	 */
	public static String repleaceNtoBR(String str) {
		String brString = "";
		brString = str.replaceAll("\n", "<br>");
		return brString;
	}

	/**
	 * 打开网址
	 * 
	 * @param cx
	 *            本地的context
	 * @param a
	 *            网址字符串
	 */
	public static void openUrlByString(Context cx, String a) {
		Uri myUri = Uri.parse(a);
		Intent returnIt = new Intent(Intent.ACTION_VIEW, myUri);
		cx.startActivity(returnIt);
	}

	/**
	 * 得到一个没有0.0的都变了数组
	 * 
	 * @param array
	 * @return
	 */
	public static double[] getDoubleArrayNoZero(double[] array) {
		Arrays.sort(array);
		double firstNzero = 0.0;
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			double aa = array[i];
			if (aa != 0.0) {
				firstNzero = aa;
				index = i;
				break;
			}
		}

		double[] result = new double[array.length - index];
		for (int i = 0; i < array.length - index; i++) {
			result[i] = array[index + i];
		}
		return result;
	}

	public static void setTextColor(TextView text, String content, int start,
			int end, int color) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(content);
		builder.setSpan(new ForegroundColorSpan(color), start, end,
				Spanned.SPAN_COMPOSING);
		text.setText(builder, BufferType.EDITABLE);
	}

	public static void setEditOnclick(final EditText mTextBeishu,
			final SeekBar mSeekBarBeishu, final Handler handler) {
		mTextBeishu.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable edit) {
				String text = edit.toString();
				int mTextNum = 1;
				if (text != null && !text.equals("")) {
					mTextNum = Integer.parseInt(text);
					int mSeekBar = mSeekBarBeishu.getMax();
					if (mTextNum > mSeekBar) {
						mTextBeishu.setText("" + mSeekBar);
						mSeekBarBeishu.setProgress(mSeekBar);
					} else {
						mSeekBarBeishu.setProgress(mTextNum);
					}
				} else {
					setValueThread(mTextBeishu, handler, 1);

				}
				mTextBeishu.setSelection(mTextBeishu.length());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

	}

	public static void setEditOnclick(final EditText mTextBeishu,
			final int minInt, final int maxInt, final Handler handler) {
		mTextBeishu.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable edit) {
				String text = edit.toString();
				int mTextNum = 1;
				if (text != null && !text.equals("")) {
					mTextNum = Integer.parseInt(text);
					if (mTextNum < minInt) {
						setValueThread(mTextBeishu, handler, minInt);
					} else if (mTextNum > maxInt) {
						mTextBeishu.setText("" + maxInt);
					}
				} else {
					setValueThread(mTextBeishu, handler, minInt);

				}
				mTextBeishu.setSelection(mTextBeishu.length());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}

	public static void setValueThread(final EditText mTextBeishu,
			final Handler handler, final int minInt) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mTextBeishu.post(new Runnable() {
					public void run() {
						String text = mTextBeishu.getText().toString();
						if (text.equals("")) {
							mTextBeishu.setText("" + minInt);
						} else if (Integer.parseInt(text) < minInt) {
							mTextBeishu.setText("" + minInt);
						}
					}
				});
			}
		}).start();
	}

	/**
	 * 格式化开奖号码
	 */
	public static String formatNum(String iNumbers, int num) {
		String iShowNumber = "";
		int length = iNumbers.length() / num;
		for (int i = 0; i < length; i++) {
			iShowNumber += iNumbers.substring(i * num, i * num + num);
			if (i != length - 1) {
				iShowNumber += ",";
			}
		}
		return iShowNumber;

	}

	/**
	 * 格式化时时彩开奖号码
	 * 
	 * @param iNumbers
	 *            开奖号码字符串
	 * @param num
	 *            号码位数
	 * @return 格式化后号码
	 */
	public static String formatSSCNum(String iNumbers, int num) {
		String iShowNumber = "";
		String singleOrSmall = "";
		int length = iNumbers.length() / num;
		for (int i = 0; i < length; i++) {
			String number = iNumbers.substring(i * num, i * num + num);
			iShowNumber += number;
			if (i == length - 1 || i == length - 2) {
				singleOrSmall += judgeBigSmallOrSigleDouble(Integer
						.valueOf(number));
				if (i == length - 2) {
					singleOrSmall += ",";
				}
			}
			if (i != length - 1) {
				iShowNumber += ",";
			}
		}
		return iShowNumber + "  " + singleOrSmall;
	}

	/**
	 * 判断该数字的大小单双
	 * 
	 * @param num
	 *            被判断的数组
	 * @return 大小单双字符串结果
	 */
	private static String judgeBigSmallOrSigleDouble(Integer num) {
		String result = "";

		if (num >= 5) {
			result += "大";
		} else {
			result += "小";
		}

		if (num % 2 == 0) {
			result += "双";
		} else {
			result += "单";
		}

		return result;
	}

	public static String getNewString(int length, String str) {
		String returnStr = "";
		if (str.length() > length) {
			returnStr = str.substring(0, length) + "***";
		} else {
			returnStr = str;
		}
		return returnStr;

	}

	public static void setTextColor(TextView text, int startInt, int endInt,
			String textStr, int color) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(textStr);
		builder.setSpan(new ForegroundColorSpan(color), startInt, endInt,
				Spanned.SPAN_COMPOSING);
		text.setText(builder, BufferType.EDITABLE);
	}

	/**
	 * 获得指定彩票信息
	 * 
	 * @param loto
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject getJsonObjectByLoto(String loto)
			throws JSONException {
		if (Constants.todayjosn == null)
			return null;
		JSONArray jsonArray = Constants.todayjosn.getJSONArray("result");
		JSONObject jsonObject = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if (!jsonObject.isNull(loto)) {
				return new JSONObject(jsonObject.getString(loto));
			}
		}
		return null;
	}

	/**
	 * 获得彩种的提示信息
	 * 
	 * @param mContext
	 * @param shellRW
	 * @param lotno
	 * @return
	 */
	public static String getMessageByLoto(Context mContext,
			RWSharedPreferences shellRW, String lotno) {
		String message = "";
        if (lotno.equals(Constants.TWENTYBEL)) {
			if (shellRW.getStringValue(Constants.TWENCLOSED).equals("true")) {
				message = mContext.getResources().getString(
						R.string.twentyClosedMessage);
			}
		}
		return message;
	}
	/**
	 * 获得资源文件
	 * @param mContext
	 * @param id
	 * @return
	 */
    public static String getResourcesMes(Context mContext,int id) {
    	return mContext.getResources().getString(id);
    }
	/**
	 * 显示消息
	 * 
	 * @param mContext
	 * @param message
	 */
	public static void showMessage(final Context mContext, String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}
	
	public static String getEndTime(String date) {
		int index = date.indexOf(":");
		date = date.substring(index - 2, index + 3);
		return date;
	}

	/** add by pengcx 20130604 start */
	/**
	 * 判断邮箱的正确性
	 * 
	 * @return 返回是否正确标识
	 */
	public static boolean isRightEmail(String email) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}
	/** add by pengcx 20130604 end */
	
	/**add by yejc 20130718 start*/
	public static void setTextViewContent(Activity activity) {
		TextView textView = (TextView)activity.findViewById(R.id.account_recharge_user_number);
		String userName = new RWSharedPreferences(activity, "addInfo").getStringValue(ShellRWConstants.USERNAME);
		textView.setText(userName);
	}
	/**add by yejc 20130718 end*/
	
	public static double[] ListToArray(List list) {
		double[] array = new double[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			array[i] = (Double) list.get(i);
		}
		
		return array;
	}
	
	/**
	 * 判断充值金额是否满足条件
	 * @param editText
	 * @param context
	 * @return
	 */
	public static boolean isRecharge(String amount, Context context) {
		if ("".equals(amount.trim())) {
			Toast.makeText(context, "请输入充值金额！",Toast.LENGTH_SHORT).show();
			return true;
		} else if("0".equals(amount.trim())) {
			Toast.makeText(context, "充值金额不能为0！",Toast.LENGTH_SHORT).show();
			return true;
		}  else {
			return false;
		}
	}
	
	/**add by yejc 20130818 start*/
	/**
	 * 格式化文本内容到html格式
	 * @param text
	 * @param colorValue
	 * @return
	 */
	public static String stringToHtml(String text, String colorValue) {
		String html = "<font color="+colorValue+">"+text+"<//font>";
		return html;
	}
	
	public static String stringToHtml(String text, String colorValue, String fontSize) {
		String html = "<font size="+fontSize+" color="+colorValue+">"+text+"<//font>";
		return html;
	}
	
	public static boolean isFiveLeague(String league) {
		String[] fiveLeague = {"意甲", "英超", "西甲", "德甲", "法甲"};
		for (int i = 0; i < fiveLeague.length; i++) {
			if (league.contains(fiveLeague[i])) {
				int index = league.indexOf(fiveLeague[i]);
				if (!isChinese(league.substring(0, index))
						&& !isChinese(league.substring(index+2, league.length()))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**判断是否是中文*/
	public static boolean isChinese(String str) {
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
			if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
					|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
					|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
					|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				return true;
			}
		}
		return false;
	}
	/**add by yejc 20130818 end*/
	
	public static void setLayoutHeight(int dip, LinearLayout layout, Context context) {
		ViewGroup.LayoutParams params = layout.getLayoutParams();
		params.height = getPxInt(dip, context);
		layout.setLayoutParams(params);
	}


	/**add by pengcx 20130808 start*/
	/**
	 * 格式化内蒙快三开奖号码
	 * 
	 * @param iNumbers
	 *            开奖号码
	 * @param i
	 *            每个开奖号码的位数
	 * @return 显示的开奖号码
	 */
	public static String formatNMK3Num(String iNumbers, int num) {
		String iShowNumber = "";
		int sumValue = 0;
		
		int length = iNumbers.length() / num;
		for (int i = 0; i < length; i++) {
			String chileNumber = iNumbers.substring(i * num + 1, i * num + num);
			sumValue += Integer.valueOf(chileNumber);
			iShowNumber += chileNumber;
			if (i != length - 1) {
				iShowNumber += ",";
			}
		}
		
		if (sumValue >= 10) {
			iShowNumber += "    和值" + sumValue;
		}else{ 
			iShowNumber += "    和值" + PublicMethod.isTenSpace(sumValue);
		}
		
		
		return iShowNumber;
	}
	/**add by pengcx 20130808 end*/
	public static String formatLongToTimeStr(Long l) {
		int hour = 0;
		int minute = 0;
		int second = 0;
		second = l.intValue();
		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		return (String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String
				.valueOf(second));
	}
	
	public static View getView(Context context) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.progress_dialog_view, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.progress_dialog_window_anim);  
		LinearInterpolator lin = new LinearInterpolator();  
		anim.setInterpolator(lin);  
		imageView.startAnimation(anim);
		
		return view;
	}
	
	public static ProgressDialog creageProgressDialog(Context context) {
		ProgressDialog mProgressdialog = new ProgressDialog(context);
		mProgressdialog.show();
//		mProgressdialog.setCancelable(false);
		View dialogView = getView(context);
		mProgressdialog.getWindow().setContentView(dialogView);
		return mProgressdialog;
	}
	
	public static PopupWindow createPopupWindow(Context context, String[] info,
			int count) {
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflate.inflate(R.layout.buy_join__window, null);
		GridView GridView = (GridView) layout.findViewById(R.id.gridView);
		GridView.setNumColumns(count);
		return null;
	}
	
	
	public static String getWeek(int i){
		String week="星期";
		switch (i) {
		case 1:
			week+="一";
			break;
		case 2:
			week+="二";
			break;
		case 3:
			week+="三";
			break;
		case 4:
			week+="四";
			break;
		case 5:
			week+="五";
			break;
		case 6:
			week+="六";
			break;
		case 7:
			week+="日";
			break;
		default :
			break;
		}
		return week;
	}
}
