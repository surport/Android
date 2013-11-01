package com.ruyicai.activity.buy.cq11x5;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;

public class CqElevenFive extends ZixuanAndJiXuan {
	
	private  RelativeLayout relativeLayout;
	private TextView titleOne;//标题
	private TextView betInfo;//投注提示1
	private TextView batchcode;//投注提示2
	private TextView time;//距离开奖时间
	private CheckBox missCheck;//遗漏值开关
	private Button mYaoYao;//摇一摇机选
	private Button imgRetrun;//Top右边按钮
	private Button refreshBtn;//Top刷新按钮
	int lesstime;// 剩余时间
	public static String batchCode;// 期号
	private boolean isRun = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lotno = Constants.CQELVENFIVE;
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutMain = inflater.inflate(R.layout.buy_cq_eleven_five_main, null);
		setContentView(layoutMain);
		initView();
		setIssue(lotno);
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isTouzhu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getZhuShu() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getZhuma() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getZhuma(Balls ball) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckAction(int checkedId) {
		// TODO Auto-generated method stub

	}
	/**
	 * 初始化组件
	 */
	private void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.last_batchcode);
		titleOne = (TextView) findViewById(R.id.layout_main_text_title_one);//标题
		betInfo = (TextView) findViewById(R.id.bet_info);//投注提示1
		batchcode = (TextView) findViewById(R.id.last_batchcode_textlable_red);//投注提示2 
		time = (TextView) findViewById(R.id.layout_main_text_time);//距离开奖时间
		imgRetrun = (Button) findViewById(R.id.layout_main_img_return);//Top右边按钮
		refreshBtn = (Button) findViewById(R.id.refresh_code);//Top刷新按钮
		mYaoYao = (Button) findViewById(R.id.yaoyao_jisuan);//摇一摇机选
		missCheck=(CheckBox)findViewById(R.id.missCheck);//遗漏值开关
		
		titleOne.setText(getString(R.string.cq_11_5));         
		
		refreshBtn.setVisibility(View.VISIBLE);
		refreshBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				initLatestLotteryList();
			}
		});
		imgRetrun.setVisibility(View.VISIBLE);
		imgRetrun.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				createDialog(NoticeActivityGroup.ID_SUB_DLC_LISTVIEW);
			}
		});
	}
	/**
	 * 赋值给当前期
	 * 
	 * @param type彩种编号
	 */
	public void setIssue(final String lotno) {
		final Handler sscHandler = new Handler();
		time.setText("获取中...");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				String re = "";
				String message = "";
				re = GetLotNohighFrequency.getInstance().getInfo(lotno);
				if (!re.equalsIgnoreCase("")) {
					try {
						JSONObject obj = new JSONObject(re);
						message = obj.getString("message");
						error_code = obj.getString("error_code");
						lesstime = Integer.valueOf(CheckUtil.isNull(obj.getString("time_remaining")));
						batchCode = obj.getString("batchcode");
						while (isRun) {
							if (isEnd(lesstime)) {
								sscHandler.post(new Runnable() {
									public void run() {
										time.setText("剩余时间:"+ PublicMethod.isTen(lesstime / 60)+ ":"+ PublicMethod.isTen(lesstime % 60));
									}
								});
								Thread.sleep(1000);
								lesstime--;
							} else {
								sscHandler.post(new Runnable() {
									public void run() {
										time.setText("剩余时间:00:00");
										nextIssue();
									}
								});
								break;
							}
						}
					} catch (Exception e) {
						sscHandler.post(new Runnable() {
							public void run() {
								time.setText("获取失败");
							}
						});
					}
				} else {

				}
			}
		});
		thread.start();
	}
	private boolean isEnd(int time) {
		if (time > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 转入下一期对话框
	 */
	private void nextIssue() {
		new AlertDialog.Builder(CqElevenFive.this)
				.setTitle("提示")
				.setMessage(titleOne.getText().toString() + "第" + batchCode+ "期已经结束,是否转入下一期")
				.setNegativeButton("转入下一期", new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						setIssue(lotno);
					}

				})
				.setNeutralButton("返回主页面",new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								CqElevenFive.this.finish();
							}
						}).create().show();
	}
}
