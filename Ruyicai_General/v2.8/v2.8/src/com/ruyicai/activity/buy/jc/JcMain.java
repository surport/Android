package com.ruyicai.activity.buy.jc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.ShellRWSharesPreferences;



public abstract class JcMain extends Activity implements SeekBar.OnSeekBarChangeListener,HandlerMsg{
	public SeekBar mSeekBarBeishu;
	protected TextView mTextBeishu;
	private ImageButton zixuanTouzhu;
	private EditText editZhuma;
	public ListView list;
	private String phonenum,sessionId,userno;
	public Spinner spinner;
	public int iProgressBeishu = 1;
	ProgressDialog progressdialog;
	MyHandler handler = new MyHandler(this);//自定义handler
	public BetAndGiftPojo betAndGift=new BetAndGiftPojo();//投注信息类
	private String msg;
	public boolean isGift = false;//是否赠送
	public boolean isTouzhu = true;//是否投注
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_jc_main);
		createView();
		initListView(list);
	}
	public void createView(){
		spinner = (Spinner) findViewById(R.id.buy_jc_main_spinner);
		list = (ListView) findViewById(R.id.buy_jc_main_listview);
		editZhuma = (EditText) findViewById(R.id.buy_zixuan_edit_zhuma);
		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_zixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mTextBeishu = (TextView) findViewById(R.id.buy_zixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_beishu, -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,true);
		zixuanTouzhu = (ImageButton) findViewById(R.id.buy_zixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
	}
	
	/**
	 * 投注方法
	 */
	public void beginTouZhu() {
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(JcMain.this, "addInfo");
		sessionId = pre.getUserLoginInfo("sessionid");
		phonenum = pre.getUserLoginInfo("phonenum");
		userno = pre.getUserLoginInfo("userno");
		if (sessionId.equals("")) {
			Intent intentSession = new Intent(JcMain.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
           isTouZhuNet();
		}
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
	protected void setSeekWhenAddOrSub(int idFind, final int isAdd,final SeekBar mSeekBar, final boolean isBeiShu) {
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
				}
			}
		});
	}
	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText(String text){
		editZhuma.setText(text);
	}
	public abstract void isTouZhuNet();
	public abstract void initListView(ListView list);
	public abstract void showEditText();
	/**
	 * 单复式投注调用函数
	 * @param string  显示框信息
	 * @return
	 */
	public void alert(String string,final String zhuma) {
		isTouzhu =true;
		isGift = false;
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v= inflater.inflate(R.layout.alert_dialog_touzhu, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是").setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
							
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {

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
				dialog.cancel();
				initBet();
				if(isGift){
					toActivity(zhuma);
				}else if(isTouzhu){
					touZhuNet();
				}
			}
		});
		RadioButton check = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu_check);
		RadioButton touzhuCheck = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu1_check);
		RadioButton joinCheck = (RadioButton) v.findViewById(R.id.alert_dialog_join_check);
		joinCheck.setVisibility(RadioButton.GONE);
		touzhuCheck.setChecked(true);
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
		  
		  Intent intent = new Intent(JcMain.this, GiftActivity.class);
		  intent.putExtra("info", byteStream.toByteArray());
		  intent.putExtra("zhuma", zhuma);
		  startActivity(intent); 


	}
	/**
	 * 提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertInfo(String string,String title) {   
		Builder dialog = new AlertDialog.Builder(this).setTitle(title)
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
	 * 初始化投注信息
	 */
	public void initBet(){
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift 
		betAndGift.setLotmulti(""+iProgressBeishu);//lotmulti    倍数   投注的倍数
	}
	/**
	 * 投注联网
	 */
	public void touZhuNet(){
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
				try {
					JSONObject obj = new JSONObject(str);		
					msg = obj.getString("message");
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			beginTouZhu();
			break;
		}
	}
	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
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
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			showEditText();
			break;
		default:
			break;
		}

		
	}
	public void errorCode_0000() {
		// TODO Auto-generated method stub
	       Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}


	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}

	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}
