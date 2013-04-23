/**
 * 
 */
package com.ruyicai.activity.buy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 单式机选父类
 * @author Administrator
 *
 */
public class DanshiJiXuan extends Activity implements SeekBar.OnSeekBarChangeListener,HandlerMsg{
	private TextView mTextSumMoney;
	private ImageButton zixuanTouzhu;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	private TextView mTextBeishu, mTextQishu;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	private Spinner jixuanZhu;
	private LinearLayout zhumaView;
	private SsqSensor sensor = new SsqSensor(this);
	private boolean isOnclik = true;
	public Vector<Balls> balls = new Vector();
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private int blueBallResId[] = { R.drawable.grey, R.drawable.blue };// 选区小球切换图片
	private Balls ballOne;
	private BuyImplement buyImplement;//投注要实现的方法
	private Toast toast;
	private boolean toLogin = false;
	ProgressDialog progressdialog;
	private static final int DIALOG1_KEY = 0;// 进度条的值2010/7/4
	public BetAndGiftPojo betAndGift=new BetAndGiftPojo();//投注信息类
	MyHandler handler = new MyHandler(this);//自定义handler
	String phonenum,sessionId,userno;
	public boolean isGift = false;//是否赠送
	public boolean isJoin = false;//是否合买
	public boolean isTouzhu = false;//是否投注
	private int oneValue = 2;//单注金额
	String codeStr;
	String lotno;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.buy_danshi_jixuan_activity);
	}
	/**
	 * 创建机选界面
	 */
	public void createView(Balls balles,BuyImplement buyImplement){
		sensor.startAction();
		this.buyImplement = buyImplement;
		this.ballOne = balles;
		zhumaView = (LinearLayout) findViewById(R.id.buy_danshi_jixuan_linear_zhuma);
		zhumaView.removeAllViews();
		toast = Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT);
		toast.show();
		balls = new Vector<Balls>();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// 初始化spinner
			jixuanZhu = (Spinner) findViewById(R.id.buy_danshi_jixuan_spinner);
			jixuanZhu.setSelection(4);
			jixuanZhu.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int position = jixuanZhu.getSelectedItemPosition();
					if (isOnclik) {
						zhumaView.removeAllViews();
						balls = new Vector();
						for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
							Balls ball = ballOne.createBalls();
							balls.add(ball);
						}
						createTable(zhumaView);
					} else {
						isOnclik = true;
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
		int index = jixuanZhu.getSelectedItemPosition() + 1;
		for (int i = 0; i < index; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
		sensor.onVibrator();// 震动
		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_danshi_jixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) findViewById(R.id.buy_danshi_jixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		mTextBeishu = (TextView) findViewById(R.id.buy_danshi_jixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView) findViewById(R.id.buy_danshi_jixuan_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.buy_danshi_jixuan_img_subtract_beishu, -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.buy_danshi_jixuan_img_add_beishu, 1, mSeekBarBeishu,true);
		setSeekWhenAddOrSub(R.id.buy_danshi_jixuan_img_subtract_qihao, -1,mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.buy_danshi_jixuan_img_add_qishu, 1, mSeekBarQishu,false);
		zixuanTouzhu = (ImageButton) findViewById(R.id.buy_danshi_jixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		ImageButton again = (ImageButton) findViewById(R.id.buy_danshi_jixuan_img_again);
		again.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zhumaView.removeAllViews();
				balls = new Vector();
				for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
					Balls ball = ballOne.createBalls();
					balls.add(ball);
				}
				createTable(zhumaView);
			}
		});
	}
	/**
	 * fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	 * 
	 * @param idFind
	 * @param iV
	 * @param isAdd
	 * @param mSeekBar
	 * @param isBeiShu
	 */
	private void setSeekWhenAddOrSub(int idFind, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressBeishu);
					} else {
						mSeekBar.setProgress(--iProgressBeishu);
					}
					iProgressBeishu = mSeekBar.getProgress();
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressQishu);
					} else {
						mSeekBar.setProgress(--iProgressQishu);
					}
					iProgressQishu = mSeekBar.getProgress();
				}
			}
		});
	}
	/**
	 * 投注方法
	 */
	private void beginTouZhu() {
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(DanshiJiXuan.this, "addInfo");
		sessionId = pre.getUserLoginInfo("sessionid");
		phonenum = pre.getUserLoginInfo("phonenum");
		userno = pre.getUserLoginInfo("userno");
		if (sessionId.equals("")) {
			toLogin = true;
			Intent intentSession = new Intent(DanshiJiXuan.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {

			if (balls.size() == 0) {
				alertInfo("请至少选择1注彩票");
			} else {
				String sTouzhuAlert = "";
				sTouzhuAlert = getTouzhuAlertJixuan();
				alert_jixuan(sTouzhuAlert,getZhuma());
			}
		}

	}
    public void setOneValue(int value){
    	oneValue = value;
    }
	/**
	 * 直选机选投注提示框中的信息
	 */
	private String getTouzhuAlertJixuan() {

		int beishu = mSeekBarBeishu.getProgress();
		int iZhuShu = balls.size() * beishu;
		return "注数："+ balls.size()+ "注"+ "\n"
				+ /* 注数不能算上倍数 陈晨 20100713*/
				"倍数：" + beishu + "倍" + "\n" + "追号："
				+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
				+ (balls.size() * oneValue * beishu) + "元" + "\n" + "冻结金额："
				+ (oneValue * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"
				+ "\n" ;
	}
	/**
	 * 投注提示显示注码
	 * @return
	 */
	private String getZhuma(){
		String zhumaString = "";
		for (int i = 0; i < balls.size(); i++) {
			for(int j=0;j<balls.get(i).getVZhuma().size();j++){
				zhumaString += balls.get(i).getShowZhuma(j);
				if(j!=balls.get(i).getVZhuma().size()-1){
					zhumaString += "+";
				}
			}
			if (i != balls.size() - 1) {
				zhumaString += "\n";
			}
		}
		return "注码：" + "\n" + zhumaString ;	
	}
	/**
	 * seekBar组件的监听器
	 */
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();
        
		switch (seekBar.getId()) {
		case R.id.buy_danshi_jixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			break;
		case R.id.buy_danshi_jixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		default:
			break;
		}

	}
	/**
	 * 创建直选机选
	 */
	public void createTable(LinearLayout layout) {
		for (int i = 0; i < balls.size(); i++) {
			final int index = i;
			int iScreenWidth = PublicMethod.getDisplayWidth(this);
			LinearLayout lines = new LinearLayout(layout.getContext());
			for(int j=0;j<balls.get(i).getVZhuma().size();j++){
				 String color = (String) balls.get(i).getVColor().get(j);
				 TableLayout table;
				if(color.equals("red")){
					table = PublicMethod.makeBallTableJiXuan(null,iScreenWidth,redBallResId, balls.get(i).getBalls(j),1, this);
				}else{
				    table = PublicMethod.makeBallTableJiXuan(null,iScreenWidth,blueBallResId, balls.get(i).getBalls(j),1, this);
				}
					
				lines.addView(table);
			}	
			ImageButton delet = new ImageButton(lines.getContext());
			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (balls.size() > 1) {
						zhumaView.removeAllViews();
						balls.remove(index);
						isOnclik = false;
						jixuanZhu.setSelection(balls.size() - 1);
						createTable(zhumaView);
					} else {
						Toast.makeText(DanshiJiXuan.this, getResources().getText(R.string.zhixuan_jixuan_toast),Toast.LENGTH_SHORT).show();
					}
				}
			});	
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(10, 5, 0, 0);
			lines.addView(delet, param);
			lines.setGravity(Gravity.CENTER_HORIZONTAL);
            if(i%2==0){
				lines.setBackgroundResource(R.drawable.jixuan_list_bg);
			}
            lines.setPadding(0, 3, 0, 0);
			layout.addView(lines);
			
		}

	}
	/**
	 * 提示框1 用来提醒选球规则
	 * @param string     显示框信息
	 * @return
	 */
	public void alertInfo(String string) {   
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
	 * 机选投注调用函数
	 * @param string         显示框信息
	 * @return
	 */
	private void alert_jixuan(String string,final String zhuma) {
		initBet();
		codeStr = zhuma;
		isGift = false;
		isJoin = false;
		isTouzhu = true; 
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v= inflater.inflate(R.layout.alert_dialog_touzhu, null);
		sensor.stopAction();
		final AlertDialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						 touZhuNet();
						
					}
				}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
	
							}
						}).create();
		dialog.show();
		TextView text =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_one);
		text.setText(string);
		TextView textZhuma =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_two);
		textZhuma.setText(zhuma);
		Button cancel = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				if(isGift){
					toActivity(zhuma);
				}else if(isJoin){
					toJoinActivity();
				}else if(isTouzhu){
					touZhuNet();
				}
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				sensor.startAction();
			}
		});
		RadioButton check = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu_check);
		RadioButton joinCheck = (RadioButton) v.findViewById(R.id.alert_dialog_join_check);
		RadioButton touzhuCheck = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu1_check);
		touzhuCheck.setChecked(true);
		TextView textAlert = (TextView) v.findViewById(R.id.alert_dialog_touzhu_text_alert);
		if(iProgressQishu>1){
			check.setVisibility(CheckBox.GONE);
			joinCheck.setVisibility(CheckBox.GONE);
			touzhuCheck.setVisibility(CheckBox.GONE);
			textAlert.setVisibility(TextView.VISIBLE);

		}else{
			check.setPadding(50, 0, 0, 0);
		    check.setButtonDrawable(R.drawable.check_select);
			// 实现记住密码 和 复选框的状态
		    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
	                            isGift = isChecked;
						}
					});
			joinCheck.setPadding(50, 0, 0, 0);
		    joinCheck.setButtonDrawable(R.drawable.check_select);
			// 实现记住密码 和 复选框的状态
		    joinCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
                        isJoin = isChecked;
				}
			});
			touzhuCheck.setPadding(50, 0, 0, 0);
		    touzhuCheck.setButtonDrawable(R.drawable.check_select);
			// 实现记住密码 和 复选框的状态
		    touzhuCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					    isTouzhu = isChecked;
				}
			});
		}
		dialog.getWindow().setContentView(v);

	}
	public void toActivity(String zhuma){
		  ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		   try {
			   ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
	           objStream.writeObject(betAndGift);
		  } catch (IOException e) {
			  return;  // should not happen, so donot do error handling
		  }
		  
		  Intent intent = new Intent(DanshiJiXuan.this, GiftActivity.class);
		  intent.putExtra("info", byteStream.toByteArray());
		  intent.putExtra("zhuma", zhuma);
		  startActivity(intent); 


	}
	public void toJoinActivity(){
		  ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		   try {
			   ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
	           objStream.writeObject(betAndGift);
		  } catch (IOException e) {
			  return;  // should not happen, so donot do error handling
		  }
		  
		  Intent intent = new Intent(DanshiJiXuan.this,JoinStartActivity.class);
		  intent.putExtra("info", byteStream.toByteArray());
		  startActivity(intent); 


	}
	/**
	 * 投注联网
	 */
	public void touZhuNet(){
		lotno = PublicMethod.toLotno(betAndGift.getLotno());
		betAndGift.setBatchcode(PublicMethod.toIssue(betAndGift.getLotno()));
		showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
					str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
					try {
						JSONObject obj = new JSONObject(str);		
						String msg = obj.getString("message");
						String error = obj.getString("error_code");
						handler.handleMsg(error,msg);
					} catch (JSONException e) {
						e.printStackTrace();
	
					}
					progressdialog.dismiss();
			}

		});
		t.start();
	}
	/**
	 * 初始化投注信息
	 */
	public void initBet(){
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBet_code(ballOne.getZhuma(balls, iProgressBeishu));
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift 
		betAndGift.setLotmulti(""+iProgressBeishu);//lotmulti    倍数   投注的倍数
		betAndGift.setBatchnum(""+iProgressQishu);//batchnum    追号期数 默认为1（不追号）
		betAndGift.setBatchcode(PublicMethod.toIssue(betAndGift.getLotno()));
		// amount      金额 单位为分（总金额）
        //lotno       彩种编号  投注彩种，如：双色球为F47104
		buyImplement.touzhuNet();
		
	}
	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}
	/**
	 * 实现震动的类
	 * @author Administrator
	 *
	 */
	class SsqSensor extends SensorActivity {

		public SsqSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
			zhumaView.removeAllViews();
			balls = new Vector<Balls>();
			for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
				Balls ball = ballOne.createBalls();
				balls.add(ball);
			}
			createTable(zhumaView);
		}
	}
	/**
	 * 重新初始化界面
	 */
	public void againView(){
	    sensor.startAction();
	    sensor.onVibrator();// 震动
	    toast.show();
	    jixuanZhu.setSelection(4);
		zhumaView.removeAllViews();
		balls = new Vector();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			beginTouZhu();
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
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!toLogin){
		    againView();			
		}else{
			toLogin = false;
		}
	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!toLogin){
			sensor.stopAction();
			mSeekBarBeishu.setProgress(1);
			mSeekBarQishu.setProgress(1);
		}
	}
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_0000()
	 */
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
	    PublicMethod.showDialog(DanshiJiXuan.this,lotno+codeStr);
	}
	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_000000()
	 */
	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#getContext()
	 */
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
}
