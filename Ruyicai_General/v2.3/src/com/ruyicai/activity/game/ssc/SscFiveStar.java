/**
 * 
 */
package com.ruyicai.activity.game.ssc;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.game.ssq.SsqActivity;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.activity.home.ScrollableTabActivity;
import com.ruyicai.dialog.Accoutdialog;
import com.ruyicai.handler.MyDialogListener;
import com.ruyicai.net.JrtLot;
import com.ruyicai.net.transaction.BettingInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;
import com.ruyicai.util.ShellRWSharesPreferences;
import com.ruyicai.view.OneBallView;
import com.ruyicai.view.RadioStateDrawable;
import com.ruyicai.view.TabBarButton;

/**
 * @author Administrator
 * 
 */
public class SscFiveStar extends Activity implements OnClickListener, // 小球被点击
		// onClick
		SeekBar.OnSeekBarChangeListener, // SeekBar改变 onProgressChanged
		RadioGroup.OnCheckedChangeListener, // 机选复选框变化 onCheckedChangeListener
		MyDialogListener {

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	// 进度条
	private static final int DIALOG1_KEY = 0;
	private ProgressDialog progressdialog;
	private ProgressDialog getlotdialog;
	private LinearLayout buyView;
	private int iScreenWidth;
    private boolean isjixuan=false;
	// 顶部
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private RadioGroup topButtonGroup;
	private int iButtonNum = 3;
	int topButtonIdOn[] = { R.drawable.goucai_b, R.drawable.jixuan_b,
			R.drawable.ssc_fivestartb };
	int topButtonIdOff[] = { R.drawable.goucai, R.drawable.jixuan,
			R.drawable.ssc_fivestart };

	SeekBar mSeekBarBeishu;
	SeekBar mSeekBarQishu;
	TextView mTextBeishu;
	TextView mTextQishu;
	TextView mTextSumMoney;
	ImageButton ssc_btn_touzhu;
	
	

	BallTable wanBallTable = null;
	BallTable qianBallTable = null;
	BallTable baiBallTable = null;
	BallTable shiBallTable = null;
	BallTable geBallTable = null;
	private int BallResId[] = { R.drawable.grey, R.drawable.red };
	public static final int WAN_BALL_START = 0x80000001; // 小球起始ID
	public static final int QIAN_BALL_START = 0x80000011; // 小球起始ID
	public static final int BAI_BALL_START = 0x80000021; // 小球起始ID
	public static final int SHI_BALL_START = 0x80000031; // 小球起始ID
	public static final int GE_BALL_START = 0x80000041; // 小球起始ID

	int iProgressBeishu = 1;
	int iProgressQishu = 1;

	private static final int BALL_WIDTH = 55; // 小球图的宽高

	private int iCurrentButton; // 首行button当前的位置
	private int dialogType = 0;
	private int iWanBallNumber;// 小球个数（机选）
	private int iQianBallNumber;// 小球个数（机选）
	private int iBaiBallNumber;// 小球个数（机选）
	private int iShiBallNumber;// 小球个数（机选）
	private int iGeBallNumber;// 小球个数（机选）
	private int mTimesMoney = 2;
	TextView issue;

	String batchCode;
	String endTime;
	String systime;
	Timer timer;
	int countMinute;
	int countSecond;
	
	private TextView title;
	private TextView term;
	
	
   private  EditText  waneditText;
   private  EditText  qianeditText;
   private  EditText  baieditText;
   private  EditText  shieditText;
   private  EditText  geeditText;
   
	private Spinner jixuanZhu;
	private LinearLayout zhuView;
	private Vector<Balls> balls = new Vector<Balls>();
	ImageButton ssq_b_touzhu_danshi;
	ImageButton ssq_b_touzhu_fushi;
	
	private boolean isOnclik=true;
	
	private SscSensor sensor=new SscSensor(this);
	/**
	 * 消息处理函数
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = msg.getData().getString("get");
			switch (msg.what) {
			case 0:
				break;
			case 1:
				progressdialog.dismiss();
				if(isFinishing() == false){
				    Accoutdialog.getInstance().createAccoutdialog(SscFiveStar.this, getResources().getString(R.string.goucai_Account_dialog_msg).toString());
				}
				// //需要添加AlertDialog提示注册成功
				break;
			case 2:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "该期已结束！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示用户已注册
				break;
			case 3:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "彩票投注中！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示系统结算，请稍后
				break;
			case 4:
				progressdialog.dismiss();
				if(isFinishing() == false){
					PublicMethod.showDialog(SscFiveStar.this);
				}
				switch(iCurrentButton){
				case  PublicConst.SSC_FIVE_STAR:
				
				if(isjixuan){
					zhuView.removeAllViews();
				}else{
				wanBallTable.clearAllHighlights();
				qianBallTable.clearAllHighlights();
				baiBallTable.clearAllHighlights();
				shiBallTable.clearAllHighlights();
				geBallTable.clearAllHighlights();
				waneditText.setText("");
				qianeditText.setText("");
				baieditText.setText("");
				shieditText.setText("");
				geeditText.setText("");
				changeTextSumMoney();
				}
				break;
				case PublicConst.SSC_FIVE_STAR_TONXXUAN:
					wanBallTable.clearAllHighlights();
					qianBallTable.clearAllHighlights();
					baiBallTable.clearAllHighlights();
					shiBallTable.clearAllHighlights();
					geBallTable.clearAllHighlights();
					waneditText.setText("");
					qianeditText.setText("");
					baieditText.setText("");
					shieditText.setText("");
					geeditText.setText("");
					changeTextSumMoney();
				}
				Intent intent = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent);

				break;
			// //需要添加AlertDialog提示该号被暂停请联系客服
			// case 5:
			// //需要添加AlertDialog注册失败
			// break;
			case 6:
				// //需要添加AlertDialog提示用户登录成功
				progressdialog.dismiss();
				if(isFinishing() == false){
				    PublicMethod.showDialog(SscFiveStar.this);
				}
				switch(iCurrentButton){
				case  PublicConst.SSC_FIVE_STAR:
				
				if(isjixuan){
					zhuView.removeAllViews();
				}else{
				wanBallTable.clearAllHighlights();
				qianBallTable.clearAllHighlights();
				baiBallTable.clearAllHighlights();
				shiBallTable.clearAllHighlights();
				geBallTable.clearAllHighlights();
				waneditText.setText("");
				qianeditText.setText("");
				baieditText.setText("");
				shieditText.setText("");
				geeditText.setText("");
				changeTextSumMoney();
				}
				break;
				case PublicConst.SSC_FIVE_STAR_TONXXUAN:
					wanBallTable.clearAllHighlights();
					qianBallTable.clearAllHighlights();
					baiBallTable.clearAllHighlights();
					shiBallTable.clearAllHighlights();
					geBallTable.clearAllHighlights();
					waneditText.setText("");
					qianeditText.setText("");
					baieditText.setText("");
					shieditText.setText("");
					geeditText.setText("");
					changeTextSumMoney();
				}
				Intent intent2 = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent2);
				break;
			case 7:
				progressdialog.dismiss();

				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(SscFiveStar.this,
						UserLogin.class);
				startActivity(intentSession);
				break;
			case 8:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示登录失败
				break;
			case 9:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败！", Toast.LENGTH_LONG)
						.show();
				break;
			case 10:
				// 将图片的背景设置回原来的图案表示可点击
				ssc_btn_touzhu.setImageResource(R.drawable.imageselecter);

				break;
			case 11:

				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "没有期号可以投注！", Toast.LENGTH_LONG)
						.show();
				break;
			case 12:// 显示当前期数

				String time[] = endTime.split(" ");
				String times[] = time[1].split(":");
				times(Integer.parseInt(times[0]), Integer.parseInt(times[1]),
						Integer.parseInt(times[2]));
				break;

			case 13:// 显示当前期数

				getlotdialog.dismiss();
				String minute = "";
				String second = "";
				if (countSecond > 0) {
					countSecond--;
				} else if (countSecond == 0 && countMinute > 0) {
					countMinute--;
					countSecond = 59;
				} else {
					timer.cancel();
				}
				if (countMinute < 10) {
					minute = "0";
				}
				if (countSecond < 10) {
					second = "0";
				}
				term.setText("第" + batchCode + "期" + "     剩余时间：" + minute
						+ countMinute + ":" + second + countSecond);
				break;
			case 14:// 改期已截止

				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "该期已经截止！", Toast.LENGTH_LONG)
						.show();
				break;

			}
		}
	};

	/**
	 * 入口函数
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ----- 加载框架 layout
		setContentView(R.layout.layout_main_buy);
		
//		TextView title = (TextView) findViewById(R.id.goucaitouzhu_title);
//		title.setText(getResources().getString(R.string.ssc_wuxing));
		title = (TextView) findViewById(R.id.layout_main_text_title);
		term = (TextView) findViewById(R.id.layout_main_text_time);
		Spinner ssc_zhonglei=(Spinner)findViewById(R.id.ssc_zhonglei_spinner);
		ssc_zhonglei.setVisibility(View.VISIBLE);
		ssc_zhonglei.setSelection(3, true);
		ssc_zhonglei.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:
					Intent intent = new Intent(SscFiveStar.this,SscZhiXuan.class);
					startActivity(intent);
					SscFiveStar.this.finish();
					break;
				case 1:
					Intent intent2 = new Intent(SscFiveStar.this,SscTwoStar.class);
					startActivity(intent2);
					SscFiveStar.this.finish();
					break;
				case 2:
					Intent intent3 = new Intent(SscFiveStar.this,SscThreeStar.class);
					startActivity(intent3);
					SscFiveStar.this.finish();
					break;
				case 4:
					Intent intent4 = new Intent(SscFiveStar.this,SscBigSmall.class);
					startActivity(intent4);
					SscFiveStar.this.finish();
					break;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		ImageButton flush = (ImageButton) findViewById(R.id.layout_ssc__flush);
		flush.setVisibility(ImageButton.VISIBLE);
		flush.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				getLotNo();
			}

		});
        
		getLotNo();
		// 获取主布局id
		buyView = (LinearLayout) findViewById(R.id.layout_buy);
	
		iCurrentButton = PublicConst.SSC_FIVE_STAR;
		initTopButtons();
		iCurrentButton = PublicConst.SSC_FIVE_STAR;
		createView(iCurrentButton);
	//	commit();
		
		
	}
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	sensor.stopAction();
    }
	/**
	 * 倒计时
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public void times(int endHour, int endMinute, int endSecond) {
	      Date date = new Date(new Long(systime));
	  	

	  	//	t.setToNow(); // 取得系统时间。
//	  		  int hour = date.hour + 8;// ;
	  		  int minute =date.getMinutes();
	  		  int second = date.getSeconds();

		// 计算剩余时间

		if (endSecond < second) {
			countSecond = endSecond - second + 60;
			endMinute--;
		} else {
			countSecond = endSecond - second;
		}
		if (endMinute < minute) {
			countMinute = endMinute - minute + 60;
			endHour--;
		} else {
			countMinute = endMinute - minute;
		}
		// if (endHour != hour && endHour != (hour + 12)) {
		// countMinute = 10;
		// countSecond = 0;
		// }
		TimerTask task = new TimerTask() {
			public void run() {
				Message message = new Message();
				message.what = 13;
				handler.sendMessage(message);
			}
		};
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer(true);
		timer.schedule(task, 1000, 1000);
	}

	/**
	 * 获得当前期号
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public String getLotNo() {
		getlotdialog = new ProgressDialog(this);
		// progressdialog.setTitle("Indeterminate");
		getlotdialog.setMessage("网络连接中...");
		getlotdialog.setIndeterminate(true);
		getlotdialog.setCancelable(true);
		getlotdialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				String re = "";

			
					re = JrtLot.getLotNo("T01007");
					if (!re.equalsIgnoreCase("")) {
						try {
							JSONObject obj = new JSONObject(re);
							error_code = obj.getString("error_code");
							batchCode = obj.getString("batchCode");
							endTime = obj.getString("end_time");
							systime = obj.getString("sys_current_time");
						} catch (JSONException e) {
						
						}
					} else {
					
					}
		
				

				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 8;
					handler.sendMessage(msg);

				} else if (error_code.equals("0000")) {
					Message msg = new Message();
					msg.what = 12;
					handler.sendMessage(msg);
				}
			}

		}).start();
		return null;
	}

	/**
	 * 初始化顶部标签
	 * 
	 */
	private void initTopButtons() {
		topButtonGroup = (RadioGroup) findViewById(R.id.topMenu);
		topButtonGroup.setOnCheckedChangeListener(this);
		topButtonLayoutParams = new RadioGroup.LayoutParams(/* 320/5 */64,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		commit();
	}

	/**
	 * 添加顶部按钮，并显示单式玩法View
	 * 
	 * @return void
	 */
	private void commit() {
		
		topButtonGroup.removeAllViews();
		if (isjixuan) {
			topButtonIdOn[1] = R.drawable.zhixuan_b;
			topButtonIdOff[1] = R.drawable.zhixuan;
		} else {
			topButtonIdOn[1] = R.drawable.jixuan_b;
			topButtonIdOff[1] = R.drawable.jixuan;
		}

		int optimum_visible_items_in_portrait_mode = iButtonNum;

		int screen_width = getWindowManager().getDefaultDisplay().getWidth();
		int width;
		width = screen_width / iButtonNum;
		RadioStateDrawable.other_width = width;
		RadioStateDrawable.other_screen_width = screen_width;

		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < iButtonNum; i++) {
			TabBarButton tabButton = new TabBarButton(this);
			tabButton.setState(topButtonIdOn[i], topButtonIdOff[i],3);
			tabButton.setId(i);
			tabButton.setGravity(Gravity.CENTER);
			topButtonGroup.addView(tabButton, i, topButtonLayoutParams);

		}
		 
	}
	public void changeImg(int iButtonNum, int[] topButtonIdon,
			int[] topButtonIdonoff, RadioGroup top) {
		top.removeViews(1,1);
		if (isjixuan) {
			topButtonIdOn[1] = R.drawable.zhixuan_b;
			topButtonIdOff[1] = R.drawable.zhixuan;
		} else {
			topButtonIdOn[1] = R.drawable.jixuan_b;
			topButtonIdOff[1] = R.drawable.jixuan;
		}
		
		int screen_width = getWindowManager().getDefaultDisplay().getWidth();
		int width = screen_width / iButtonNum;

		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		TabBarButton tabButton = new TabBarButton(this);

		tabButton.setState(topButtonIdon[1], topButtonIdonoff[1],3);
		tabButton.setId(1);
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		top.addView(tabButton, 1, topButtonLayoutParams);

	}
	/**
	 * 设置顶部按钮的位置
	 * 
	 * @param int index 顶部按钮的索引
	 * 
	 * @return void
	 */

    /* 
     * 
     * */
	/**
	 * 加载五星View 五星直选和通选通用
	 */
	public void createViewfivestar(){
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = null;
		int BallViewNum = 10;
		int BallViewWidth = SscFiveStar.BALL_WIDTH;
		iScreenWidth = PublicMethod.getDisplayWidth(this);
	    dialogType = PublicConst.SSC_FIVE_STAR;
			iV = (LinearLayout) inflate.inflate(R.layout.layout_ssc_five_star,
					null);

			wanBallTable = PublicMethod.makeBallTable(iV, R.id.ssc_table_wan, iScreenWidth,
					BallViewNum, BallResId, WAN_BALL_START, 0,this,this);

			qianBallTable = PublicMethod.makeBallTable(iV, R.id.ssc_table_qian,
					iScreenWidth, BallViewNum, BallResId,
					QIAN_BALL_START, 0,this,this);
			baiBallTable = PublicMethod.makeBallTable(iV, R.id.ssc_table_bai, iScreenWidth,
					BallViewNum,BallResId, BAI_BALL_START, 0,this,this);
			shiBallTable = PublicMethod.makeBallTable(iV, R.id.ssc_table_shi, iScreenWidth,
					BallViewNum, BallResId, SHI_BALL_START, 0,this,this);
			geBallTable = PublicMethod.makeBallTable(iV, R.id.ssc_table_ge, iScreenWidth,
					BallViewNum,BallResId, GE_BALL_START, 0,this,this);
			final TableLayout wan=(TableLayout)iV.findViewById(R.id.ssc_table_wan);
			final TableLayout qian=(TableLayout)iV.findViewById(R.id.ssc_table_qian);
			final TableLayout bai=(TableLayout)iV.findViewById(R.id.ssc_table_bai);
			final TableLayout shi=(TableLayout)iV.findViewById(R.id.ssc_table_shi);
			final TableLayout ge=(TableLayout)iV.findViewById(R.id.ssc_table_ge);
			final TextView    fivestarwan=(TextView)iV.findViewById(R.id.ssc_fivestarwan);
			final TextView    fivestarqian=(TextView)iV.findViewById(R.id.ssc_fivestarqian);
			final TextView    fivestarbai=(TextView)iV.findViewById(R.id.ssc_fivestarbai);
			final TextView    fivestarshi=(TextView)iV.findViewById(R.id.ssc_fivestarshi);
			final TextView    fivestarge=(TextView)iV.findViewById(R.id.ssc_fivestarge);
			qian.setVisibility(View.GONE);
			bai.setVisibility(View.GONE);
			shi.setVisibility(View.GONE);
			ge.setVisibility(View.GONE);
    	    waneditText=(EditText)iV.findViewById(R.id.ssc_zhixuanfivestarw_edit);
    	    qianeditText=(EditText)iV.findViewById(R.id.ssc_zhixuanfivestarq_edit);
    	    baieditText=(EditText)iV.findViewById(R.id.ssc_zhixuanfivestarb_edit);
    	    shieditText=(EditText)iV.findViewById(R.id.ssc_zhixuanfivestars_edit);
    	    geeditText=(EditText)iV.findViewById(R.id.ssc_zhixuanfivestarg_edit);
    	    
    	    waneditText.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus){
						v.setBackgroundResource(R.drawable.hongkuang);
						wan.setVisibility(View.VISIBLE);
						qian.setVisibility(View.GONE);
						bai.setVisibility(View.GONE);
						shi.setVisibility(View.GONE);
						ge.setVisibility(View.GONE);
						fivestarwan.setTextColor(Color.RED);
					}else{
						v.setBackgroundResource(R.drawable.huikuang);
						fivestarwan.setTextColor(Color.BLACK);
					}
						
				}
			});
            qianeditText.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus){
						v.setBackgroundResource(R.drawable.hongkuang);
						qian.setVisibility(View.VISIBLE);
						wan.setVisibility(View.GONE);
						bai.setVisibility(View.GONE);
						shi.setVisibility(View.GONE);
						ge.setVisibility(View.GONE);
						fivestarqian.setTextColor(Color.RED);
					}else{
						v.setBackgroundResource(R.drawable.huikuang);
						fivestarqian.setTextColor(Color.BLACK);
					}
						
				}
			});
           baieditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	
	    @Override
	    public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			v.setBackgroundResource(R.drawable.hongkuang);
			bai.setVisibility(View.VISIBLE);
			qian.setVisibility(View.GONE);
			wan.setVisibility(View.GONE);
			shi.setVisibility(View.GONE);
			ge.setVisibility(View.GONE);
			fivestarbai.setTextColor(Color.RED);
		}else{
			v.setBackgroundResource(R.drawable.huikuang);
			fivestarbai.setTextColor(Color.BLACK);
		}
			
	}
});
       shieditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			v.setBackgroundResource(R.drawable.hongkuang);
			shi.setVisibility(View.VISIBLE);
			qian.setVisibility(View.GONE);
			bai.setVisibility(View.GONE);
			wan.setVisibility(View.GONE);
			ge.setVisibility(View.GONE);
			fivestarshi.setTextColor(Color.RED);
		}else{
			v.setBackgroundResource(R.drawable.huikuang);
			fivestarshi.setTextColor(Color.BLACK);
		}
			
	}
});
     geeditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	
	@Override
	  public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			v.setBackgroundResource(R.drawable.hongkuang);
			ge.setVisibility(View.VISIBLE);
			qian.setVisibility(View.GONE);
			bai.setVisibility(View.GONE);
			shi.setVisibility(View.GONE);
			wan.setVisibility(View.GONE);
			fivestarge.setTextColor(Color.RED);
		}else{
			v.setBackgroundResource(R.drawable.huikuang);
			fivestarge.setTextColor(Color.BLACK);
		}
			
	}
      });
	     ImageButton	chongxuan = (ImageButton) iV.findViewById(R.id.ssc_jisuan_cb);
		 chongxuan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wanBallTable.clearAllHighlights();
				qianBallTable.clearAllHighlights();
				baiBallTable.clearAllHighlights();
				shiBallTable.clearAllHighlights();
                geBallTable.clearAllHighlights();
                waneditText.setText("");
                qianeditText.setText("");
                baieditText.setText("");
                shieditText.setText("");
                geeditText.setText("");
                changeTextSumMoney();
				
				
			}
		});
       

		{
//			TextView alert = (TextView) iV.findViewById(R.id.ssc_text_alert);
//			alert.setText(getResources().getString(R.string.ssc_title_alert));
			mTextSumMoney = (TextView) iV.findViewById(R.id.ssc_text_sum_money);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));
			mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.ssc_seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			iProgressBeishu = 1;
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV.findViewById(R.id.ssc_seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			iProgressQishu = 1;
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV.findViewById(R.id.ssc_text_beishu);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV.findViewById(R.id.ssc_text_qishu);
			mTextQishu.setText("" + iProgressQishu);
			/*
			 * 点击加减图标，对seekbar进行设置：
			 * 
			 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加
			 * -1表示减 final SeekBar mSeekBar
			 * 
			 * @return void
			 */
			setSeekWhenAddOrSub(R.id.ssc_seekbar_subtract_beishu, iV, -1,
					mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.ssc_seekbar_add_beishu, iV, 1,
					mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.ssc_seekbar_subtract_qihao, iV, -1,
					mSeekBarQishu, false);
			setSeekWhenAddOrSub(R.id.ssc_seekbar_add_qihao, iV, 1,
					mSeekBarQishu, false);

//			
			ssc_btn_touzhu = (ImageButton) iV.findViewById(R.id.ssc_btn_touzhu);
			ssc_btn_touzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu(); // 1表示当前为单式
				}
			});
		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));
		
	}
	public void createView(int Type) {
		switch (Type) {
		case PublicConst.SSC_FIVE_STAR_TONXXUAN:
			title.setText("时时彩五星通选");
			createViewfivestar();
			break;
		case PublicConst.SSC_FIVE_STAR:
			title.setText("时时彩五星直选");
			createViewfivestar();
		}
       
	}
	
	
	  /*
     * *  创建五星机选View
     *    
     */
	public void create_FiveStar_JiXuan(){
		buyView.removeAllViews();
     	balls = new Vector<Balls>();
     	title.setText("时时彩五星机选");
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_zhixuan_jixuan, null);
		jixuanZhu = (Spinner)iV.findViewById(R.id.layout_zhixuan_jixuan_spinner);
        jixuanZhu.setSelection(4);
        jixuanZhu.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int position = jixuanZhu.getSelectedItemPosition();
				if (isOnclik) {
					zhuView.removeAllViews();
				    balls = new Vector<Balls>();
					for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
						Balls ball = new Balls(5);
						ball.createBalls();
						balls.add(ball);
					}
					
					createTable(zhuView);
				} else {
					isOnclik = true;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
        
       zhuView = (LinearLayout) iV
  		.findViewById(R.id.layout_zhixuan_linear_zhuma);

      int index = jixuanZhu.getSelectedItemPosition() + 1;
      for (int i = 0; i < index; i++) {
	  Balls ball = new Balls(5);
	  ball.createBalls();
	  balls.add(ball);
}     
//      sensor.onVibrator();
      createTable(zhuView);
      
      mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.ssq_fushi_seek_beishu);
      mSeekBarBeishu.setOnSeekBarChangeListener(this);
	  iProgressBeishu = 1;

	  mSeekBarQishu = (SeekBar) iV.findViewById(R.id.ssq_fushi_seek_qishu);
	  mSeekBarQishu.setOnSeekBarChangeListener(this);
	  iProgressQishu = 1;

	  mTextBeishu = (TextView) iV.findViewById(R.id.ssq_fushi_text_beishu);
	  mTextBeishu.setText("" + iProgressBeishu);
	  mTextQishu = (TextView) iV.findViewById(R.id.ssq_fushi_text_qishu);
	  mTextQishu.setText("" + iProgressQishu);

		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_subtract_beishu, iV, -1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_add_beishu, iV, 1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_subtract_qihao, iV, -1,
				mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_add_qishu, iV, 1,
				mSeekBarQishu, false);

		ssq_b_touzhu_fushi = (ImageButton) iV
				.findViewById(R.id.ssq_fushi_b_touzhu);
		ssq_b_touzhu_fushi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		ImageButton jixuan = (ImageButton) iV
		.findViewById(R.id.jixuan_new_select);
        jixuan.setOnClickListener(new OnClickListener() {

	     @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		zhuView.removeAllViews();
		balls = new Vector<Balls>();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = new Balls(5);
			ball.createBalls();
			balls.add(ball);
		}
		createTable(zhuView);
	}
});


		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

		
		
	}
	
	
	/**
	 * 创建直选机选
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public void createTable(LinearLayout layout) {
		PublicMethod.recycleBallTable(wanBallTable);
		PublicMethod.recycleBallTable(qianBallTable);
		PublicMethod.recycleBallTable(baiBallTable);
		PublicMethod.recycleBallTable(shiBallTable);
		PublicMethod.recycleBallTable(geBallTable);

		for (int i = 0; i < balls.size(); i++) {
			final int index = i;
			int redBallViewWidth = SsqActivity.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			LinearLayout lines = new LinearLayout(layout.getContext());
			BallTable ballTablege = new BallTable(lines, GE_BALL_START);
			PublicMethod.makeBallTableJiXuanSSC(ballTablege, iScreenWidth, 
					BallResId, balls.get(i).getball(),this);
			
			ImageButton delet = new ImageButton(lines.getContext());

			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (balls.size() > 1) {
						zhuView.removeAllViews();
						balls.remove(index);
						isOnclik = false;
						jixuanZhu.setSelection(balls.size() - 1);
						createTable(zhuView);
					} else {
						Toast.makeText(
								SscFiveStar.this,
								getResources().getText(
										R.string.zhixuan_jixuan_toast),
								Toast.LENGTH_SHORT).show();

					}

				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(param.rightMargin, 13, 0, 0);
			lines.setGravity(Gravity.CENTER_HORIZONTAL);
			lines.addView(delet, param);
			layout.addView(lines);
		}

	}
	

  
	/**
	 * 响应小球被点击的回调函数
	 * 
	 * @param View
	 *            v 被点击的view
	 * 
	 * @return void
	 */
	@Override
	public void onClick(View v) {
	
		int iBallId = v.getId();
		// red ball
		if (iBallId >= WAN_BALL_START && iBallId < QIAN_BALL_START) {
			int iBallViewId = v.getId() - WAN_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(iCurrentButton, 0, iBallViewId);// 点红球组

			}
		} else if (iBallId >= QIAN_BALL_START && iBallId < BAI_BALL_START) {
			int iBallViewId = v.getId() - QIAN_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				PublicMethod.myOutput("----- blue:" + iBallViewId);
				changeBuyViewByRule(iCurrentButton, 1, iBallViewId);// 点蓝球组
			}
		} else if (iBallId >= BAI_BALL_START && iBallId < SHI_BALL_START) {
			int iBallViewId = v.getId() - BAI_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				PublicMethod.myOutput("----- blue:" + iBallViewId);
				changeBuyViewByRule(iCurrentButton, 2, iBallViewId);// 点蓝球组
			}
		} else if (iBallId >= SHI_BALL_START && iBallId < GE_BALL_START) {
			int iBallViewId = v.getId() - SHI_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				PublicMethod.myOutput("----- blue:" + iBallViewId);
				changeBuyViewByRule(iCurrentButton, 3, iBallViewId);// 点蓝球组
			}
		} else {
			int iBallViewId = v.getId() - GE_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				PublicMethod.myOutput("----- blue:" + iBallViewId);
				changeBuyViewByRule(iCurrentButton, 4, iBallViewId);// 点蓝球组
			}

		}

		changeTextSumMoney();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		case R.id.ssc_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			changeTextSumMoney();
			break;
		case R.id.ssc_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		case R.id.ssq_fushi_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			break;
		case R.id.ssq_fushi_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		default:
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		

		switch (checkedId) {
		case 0: // 返回购彩
		sensor.stopAction();
		Intent intent=new Intent(this,RuyicaiAndroid.class);
		startActivity(intent);
		finish();
			break;
		case 1: // 五星直选
	    
			iCurrentButton = PublicConst.SSC_FIVE_STAR;
			isjixuan=!isjixuan;
		
			if(isjixuan){
			sensor.startAction();
			create_FiveStar_JiXuan();
			
			}else{
			sensor.stopAction();
            createView(iCurrentButton);
			}
			topButtonGroup.setSelected(false);
			commit();
			break;
		case 2: 
		sensor.stopAction();// 五星通选
		iCurrentButton=PublicConst.SSC_FIVE_STAR_TONXXUAN;
		if(!isjixuan){
			isjixuan=true;
		}
		createView(iCurrentButton);
		topButtonGroup.setSelected(false);
		changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);
			break;
		
		}

	}

	@Override
	public void onCancelClick() {
		

	}

	@Override
	public void onOkClick(int[] aNums) {
		
		switch (iCurrentButton) {
		case PublicConst.SSC_FIVE_STAR:
			iWanBallNumber = aNums[0];
			iQianBallNumber = aNums[1];
			iBaiBallNumber = aNums[2];
			iShiBallNumber = aNums[3];
			iGeBallNumber = aNums[4];
			wanBallTable.sscRandomChooseConfigChange(iWanBallNumber);
			qianBallTable.sscRandomChooseConfigChange(iQianBallNumber);
			baiBallTable.sscRandomChooseConfigChange(iBaiBallNumber);
			shiBallTable.sscRandomChooseConfigChange(iShiBallNumber);
			geBallTable.sscRandomChooseConfigChange(iGeBallNumber);

			break;
//		case PublicConst.SSC_THREE_STAR:
//			iBaiBallNumber = aNums[0];
//			iShiBallNumber = aNums[1];
//			iGeBallNumber = aNums[2];
//			baiBallTable.sscRandomChooseConfigChange(iBaiBallNumber);
//			shiBallTable.sscRandomChooseConfigChange(iShiBallNumber);
//			geBallTable.sscRandomChooseConfigChange(iGeBallNumber);
//			break;
//		case PublicConst.SSC_TWO_STAR:
//			iShiBallNumber = aNums[0];
//			iGeBallNumber = aNums[1];
//			shiBallTable.sscRandomChooseConfigChange(iShiBallNumber);
//			geBallTable.sscRandomChooseConfigChange(iGeBallNumber);
//			break;
//		case PublicConst.SSC_ONE_STAR:
//			iGeBallNumber = aNums[0];
//			geBallTable.sscRandomChooseConfigChange(iGeBallNumber);
//			break;
		}
		changeTextSumMoney();

	}

	/**
	 * 提示信息
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public void changeTextSumMoney() {

		int iZhuShu = getZhuShu();
		if (iZhuShu != 0) {
			String iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			mTextSumMoney.setText(iTempString);
		} else {
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));
		}
	}

	/**
	 *时时彩注数的计算
	 * 
	 * @return int 返回注数 包含倍数
	 */
	private int getZhuShu() {
		int wan = wanBallTable.getHighlightBallNums();
		int qian = qianBallTable.getHighlightBallNums();
		int bai = baiBallTable.getHighlightBallNums();
		int shi = shiBallTable.getHighlightBallNums();
		int ge = geBallTable.getHighlightBallNums();
		int beishu = mSeekBarBeishu.getProgress();
		int iReturnValue = 0;
		switch (iCurrentButton) {
		case PublicConst.SSC_FIVE_STAR:
			iReturnValue = wan * qian * bai * shi * ge * beishu;
			break;
		case PublicConst.SSC_FIVE_STAR_TONXXUAN:
			iReturnValue = wan * qian * bai * shi * ge * beishu;
			break;
		}
		return iReturnValue;
	}

	/**
	 * 根据玩法改变当前View 及响应
	 * 
	 * @param aWhichTopButton
	 *            当前顶部标签位置
	 * @param aWhichGroupBall
	 *            第几组小球，如单式共两组小球，从0开始计数
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id+1
	 */
	private void changeBuyViewByRule(int aWhichTopButton, int aWhichGroupBall,
			int aBallViewId) {
		switch (aWhichTopButton) {
		case PublicConst.SSC_FIVE_STAR:
			// 单式View中，有2个ball table
			buyTicket(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.SSC_FIVE_STAR_TONXXUAN:
			buyTicket(aWhichGroupBall, aBallViewId);
			break;

		default:
			break;
		}
	}

	/**
	 * 单式玩法改变View
	 * 
	 * @param int aWhichGroupBall 被点击的小球位置
	 * 
	 * @param int aBallViewId 小球id
	 * 
	 * @return void
	 */
	private void buyTicket(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 红球区
			int iChosenBallSum = 10;
			// 每次点击后记住小球的状态
  
			int isHighLight = wanBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
          PublicMethod.showedittextnumber(waneditText, wanBallTable, getString(R.string.sscwanweitext));

		} else if (aWhichGroupBall == 1) {
			int iChosenBallSum = 10;
			int isHighLight = qianBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			PublicMethod.showedittextnumber(qianeditText, qianBallTable, getString(R.string.sscqianweitext));
		} else if (aWhichGroupBall == 2) {
			int iChosenBallSum = 10;
			int isHighLight = baiBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			PublicMethod.showedittextnumber(baieditText, baiBallTable, getString(R.string.sscbaiweitext));
		} else if (aWhichGroupBall == 3) {
			int iChosenBallSum = 10;
			int isHighLight = shiBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			PublicMethod.showedittextnumber(shieditText, shiBallTable,getString(R.string.sscshiweitext));
		} else if (aWhichGroupBall == 4) {
			int iChosenBallSum = 10;
			int isHighLight = geBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			PublicMethod.showedittextnumber(geeditText, geBallTable, getString(R.string.sscgeweitext));
		}
	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			progressdialog = new ProgressDialog(this);
			
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}

	/**
	 * 加减按钮事件监听方法
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	private void setSeekWhenAddOrSub(int idFind, View iV, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) iV.findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						iProgressBeishu++;
						if (iProgressBeishu > 99)
							iProgressBeishu = 99;
						mSeekBar.setProgress(iProgressBeishu);
					} else {
						iProgressBeishu--;
						if (iProgressBeishu < 1)
							iProgressBeishu = 1;
						mSeekBar.setProgress(iProgressBeishu);
					}
				} else {
					if (isAdd == 1) {
						iProgressQishu++;
						if (iProgressQishu > 99) {
							iProgressQishu = 99;
						}
						mSeekBar.setProgress(iProgressQishu);
					} else {
						iProgressQishu--;
						if (iProgressQishu < 1) {
							iProgressQishu = 1;
						}
						mSeekBar.setProgress(iProgressQishu);
					}

				}
			}
		});
	}

	
	/**
	 * 时时彩投注方法
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	private void beginTouZhu() {
		int wan = wanBallTable.getHighlightBallNums();
		int qian = qianBallTable.getHighlightBallNums();
		int bai = baiBallTable.getHighlightBallNums();
		int shi = shiBallTable.getHighlightBallNums();
		int ge = geBallTable.getHighlightBallNums();
		int beishu = mSeekBarBeishu.getProgress();
		int iZhuShu = getZhuShu();
		Log.v("isjixuan",  String.valueOf(isjixuan));
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
				SscFiveStar.this, "addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		if (sessionIdStr==null||sessionIdStr.equals("")) {
			Intent intentSession = new Intent(SscFiveStar.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			 switch(iCurrentButton){
			 case PublicConst.SSC_FIVE_STAR:
				 if(isjixuan){
					 if(balls.size()==0){
					  alert1("请至少选择1注彩票");
					}else{
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlertJixuan();
						alert_jixuan(sTouzhuAlert);
					}
					 
				    }else{

				if (wan == 0 | qian == 0 | bai == 0 | shi == 0 | ge == 0) {
					alert1("请至少选择一注！");
				} else if (iZhuShu * 2 > 20000) {
					DialogExcessive();
				} else{

					String sTouzhuAlert = "";
					sTouzhuAlert = getTouzhuAlert();
					alert(sTouzhuAlert);
				}
				    }
				 break;
			 case PublicConst.SSC_FIVE_STAR_TONXXUAN:
				 if (wan == 0 | qian == 0 | bai == 0 | shi == 0 | ge == 0) {
						alert1("请至少选择一注！");
					} else if (iZhuShu * 2 > 20000) {
						DialogExcessive();
					} else{

						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlert();
						alert(sTouzhuAlert);
					}
				     break;
					    }
			     
				 
			 }

		}


	/**
	 * 单笔投注大于2万元时的对话框
	 */
	private void DialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SscFiveStar.this);
		builder.setTitle("投注失败");
		builder.setMessage("单笔投注不能大于2万元");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	// 提示框1 用来提醒选球规则
	// fqc delete 删除取消按钮 7/14/2010
	private void alert1(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("请选择号码")
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
	 * 投注确认提示框
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	private void alert(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ssc_btn_touzhu.setClickable(false);
								ssc_btn_touzhu
										.setImageResource(R.drawable.touzhuup_n);
								showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
							
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQishu();
									String[] strCode = null;
									int iBeiShu = mSeekBarBeishu.getProgress();
									String zhuma = getZhuMa();

									@Override
									public void run() {

										if (mTimesMoney == 2) {
											strCode = payNew(zhuma, ""
													+ iBeiShu,
													iZhuShu * 2 + "", iQiShu
															+ "");
										} else if (mTimesMoney == 3) {
											strCode = payNew(zhuma, ""
													+ iBeiShu,
													iZhuShu * 3 + "", iQiShu
															+ "");
										}

										ssc_btn_touzhu.setClickable(true);

										Message msg1 = new Message();
										msg1.what = 10;
										handler.sendMessage(msg1);

										if (strCode[0].equals("0000")
												&& strCode[1].equals("000000")) {
											Message msg = new Message();
											msg.what = 6;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("070002")) {
											Message msg = new Message();
											msg.what = 7;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("0000")
												&& strCode[1].equals("000001")) {
											Message msg = new Message();
											msg.what = 4;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("1007")) {
											Message msg = new Message();
											msg.what = 2;
											handler.sendMessage(msg);
										} else if (strCode[1].equals("002002")) {
											Message msg = new Message();
											msg.what = 3;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("040006")
												|| strCode[0].equals("201015")) {
											Message msg = new Message();
											msg.what = 1;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("00")
												&& strCode[1].equals("00")) {
											Message msg = new Message();
											msg.what = 8;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("040003")) {
											Message msg = new Message();
											msg.what = 11;
											handler.sendMessage(msg);
										} else if (strCode[1].equals("1002")) {
											Message msg = new Message();
											msg.what = 14;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("20100706")) {
											Message msg = new Message();
											msg.what = 2;
											handler.sendMessage(msg);
										}else {
											Message msg = new Message();
											msg.what = 9;
											handler.sendMessage(msg);
										}
									}

								});
								t.start();
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
							}

						});
		dialog.show();

	}
    
	/*机选投注提示框*/
	private void alert_jixuan(String string) {
		sensor.stopAction();
		Dialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								ssc_btn_touzhu.setClickable(false);
								ssc_btn_touzhu
										.setImageResource(R.drawable.touzhuup_n);

								showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
								// TODO Auto-generated method stub
								Thread t = new Thread(new Runnable() {
								    int iQiShu = getQishu();
									String[] strCode = null;
									int iBeiShu = mSeekBarBeishu.getProgress();
									int iZhuShu =balls.size()*iBeiShu;
									String zhuma = getZhuMa();
									

									@Override
									public void run() {

										if (mTimesMoney == 2) {
											strCode = payNew(zhuma, ""
													+ iBeiShu,
													iZhuShu * 2 + "", iQiShu
															+ "");
										} else if (mTimesMoney == 3) {
											strCode = payNew(zhuma, ""
													+ iBeiShu,
													iZhuShu * 3 + "", iQiShu
															+ "");
										}

										ssc_btn_touzhu.setClickable(true);

										Message msg1 = new Message();
										msg1.what = 10;
										handler.sendMessage(msg1);

										if (strCode[0].equals("0000")
												&& strCode[1].equals("000000")) {
											Message msg = new Message();
											msg.what = 6;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("070002")) {
											Message msg = new Message();
											msg.what = 7;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("0000")
												&& strCode[1].equals("000001")) {
											Message msg = new Message();
											msg.what = 4;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("1007")) {
											Message msg = new Message();
											msg.what = 2;
											handler.sendMessage(msg);
										} else if (strCode[1].equals("002002")) {
											Message msg = new Message();
											msg.what = 3;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("040006")
												|| strCode[0].equals("201015")) {
											Message msg = new Message();
											msg.what = 1;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("00")
												&& strCode[1].equals("00")) {
											Message msg = new Message();
											msg.what = 8;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("040003")) {
											Message msg = new Message();
											msg.what = 11;
											handler.sendMessage(msg);
										} else if (strCode[1].equals("1002")) {
											Message msg = new Message();
											msg.what = 14;
											handler.sendMessage(msg);
										} 
										else if (strCode[0].equals("20100706")) {
											Message msg = new Message();
											msg.what = 2;
											handler.sendMessage(msg);
										}else {
											Message msg = new Message();
											msg.what = 9;
											handler.sendMessage(msg);
										}
									}

								});
								t.start();
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
							}

						}).create();
		dialog.show();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				sensor.startAction();
			}
		});
		

	}
	private String getTouzhuAlert() {
		int iZhuShu = getZhuShu();
		int[] wan = wanBallTable.getHighlightBallNOs();
		int[] qian = qianBallTable.getHighlightBallNOs();
		int[] bai = baiBallTable.getHighlightBallNOs();
		int[] shi = shiBallTable.getHighlightBallNOs();
		int[] ge = geBallTable.getHighlightBallNOs();

		return  "第" + batchCode + "期\n" 
				+ "注数："
				+ iZhuShu / mSeekBarBeishu.getProgress()
				+ "注"
				+ "\n"
				+ // 注数不能算上倍数 陈晨 20100713
				"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
				+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
				+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
				+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"+ "\n" 
				+"注码：" + "\n" + 
				 "万位："
				+ getStrZhuMa(wan) + "\n" + "千位：" + getStrZhuMa(qian) + "\n"
				+ "百位：" + getStrZhuMa(bai) + "\n" + "十位：" + getStrZhuMa(shi)
				+ "\n" + "个位：" + getStrZhuMa(ge) + "\n"
				+ "\n" + "确认支付吗？";

	}

	
	/*机选投注弹出框
	 * */
		private String getTouzhuAlertJixuan() {
			String zhumaString = "";
			for (int i = 0; i < balls.size(); i++) {
				zhumaString +=balls.get(i).getzhuma()+ "\n";
			}

			int beishu = mSeekBarBeishu.getProgress();
			int iZhuShu = balls.size() * beishu;
			return  "第" + batchCode + "期\n"
			        +"注数："
					+ balls.size()
					+ "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + beishu + "倍" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (balls.size() * 2 * beishu) + "元" + "\n" + "冻结金额："
					+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"
					+ "\n" + "注码：" + "\n" +"万位-千位-百位-十位-个位"+"\n"+zhumaString + "确认支付吗？";
		}
	/**
	 * 获取注码
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public String getZhuMa() {
		String t_str = "";
		int[] wan = wanBallTable.getHighlightBallNOs();
		int[] qian = qianBallTable.getHighlightBallNOs();
		int[] bai = baiBallTable.getHighlightBallNOs();
		int[] shi = shiBallTable.getHighlightBallNOs();
		int[] ge = geBallTable.getHighlightBallNOs();
		
        switch(iCurrentButton){
        case PublicConst.SSC_FIVE_STAR_TONXXUAN:
        
		t_str = "5T|";
		t_str += getStr(wan) + "," + getStr(qian) + "," + getStr(bai) + ","
				+ getStr(shi) + "," + getStr(ge);
		break;
        case PublicConst.SSC_FIVE_STAR:
        if(isjixuan){
        t_str = "5D|";
        t_str+=getstrjixuan();
        }else{
		t_str = "5D|";
		t_str += getStr(wan) + "," + getStr(qian) + "," + getStr(bai) + ","
					+ getStr(shi) + "," + getStr(ge);
        }
		break;
		}

		return t_str;

	}
    
	public String getstrjixuan(){
		String str="";
		for (int i = 0; i < balls.size(); i++) {
		   
			int ge[]= balls.get(i).getball();
			int j;
			for(j=0;j<ge.length;j++){
				if(j!=ge.length-1){
				str += ge[j]+",";
				}
				else{
					str += ge[j];	
				}
			}
			 if(i!=balls.size()-1){
					str+=";5D|";
					}
	    }
			
		return str;
		
	}
	public String getStr(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
		}
		return str;

	}

	public String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
			if (i != (balls.length - 1))
				str += ",";
		}
		return str;

	}

	private int getBeishu() {
		return mSeekBarBeishu.getProgress();

	}

	private int getQishu() {
		return mSeekBarQishu.getProgress();

	}

	// 投注新接口 20100711陈晨
	protected String[] payNew(String betCode, String lotMulti, String amount,
			String qiShu) {
	
		String[] error_code = null;
		BettingInterface betting = BettingInterface.getInstance();

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		String phonenum = shellRW.getUserLoginInfo("phonenum");
		PublicMethod.myOutput("-------------touzhusessionid" + sessionid);
		PublicMethod.myOutput("-------------phonenum" + phonenum
				+ "----lotMulti---" + lotMulti + "----amount----" + amount
				+ "---qiShu---" + qiShu);
		if (mTimesMoney == 2) {
			error_code = betting.BettingTC(phonenum, "T01007", betCode,
					lotMulti, amount, "2", qiShu, sessionid, batchCode);
		} else if (mTimesMoney == 3) {
			error_code = betting.BettingTC(phonenum, "T01007", betCode,
					lotMulti, amount, "3", qiShu, sessionid, batchCode);
		}

		return error_code;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (resultCode) {
		case RESULT_OK:
			
			beginTouZhu();
			
			break;
		default:
			Toast.makeText(SscFiveStar.this, "未登录成功！", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}
	
	class SscSensor extends SensorActivity {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.palmdream.netintface.SensorActivity#action()
		 */
		public SscSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
			zhuView.removeAllViews();
			balls = new Vector<Balls>();
			for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
				Balls ball = new Balls(5);
				ball.createBalls();
				balls.add(ball);
			}
			createTable(zhuView);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		recycleResources();
	}
	
	
	private void recycleResources(){
		PublicMethod.recycleBallTable(wanBallTable);
		PublicMethod.recycleBallTable(qianBallTable);
		PublicMethod.recycleBallTable(baiBallTable);
		PublicMethod.recycleBallTable(shiBallTable);
		PublicMethod.recycleBallTable(geBallTable);
	}
}
