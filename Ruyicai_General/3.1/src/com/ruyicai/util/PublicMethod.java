
package com.ruyicai.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.net.newtransaction.SoftwareUpdateInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;


/**
 * 共用方法类
 */

public class PublicMethod {
	public final static int MAXICON = 8; 
	public static int icons[]={R.drawable.crown,R.drawable.cup,R.drawable.diamond,R.drawable.star};
    /**
     * 求组合
     * @param m 每一注小球个数
     * @param n 真实小球个数(即点击小球的个数)
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
	 *  求阶乘
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
	 *  检查数组碰撞
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
	 *  获取 多个随机数
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
	 * 获取当前页面的屏幕高度
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
	 *  获取当前页面的屏幕宽度
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
	 *  修改余额格式
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
	 *  发短信
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

	/**
	 * 输出信息
	 * @param tag
	 * @param msg
	 */
	public static void myOutLog(String tag,String msg) {
//           Log.e(tag, msg);
	}

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
	 *  获得当前期和截止时间
	 * @param string
	 * @param context
	 * @return
	 */
	public static String[] getLotno(String string, Context context) {
		
		return null;

	}

	/**
	 *  排序
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
	 * @param num
	 * @return
	 */
	public static String getZhuMa(int num) {
		String str="";

		if (num < 10) {
			str = "0" + num;
		} else {
			str = "" + num;
		}
		return str;

	}

    public static String getbigsmalZhumastr(int num){
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
	 * @param string 显示框信息
	 * @return
	 */

	public static void alertJiXuan(String string, Context context) {
		Builder dialog = new AlertDialog.Builder(context).setTitle("请选择号码")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}

						});
		dialog.show();

	}

	/**
	 * 获取当前期号信息
	 * @param lotno
	 * @return
	 */
	public static JSONObject getCurrentLotnoBatchCode(String lotno) {
		try {
			return Constants.currentLotnoInfo.getJSONObject(lotno);
		} catch (Exception e) {
			return null;
		}
	}
	

	/**
	 * 联网获取期号
	 * @param lotno
	 * @param term
	 * @param time
	 * @param updateIssueHandler
	 */
	public static void getIssue(final String lotno,final TextView term,final TextView time,final Handler updateIssueHandler){
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					term.setText("期号获取中....");
					JSONObject softupdateObj = new JSONObject(SoftwareUpdateInterface.getInstance().softwareupdate(null));
					Constants.currentLotnoInfo = softupdateObj.getJSONObject("currentBatchCode");// 获取网络的期号信息
					JSONObject temp_obj = Constants.currentLotnoInfo.getJSONObject(lotno);
					final String issueStr = temp_obj.getString("batchCode");
					final String timeStr = temp_obj.getString("endTime");
					updateIssueHandler.post(new Runnable() {
						public void run() {
							term.setText("第" + issueStr + "期");
							time.setText("截止时间：" + timeStr);
						}
					});// 获取成功
				} catch (Exception e) {
					e.printStackTrace();
					updateIssueHandler.post(new Runnable() {
						public void run() {
							term.setText("期号获取失败");
						}
					});// 获取成功
				}
			}
		});
		t.start();
	}
