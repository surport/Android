package com.ruyicai.activity.game.dlt;

import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;
import com.ruyicai.util.ShellRWSharesPreferences;

public class DLTzhixuanjixuan extends DLT{
	private Vector<ZhixuanjiuxuanBalls> zhixuanballs;
	private ZhixuanjixuanSensor sensor = new ZhixuanjixuanSensor(this);
	private boolean isfreash = true;

	 int a = 1;
	 boolean isreselect = false;
	
	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		lottery_title.setText(getString(R.string.dlt_zhixuan_jixuan));
		dltzhixuan_button.setBackgroundResource(R.drawable.dlt_downbutton_turnzhixuan);
		Toast.makeText(DLTzhixuanjixuan.this, "摇动手机试试重选", Toast.LENGTH_SHORT).show();
		goucaidating_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				sensor.stopAction();
				Intent goucai = new Intent(DLTzhixuanjixuan.this,RuyicaiAndroid.class);
				startActivity(goucai);
				DLTzhixuanjixuan.this.finish();
			}});
		dltzhixuan_button.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				sensor.stopAction();
				Intent zhixuan = new Intent(DLTzhixuanjixuan.this,DLTzhixuan.class);
				startActivity(zhixuan);
				DLTzhixuanjixuan.this.finish();
			}});
		dltdantuo_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sensor.stopAction();
				Intent dantuo = new Intent(DLTzhixuanjixuan.this,DLTdantuo.class);
				startActivity(dantuo);	
				DLTzhixuanjixuan.this.finish();
			}
		});
		dlt12xuan2_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sensor.stopAction();
				Intent twoxuan2 = new Intent(DLTzhixuanjixuan.this,DLT12xuan2.class);
				startActivity(twoxuan2);	
				DLTzhixuanjixuan.this.finish();
			}
		});
		init();
	}
	private	void init(){
		    iCurrentButton = PublicConst.BUY_DLT_FUSHI_JX;
		    buyView.removeAllViews();
			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_dlt_zhixuan_jixuan, null);
			dltjixuanlinear = (LinearLayout)iV.findViewById(R.id.layout_dlt_zhixuan_linear_zhuma);
			dltjixuanzhu = (Spinner)iV.findViewById(R.id.layout_zhixuan_jixuan_spinner);	
			mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.dlt_zhixuan_jixuan_seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);
			mSeekBarQishu = (SeekBar) iV.findViewById(R.id.dlt_zhixuan_jixuan_seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);
			mTextBeishu = (TextView) iV.findViewById(R.id.dlt_zhixuan_jixuan_beishu_text);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV.findViewById(R.id.dlt_zhixuan_jixuan_text_qishu);
			mTextQishu.setText("" + iProgressQishu);
			setSeekWhenAddOrSub(R.id.dlt_zhixuan_jixuan_seekbar_sub_beishu, iV, -1,	mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.dlt_zhixuan_jixuan_seekbar_add_beishu, iV, 1,mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.dlt_zhixuan_jixuan_seekbar_subtract_qihao, iV, -1,	mSeekBarQishu, false);
			setSeekWhenAddOrSub(R.id.dlt_zhixuan_jixuan_seekbar_add_qishu, iV, 1,mSeekBarQishu, false);
			dlt_b_touzhu_fushi = (Button)iV.findViewById(R.id.dlt_zhixuan_jixuan_b_touzhu);
			dlt_b_touzhu_fushi.setOnClickListener(dltzhixuanjixuanlistener);
			Button chongxuan = (Button) iV.findViewById(R.id.dlt_zhixuan_jixuan_chongxuan);
			chongxuan.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dltjixuanlinear.removeAllViews();
					zhixuanballs = new Vector<ZhixuanjiuxuanBalls>();
					for (int i = 0; i < dltjixuanzhu.getSelectedItemPosition() + 1; i++) {
						ZhixuanjiuxuanBalls ball = new ZhixuanjiuxuanBalls();
						zhixuanballs.add(ball);
					}
					creatTablezhixuanjixuan(dltjixuanlinear);
					isreselect =true;
				}
			});
			dltjixuanzhu.setSelection(4);
			zhuijiatouzhu = (ToggleButton)iV.findViewById(R.id.dlt_zhixuan_jixuan_zhuijia);
			zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					mTimesMoney=isChecked?3:2;
					changeTextSumMoney();
				}
			});
			dltjixuanzhu.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					isreselect = true;
					if(isfreash){
						dltjixuanlinear.removeAllViews();
						zhixuanballs = new Vector<ZhixuanjiuxuanBalls>();
						for (int i = 0; i < dltjixuanzhu.getSelectedItemPosition() + 1; i++) {						
							ZhixuanjiuxuanBalls ball = new ZhixuanjiuxuanBalls();
							zhixuanballs.add(ball);
						}
						creatTablezhixuanjixuan(dltjixuanlinear);
					}else{
						isfreash = true;
					}
				}
				public void onNothingSelected(AdapterView<?> arg0) {				
				}
			});
			buyView.addView(iV, new LinearLayout.LayoutParams(buyView.getLayoutParams().width, buyView.getLayoutParams().height));
			sensor.onVibrator();// 震动
	}
    OnClickListener dltzhixuanjixuanlistener=new OnClickListener(){
	    public void onClick(View v) {
	    	ShellRWSharesPreferences pre = new ShellRWSharesPreferences(DLTzhixuanjixuan.this,"addInfo");
		    String sessionIdStr = pre.getUserLoginInfo("sessionid");
			if (sessionIdStr.equals("")||sessionIdStr==null) {
				Intent intentSession = new Intent(DLTzhixuanjixuan.this, UserLogin.class);
				startActivity(intentSession);
			} else{
				String zhuma_zhixuanjixaun = getDLTzhixuanjixuanZhuMa(1,2);
				Log.e("zhuma_jixuan========",zhuma_zhixuanjixaun);
	            beginZhixuanjixuanTouZhu();
			}
	     }};
	  
	/**返回直选机选alert的值*/
	 protected String  getzhixuanjixuanTouzhuAlert(){
		 int zhixuanjixuanzhushu=zhixuanballs.size();
		 if(isreselect == false){
				a++;
			}else{
				a=1;
			}
		 String alert_touzhu_string = "";
			alert_touzhu_string = 
			"注数："+ zhixuanjixuanzhushu+ "注"+ "\n"
			+ "倍数："/* 注数不能算上倍数 陈晨 20100713*/ + mSeekBarBeishu.getProgress() + "倍" + "\n" 
			+ "追号："+ mSeekBarQishu.getProgress() + "期" + "\n"
			+ "金额："+ (zhixuanjixuanzhushu * mTimesMoney*mSeekBarBeishu.getProgress()) + "元" + "\n"
			+ "冻结金额："+ ((getQishu() - 1) * zhixuanjixuanzhushu * mTimesMoney*mSeekBarBeishu.getProgress()) + "元" + "\n"
			+"注码："+"\n" + getDLTzhixuanjixuanZhuMa(2,a) + "\n"
			+ "确认支付吗？";
			
			isreselect = false;
		return alert_touzhu_string;
	}
	/**
	 *  开始投注 响应点击投注按钮 和 menu里面的投注选项
	 */
	void beginZhixuanjixuanTouZhu () {	   
		    String sTouzhuAlert = getzhixuanjixuanTouzhuAlert();
			alertZhixuanjixuan(sTouzhuAlert);	   
		}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensor.stopAction();
	}
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sensor.startAction();
	}
	/** 大乐透直选机选提示框 用来确定是否投注*/
	void alertZhixuanjixuan(String string) {
		sensor.stopAction();
		Dialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
								Thread t = new Thread(new Runnable() {									
									int iQiShu = mSeekBarQishu.getProgress();;
									String[] strCode = null;
									int iBeiShu = mSeekBarBeishu.getProgress();
									public void run() {
										String zhuma_zhixuanjixaun = getDLTzhixuanjixuanZhuMa(1,2);
										int zhixuanjixuanzhushu=zhixuanballs.size();						
										Log.e("zhuma_jixuan===++++++",zhuma_zhixuanjixaun);
										// 新接口 陈晨20100711
										strCode = payNew(zhuma_zhixuanjixaun,"" + iBeiShu, zhixuanjixuanzhushu* mTimesMoney*iBeiShu + "",iQiShu + "");
										Log.v("zhuma_zhixuanjixaun",""+zhuma_zhixuanjixaun);
										if (strCode[0].equals("0000")&& strCode[1].equals("000000")) {
											Message msg = new Message();
											msg.what = 6;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("070002")) {
											Message msg = new Message();
											msg.what = 7;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("0000")&& strCode[1].equals("000001")) {
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
										} else if (strCode[0].equals("040006")|| strCode[0].equals("201015")) {
											Message msg = new Message();
											msg.what = 1;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("00")&& strCode[1].equals("00")) {
											Message msg = new Message();
											msg.what = 8;
											handler.sendMessage(msg);
										} else {
											Message msg = new Message();
											msg.what = 9;
											handler.sendMessage(msg);
										}
									}
								});
								t.start();
							}
						}).setNegativeButton("取消",	new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {}
						}).create();
		dialog.show();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				sensor.startAction();
			}
		});
	}
	class ZhixuanjiuxuanBalls{
		int[] redBall;
		int[] blueBall;
		public ZhixuanjiuxuanBalls(){
			redBall = PublicMethod.getRandomsWithoutCollision(5, 0, 34);
			redBall = PublicMethod.orderby(redBall, "abc");
			blueBall = PublicMethod.getRandomsWithoutCollision(2, 0, 11);
			blueBall =  PublicMethod.orderby(blueBall, "abc");
		}
		public int[] getRedBall() {
			return redBall;
		}
		public int[] getBlueBall() {
			return blueBall;
		}
		String getRedBallZhu(){
			String str = "";
			for (int i = 0; i < redBall.length; i++) {
				if (i != redBall.length - 1){
					str += PublicMethod.getZhuMa(redBall[i] + 1) + ",";
				}else{
					str += PublicMethod.getZhuMa(redBall[i] + 1);
				}
			}
			return str;
		}
		String getBlueBallZhu(){
			String str = "";
			for (int i = 0; i < blueBall.length; i++) {
				if (i != blueBall.length - 1){
					str += PublicMethod.getZhuMa(blueBall[i] + 1) + ",";
				}else{
					str += PublicMethod.getZhuMa(blueBall[i] + 1);
				}
			}
			return str;
		}
	}
	/**
	 * 创建大乐透直选机选小球队列
	 * @param layout 主布局，在主布局中创建小球队列
	 */
	void creatTablezhixuanjixuan(LinearLayout layout ){
		for (int i = 0; i < zhixuanballs.size(); i++) {
			final int index = i;
			LinearLayout lines = new LinearLayout(layout.getContext());
		    redBallTable = new BallTable(lines,RED_BALL_START);
		    blueBallTable = new BallTable(lines,BLUE_BALL_START);
		    int[] redball = zhixuanballs.get(i).getRedBall();
			int[] blueball = zhixuanballs.get(i).getBlueBall();
			
			PublicMethod.makeBallTableJiXuan(redBallTable, iScreenWidth,redBallResId, redball,this);			
			PublicMethod.makeBallTableJiXuan(blueBallTable, iScreenWidth,blueBallResId, blueball,this);
			final ImageButton delet = new ImageButton(lines.getContext());
			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(zhixuanballs.size() > 1){
						dltjixuanlinear.removeAllViews();
					    zhixuanballs.remove(index);
					    dltjixuanzhu.setSelection(zhixuanballs.size() - 1);
					    creatTablezhixuanjixuan(dltjixuanlinear);
					    isfreash = false;
					}else {
						Toast.makeText(DLTzhixuanjixuan.this, R.string.zhixuan_jixuan_toast, Toast.LENGTH_SHORT).show();
					}
				}
			}); 
			   
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
	
			param.setMargins(15, 10, 0, 0);
			lines.addView(delet, param);
			lines.setGravity(Gravity.CENTER_HORIZONTAL);
			layout.addView(lines);
		}
	}
 /**
  * 得到大乐透机选玩法的注码，
  * @param type  type=1得到的注码用于服务器;type为其它值时，得到的注码用于投注提示
  * @return
  */
	String getDLTzhixuanjixuanZhuMa(int type,int a){ 
		int[] redball = null ;
		int[] blueball = null;
		String[] redballbatchcode = new String[5];
		String[] blueballbatchcode = new String[2];
		int zhixuanjixuanzhushu=zhixuanballs.size();
		String t_str = "";
		for (int i = 0; i < zhixuanjixuanzhushu; i++) {
			
				redball = zhixuanballs.get(i).getRedBall();
				blueball = zhixuanballs.get(i).getBlueBall();
				for(int j = 0;j<redball.length;j++){
					if(type!=1&&a == 1){
					redball[j] += 1;
					}
					if(redball[j]<10){
						redballbatchcode[j] = "0"+redball[j];
					}else{
						redballbatchcode[j] = redball[j]+"";
					}
				}
				
			   for(int k = 0;k<blueball.length;k++){
				   if(type!=1&&a == 1){
				   blueball[k] += 1;
				   }
				   if(blueball[k]<10){
					   blueballbatchcode[k] = "0"+blueball[k];
					}else{
						blueballbatchcode[k] = blueball[k]+"";
					}
			   }		
			switch (type) {
			case 1:
				t_str += redballbatchcode[0]+" "+redballbatchcode[1]+" "+redballbatchcode[2]+" "+redballbatchcode[3]+" "+redballbatchcode[4]+"-"+blueballbatchcode[0]+" "+blueballbatchcode[1];
				if(i!=zhixuanjixuanzhushu-1){
					t_str += ";";
				}
				break;	
			default:
				t_str += redballbatchcode[0]+"."+redballbatchcode[1]+"."+redballbatchcode[2]+"."+redballbatchcode[3]+"."+redballbatchcode[4]+"|"+blueballbatchcode[0]+"."+blueballbatchcode[1]+"\n";
				break;
			}	
		}

		return t_str;
	}
	
	
	/**
	 * 继承SensorActivity类，重写他的action方法，在action方法中加入逻辑
	 * @author miao
	 */
	class ZhixuanjixuanSensor extends SensorActivity {
		public ZhixuanjixuanSensor(Context context) {
			getContext(context);
		}
		public void action() {
			dltjixuanlinear.removeAllViews();
			zhixuanballs = new Vector<ZhixuanjiuxuanBalls>();
			for (int i = 0; i < dltjixuanzhu.getSelectedItemPosition() + 1; i++) {
				ZhixuanjiuxuanBalls ball = new ZhixuanjiuxuanBalls();
				zhixuanballs.add(ball);
			}
			creatTablezhixuanjixuan(dltjixuanlinear);
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {	
		case KeyEvent.KEYCODE_BACK:
			sensor.stopAction();
			break;	
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PublicMethod.recycleBallTable(redBallTable);
		PublicMethod.recycleBallTable(redTuoBallTable);
		PublicMethod.recycleBallTable(blueBallTable);
		PublicMethod.recycleBallTable(blueTuoBallTable);
	}
}