//	/**
//	 * 打开网址
//	 * @param cx 本地的context
//	 * @param a  网址字符串
//	 */
//	public static void openUrlByString(Context cx, String a) {
//		Uri myUri = Uri.parse(a);
//		Intent returnIt = new Intent(Intent.ACTION_VIEW, myUri);
//		cx.startActivity(returnIt);
//	}
//    
//	
   /**
    * 投注成功后显示Dialog
    * @param context
    */
	public static void showDialog(final Context context,final String shareCode){
	    LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflate.inflate(R.layout.touzhu_succe,null);
	    final AlertDialog dialog = new AlertDialog.Builder(context).create();
	    ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
	    Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
	    Button share = (Button) view.findViewById(R.id.touzhu_succe_button_share);
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
				Intent intent=new Intent(Intent.ACTION_SEND);  
				intent.setType("text/plain");  
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享给朋友");  
				intent.putExtra(Intent.EXTRA_TEXT, "我刚刚使用如意彩手机客户端购买了一张彩票:"+shareCode+"快来参与吧！官网www.ruyicai.com");  
				context.startActivity(Intent.createChooser(intent,"请选择分享方式")); 
			}
		});
	   
        dialog.show();
        dialog.getWindow().setContentView(view);
       

	}
	   /**
	    * 投注成功后显示Dialog
	    * @param context
	    */
		public static void showDialog(final Context context){
		    LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View view = inflate.inflate(R.layout.touzhu_succe,null);
		    final AlertDialog dialog = new AlertDialog.Builder(context).create();
		    ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
		    Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		    Button share = (Button) view.findViewById(R.id.touzhu_succe_button_share);
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
	/**
	 * 创建机选Table
	 * type 0为时时彩,1位其他
	 */
	public static TableLayout makeBallTableJiXuan(TableLayout tableLayout, int aFieldWidth, int[] aResId,int[] iTotalRandoms,Context context) {
		// int iBallNum = aBallNum;
		TableLayout table ;
		if(tableLayout == null){
			table = new TableLayout(context);
		}else{
			table = tableLayout;
		}
	
		int iBallNum = iTotalRandoms.length;
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int viewNumPerLine = 9;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth)/viewNumPerLine-2;
		
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);
			for (int col = 0; col < viewNumPerLine; col++) {
				int num = iTotalRandoms[(row * viewNumPerLine) + col] ;
				String iStrTemp = "" + num;
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
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
				int num =  iTotalRandoms[iBallViewNo] ;

				
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
	 * 创建机选Table,大小双单专用
	 * type 0为时时彩,1位其他
	 */
	public static TableLayout makeBallTableJiXuanbigsmall(TableLayout tableLayout, int aFieldWidth, int[] aResId,int[] iTotalRandoms,Context context) {
		// int iBallNum = aBallNum;
		TableLayout table ;
		if(tableLayout == null){
			table = new TableLayout(context);
		}else{
			table = tableLayout;
		}
	
		int iBallNum = iTotalRandoms.length;
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int viewNumPerLine = 8;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth)/viewNumPerLine-2;
		
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
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
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
	 * @param LinearLayout      aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	public static BallTable makeBallTable(TableLayout tableLayout, int aFieldWidth,int aBallNum, int[] aResId, int aIdStart, int aBallViewText,
	    Context context,OnClickListener onclick,int isNum) {
		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart,context);
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
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)* viewNumPerLine) / 2;
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
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
				tempBallView.setOnClickListener(onclick);
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
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
				tempBallView.setOnClickListener(onclick);
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
	 * 创建自选BallTable和遗漏值
	 * @param LinearLayout      aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	public static BallTable makeBallTable(TableLayout tableLayout, int aFieldWidth,int aBallNum, int[] aResId, int aIdStart, int aBallViewText, boolean isplw,
	    Context context,String num) {
		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart,context);
		int iBallNum = aBallNum;
		int viewNumPerLine = 0;
        if(isplw){
        	viewNumPerLine=10;	
        }else{
            viewNumPerLine = 8;
        }
		// 定义没行小球的个数为7
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine - 2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		iBallTable.lineNum = lineNum;
		iBallTable.lieNum = viewNumPerLine;
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
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				iBallViewNo++;
				tableRow.addView(tempBallView, lp);
				TextView textView = new TextView(context);
				textView.setText(num);
				textView.setGravity(Gravity.CENTER);
				tableRowText.addView(textView, lp);
				if(iBallTable.textView == null){
					iBallTable.textView = textView;
				}
			}
			tabble.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(PublicConst.WC, PublicConst.WC));
			
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
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				iBallViewNo++;
				tableRow.addView(tempBallView, lp);
				TextView textView = new TextView(context);
				textView.setText(""+iBallViewNo);
				textView.setGravity(Gravity.CENTER);
				tableRowText.addView(textView, lp);
			}
			// 新建的TableRow添加到TableLayout
			tabble.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}
	    
	/**
	 * 中奖查询，投注查询注码解析
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

					betCode += (GT.makeString("F47104", wayCode, mp[i]
							.substring(4)) + "\n");
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
				String danma = GT.makeString("F47104", wayCode, betcode.substring(4, index1));
				String tuoma = GT.makeString("F47104", wayCode, betcode.substring(index1 + 1, index2));
				String lanqiu = GT.makeString("F47104", wayCode, betcode.substring(index2 + 1, betcode.length() - 1));
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

				String redball = GT.makeString("F47104", wayCode, betcode
						.substring(5, index1));
				String blueball = GT.makeString("F47104", wayCode, betcode
						.substring(index1 + 1, betcode.length() - 1));

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
				String danma = GT.makeString("F47103", wayCode, betcode
						.substring(4, index1));
				String tuoma = GT.makeString("F47103", wayCode, betcode
						.substring(index1 + 1, betcode.length() - 1));
				betCode = "胆码: " + danma + "\n" + "拖码: " + tuoma + "\n";
				// 单选复式码解析 2010/7/5 陈晨
			} else if (wayCode == 00) {
				// 3D单选注码格式 解析 
				lotNo = "福彩3D直选";
				String mp[] = GT.splitBetCode(betcode);
				betcode = "";
				for (int i = 0; i < mp.length; i++) {
					betCode += (GT.makeString("F47103", wayCode, mp[i].substring(4)) + "\n");
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
				String baiwei = GT.makeString("F47103", wayCode, betcode
						.substring(6, index1 + 1));
				String shiwei = GT.makeString("F47103", wayCode, betcode
						.substring(index1 + 3, index2));
				String gewei = GT.makeString("F47103", wayCode, betcode
						.substring(index2 + 3, betcode.length() - 1));
				betCode = "百位: " + baiwei + "\n" + "十位: " + shiwei + "\n"+ "个位: " + gewei + "\n";
				// 3D直选单式 注码解析
			}

			else {
				if (wayCode == 01) {
					lotNo = "福彩3D组3";
				} else if (wayCode == 02||wayCode == 32) {
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
					betCode += (GT.makeString("F47103", wayCode, mp[i].substring(4)) + "\n");
				}
			}
		} else if (play_name.equals("QL730") || play_name.equals("F47102")) {
			if (wayCode == 00) {
				int index_q;
				String mp[] = GT.splitBetCode(betcode);
				lotNo = "七乐彩单式";
				betCode = "";
				for (int i = 0; i < mp.length; i++) {
					betCode += (GT.makeString("F47102", wayCode, mp[i].substring(4)) + "\n");
				}
			} else if (wayCode == 10) {
				lotNo = "七乐彩复式";
				betCode = GT.makeString("F47102", wayCode, betcode.substring(5,
						betcode.length() - 1))
						+ "\n";
			} else if (wayCode == 20) {
				lotNo = "七乐彩胆拖";
				int index1 = 0;
				for (int i = 0; i < betcode.length(); i++) {
					if (betcode.charAt(i) == '*') {
						index1 = i;
					}
				}

				String danma = GT.makeString("F47102", wayCode, betcode.substring(4, index1));
				String tuoma = GT.makeString("F47102", wayCode, betcode.substring(index1 + 1, betcode.length() - 1));
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

					} else if (checkType[0].length() != 14|| checkType[1].length() != 5) {
						if(checkType[1].contains("-")){
							lotNo = "超级大乐透直选";
							String strs[]=betcode.split(";");
							for(int i=0;i<strs.length;i++){
								String str[]=strs[i].split("-");
								betCode +=str[0]+" | "+str[1]+"\n";		
							}
						}else{
							lotNo = "超级大乐透复式";
							betCode = checkType[0] + " | " + checkType[1] + "\n";
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
					String strs[]=betcode.split(";");
					for(int i=0;i<strs.length;i++){
						betCode +=strs[i]+"\n";		
					}
				}
			}
		} else if (play_name.equals("T01002") || play_name.equals("PL3_33")) {
			String[] betcodes=betcode.split("\\;");
			for(int m=0;m<betcodes.length;m++){
			   String[] checkType = new String[2];
			/*
			 * try{ JSONObject obj = jsonArray.getJSONObject(index); betcode =
			 * obj.getString("betcode"); } catch (JSONException e) {
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
				String subStr =null;
				subStr = checkType[1];
				int[] subStrLast = new int[3];
				for (int i = 0; i < 3; i++) {
					subStrLast[i] = Integer.valueOf(subStr.substring(2 * i,
							2 * i + 1));
				}
				if (subStrLast[0] == subStrLast[1]
						|| subStrLast[1] == subStrLast[2]) {
					lotNo = "排列三组三";
					betCode += subStr+"\n" ;
				} else {
					lotNo = "排列三组六";
					betCode += subStr+"\n" ;
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
		}else if(play_name.equals("T01009")){
			lotNo = "七星彩";
		}else if(play_name.equals("T01008")){
			lotNo = "北京单场";
		}else if(play_name.equals("T01010")){
			lotNo = "11选5";
		}else if(play_name.equals("T01011")){
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
	public static String toLotno(String type){
		   String title = "";
      if(type!=null){
		if(type.equals(Constants.LOTNO_SSQ)){
			   title = "双色球";
		   }else if(type.equals(Constants.LOTNO_FC3D)){
			   title = "福彩3D";
		   }else if(type.equals(Constants.LOTNO_QLC)){
			   title = "七乐彩";
		   }else if(type.equals(Constants.LOTNO_PL3)){
			   title = "排列三";
		   }else if(type.equals(Constants.LOTNO_DLT)){
			   title = "大乐透";
		   }else if(type.equals(Constants.LOTNO_SFC)){
			   title = "胜负彩";
		   }else if(type.equals(Constants.LOTNO_JQC)){
			   title = "进球彩";
		   }else if(type.equals(Constants.LOTNO_LCB)){
			   title = "六场半";
		   }else if(type.equals(Constants.LOTNO_RX9)){
			   title = "任选九";
		   }else if(type.equals(Constants.LOTNO_QXC)){
			   title = "七星彩";
		   }else if(type.equals(Constants.LOTNO_SSC)){
			   title = "时时彩";
		   }else if(type.equals(Constants.LOTNO_11_5)){
			   title = "11选5";
		   }else if(type.equals(Constants.LOTNO_QXC)){
			   title = "七星彩";
		   }else if(type.equals(Constants.LOTNO_PL5)){
			   title = "排列五";
		   }else if(type.equals(Constants.LOTNO_JC)){
			   title = "竞彩足球";
		   }else if(type.equals(Constants.LOTNO_JCLQ)){
			   title = "竞彩篮球胜负";
		   }else if(type.equals(Constants.LOTNO_JCLQ_RF)){
			   title = "竞彩篮球让分胜负";
		   }else if(type.equals(Constants.LOTNO_JCLQ_SFC)){
			   title = "竞彩篮球胜分差";
		   }else if(type.equals(Constants.LOTNO_JCLQ_DXF)){
			   title = "竞彩篮球大小分";
		   }else if(type.equals(Constants.LOTNO_22_5)){
			   title = "22选5";
		   }else if(type.equals(Constants.LOTNO_eleven)){
			   title = "11运夺金";
		   }else if(type.equals("")){
			   title = "所有彩种";
		   }else{
			   title = "未知";
		   }
		}
		return title;
		
	}
	/**
	 * 获取期号
	 * @param type
	 * @return
	 */
	public static String toIssue(String type){
		JSONObject ssqLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(type);
		String issueStr="";
		if (ssqLotnoInfo != null) {
			// 成功获取到了期号信息
			try {
				issueStr = ssqLotnoInfo.getString("batchCode");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
		return issueStr;
	}
	/**
	 * 转换成分
	 * 
	 */
	public static String toFen(String amt){
		
		return Integer.toString(Integer.parseInt(amt)*100);
		
	}
	/**
	 * 带小数的金额转换成分
	 * 
	 */
	public static String doubleToFen(String amt){
		double amt2 = Double.parseDouble(amt)*100;
		return (int)(amt2)+"";
//		return String.valueOf(Integer.parseInt(amt)*100);
		
	}
	/**
	 * 转换成元
	 * 
	 */
	public static String toIntYuan(String amt){
		String money = "";
		try{
			money = Long.toString(Long.parseLong(amt)/100);
		}catch(Exception e){
			
		}
		return money;
		
	}
	/**
	 * 转换成元
	 * 
	 */
	public static String toYuan(String amt){
		DecimalFormat df1 = new DecimalFormat("###0.00");
		 String result = df1.format(Double.parseDouble(amt)/100);
		return result;
		
	}

	/**
	 * 将1转换成01
	 * @param time
	 * @return
	 */
	public static String isTen(int time){
		String timeStr = "";
		if(time<10){
			timeStr +="0"+time;
		}else{
			timeStr += time;
		}
		return timeStr;
	}
	
	/**
	 * 创建战绩效果图
	 * 
	 */
	public static void createStar(LinearLayout starNum,String crown,String cup,String diamond,String star,Context context){
		starNum.removeAllViews();
		int crownInt = toInt(crown);
		int cupInt = toInt(cup);
		int diamondInt = toInt(diamond);
		int starInt = toInt(star);

		for(int i=0;i<MAXICON;i++){
		 ImageView view = new ImageView(context);
			if(i<crownInt){
				view.setBackgroundResource(icons[0]);
			}else if(i<crownInt+cupInt){
				view.setBackgroundResource(icons[1]);
			}else if(i<crownInt+cupInt+diamondInt){
				view.setBackgroundResource(icons[2]);
			}else if(i<crownInt+cupInt+diamondInt+starInt){
				view.setBackgroundResource(icons[3]);
			}
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(5, 0, 0, 0);
			starNum.addView(view,param);
		}
	}
	public static int toInt(String string){
		int num = 0;
		if( string!=null){
			if(!string.equals("")){
				num = Integer.parseInt(string);
			}
		}
		return num;
	}
	/**
	 * 把金额格式化成"*元"
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


	
	public static String getzhumainfo(String lotno,int beishu,String bet_code){
		String zhuma = "";
		String beishuzhuma="";
		if(beishu<10){
		     beishuzhuma = "0"+beishu;	
		}else{
			 beishuzhuma =beishu+"";
		}
		if(lotno.equals("F47104")){
		zhuma = "00"+beishuzhuma+bet_code;
		}else if(lotno.equals("F47102")){
		zhuma = "00"+beishuzhuma+bet_code;
		}else if(lotno.equals("F47103")){
		zhuma = "20"+beishuzhuma+bet_code;
		}else {
		zhuma = bet_code;	
		}
		return zhuma;
	}
  
    /** 
     *   gzip
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
 //检查是否11位电话号码
    public static boolean isphonenum(String phonenum){
    	Pattern p = Pattern.compile("^\\d{11}");
    	Matcher m = p.matcher(phonenum);
    	return  m.matches();
    }
    /**
     * 返回总注数竞彩多串过关投注计算注数
     * @param teamNum 多串过关3*3 teamNum = 3
     * @param select 2*1 select=2
     * @return
     * 将几场比赛分成几组
     */
	public  static int getDouZhushu(int teamNum,List<String> betcodes, int select) {
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
		List<List> teams = new ArrayList<List>();
		for (int[] result : list) {
			List<String> betcode = new ArrayList<String>();
			for (int p : result) {
				betcode.add(betcodes.get(p));
			}
			teams.add(betcode);
		}
		int resultInt = 0; 
		for(List<String> team:teams){
			resultInt += getAllAmt(team,select);
		}
		return resultInt;
	}
   
    /**
     * 返回总注数竞彩自由过关投注计算注数
     * @param betcodes
     * @param select
     * @return
     */
	public  static int getAllAmt(List<String> betcodes, int select) {
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
			for (int p : result) {
				itemNum *= Integer.parseInt(betcodes.get(p));
			}
			resultInt += itemNum;
		}

		return resultInt;
	}
     public static int getDanAAmt(List<String> betcodes){
    	 int zhushu=0;
    	 for(int i=0;i<betcodes.size();i++){
    		 zhushu+=Integer.valueOf(betcodes.get(i));
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
	public static void combine(int a[], int n, int m, int b[], final int M,List<int[]> list) {
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
	public	static TextWatcher twoDigitsDecimal = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
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
	          if (posDot <= 0) return;
	          if (temp.length() - posDot - 1 > 2)
	          {
	              edt.delete(posDot + 3, posDot + 4);
	          }
		}
	};
	/**
	 * 将dip转换成px
	 * @param dip
	 * @return
	 */
	public static int getPxInt(int dip,Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
	}
	/**
	 * 将字符串中的“\n”转化为“<br>”用于字符串的Html的换行操作
	 * @param str
	 * @return
	 */
	public static String repleaceNtoBR(String str){
		String brString = "";
		brString = str.replaceAll("\n", "<br>");
		return brString;
	}
	/**
	 * 打开网址
	 * @param cx 本地的context
	 * @param a  网址字符串
	 */
	public static void openUrlByString(Context cx, String a) {
		Uri myUri = Uri.parse(a);
		Intent returnIt = new Intent(Intent.ACTION_VIEW, myUri);
		cx.startActivity(returnIt);
	}
}
