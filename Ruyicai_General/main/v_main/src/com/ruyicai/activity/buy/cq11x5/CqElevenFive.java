package com.ruyicai.activity.buy.cq11x5;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.cq11x5.ChoosePopuAdapter.OnChickItem;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class CqElevenFive extends ZixuanAndJiXuan {

	private RelativeLayout relativeLayout;
	private TextView titleOne;// 标题
	private TextView betInfo;// 投注提示1
	private TextView batchcode;// 投注提示2
	private TextView time;// 距离开奖时间
	private CheckBox missCheck;// 遗漏值开关
	private Button mYaoYao;// 摇一摇机选
	private Button imgRetrun;// Top右边按钮
	private Button refreshBtn;// Top刷新按钮
	int lesstime;// 剩余时间
	public static String batchCode;// 期号
	private boolean isRun = true;
	//...miqingqiang start
	private TextView viewButton;
	private RelativeLayout reBtn;
	private GridView mGridViewFirst,mGridViewSecond;
	private PopupWindow popupWindow;
	private Button returnBtn;
	private int lottypeIndex = 0;
	RWSharedPreferences shellRW;
	private int position=0;
	private static final String PT="pt";//普通
	private static final String DT="dt";//胆拖
	//...end
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lotno = Constants.LOTNO_CQ_ELVEN_FIVE;
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
		titleOne = (TextView) findViewById(R.id.layout_main_text_title_one);// 标题
		betInfo = (TextView) findViewById(R.id.bet_info);// 投注提示1
		batchcode = (TextView) findViewById(R.id.last_batchcode_textlable_red);// 投注提示2
		time = (TextView) findViewById(R.id.layout_main_text_time);// 距离开奖时间
		imgRetrun = (Button) findViewById(R.id.layout_main_img_return);// Top右边按钮
		refreshBtn = (Button) findViewById(R.id.refresh_code);// Top刷新按钮
		mYaoYao = (Button) findViewById(R.id.yaoyao_jisuan);// 摇一摇机选
		missCheck = (CheckBox) findViewById(R.id.missCheck);// 遗漏值开关

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
				// createDialog(NoticeActivityGroup.ID_SUB_DLC_LISTVIEW);
			}
		});
		
		//...miqingqiang start
		reBtn=(RelativeLayout)findViewById(R.id.main_buy_title);
		viewButton=(TextView)findViewById(R.id.layout_main_text_title_one);
		reBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
				createMenuDialog();
			}});
		//...end
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
						lesstime = Integer.valueOf(CheckUtil.isNull(obj
								.getString("time_remaining")));
						batchCode = obj.getString("batchcode");
						while (isRun) {
							if (isEnd(lesstime)) {
								sscHandler.post(new Runnable() {
									public void run() {
										time.setText("距" + batchCode.substring(8) + "期截止:"
												+ PublicMethod
														.isTen(lesstime / 60)
												+ "分"
												+ PublicMethod
														.isTen(lesstime % 60)+"秒");
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
				.setMessage(
						titleOne.getText().toString() + "第" + batchCode
								+ "期已经结束,是否转入下一期")
				.setNegativeButton("转入下一期", new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						setIssue(lotno);
					}

				})
				.setNeutralButton("返回主页面",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								CqElevenFive.this.finish();
							}
						}).create().show();
	}
	/**
	 * 创建普通界面
	 */
	private void createViewPT(){
		
	}
	/**
	 * 创建胆拖界面
	 */
	private void createViewDT(){
		
	}
	/**
	 * 普通投注提示
	 */
	private void setPtBetPrompt(int type) {
		switch (type) {
		//普通
		case 0:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_2));
			batchcode.setText(getString(R.string.cq_11_5_prize2_rx_2));
			break;
		case 1:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_3));
			batchcode.setText(getString(R.string.cq_11_5_prize2_rx_3));
			break;
		case 2:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_4));
			batchcode.setText(getString(R.string.cq_11_5_prize2_rx_4));
			break;
		case 3:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_5));
			batchcode.setText(getString(R.string.cq_11_5_prize2_rx_5));
			break;
		case 4:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_6));
			batchcode.setText(getString(R.string.cq_11_5_prize2_rx_6));
			break;
		case 5:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_7));
			batchcode.setText(getString(R.string.cq_11_5_prize2_rx_7));
			break;
		case 6:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_8));
			batchcode.setText(getString(R.string.cq_11_5_prize2_rx_8));
			break;
			//前一
		case 7:
			betInfo.setText(getString(R.string.cq_11_5_prize_pt_qy));
			batchcode.setText(getString(R.string.cq_11_5_prize2_pt_qy));
			break;
			//前二直选
		case 8:
			betInfo.setText(getString(R.string.cq_11_5_prize_pt_qe_zhix));
			batchcode.setText(getString(R.string.cq_11_5_prize2_pt_qe_zhix));
			break;
			//前二组选
		case 9:
			betInfo.setText(getString(R.string.cq_11_5_prize_pt_qe_zux));
			batchcode.setText(getString(R.string.cq_11_5_prize2_pt_qe_zux));
			break;
			//前三直选
		case 10:
			betInfo.setText(getString(R.string.cq_11_5_prize_pt_qs_zhix));
			batchcode.setText(getString(R.string.cq_11_5_prize2_pt_qs_zhix));
			break;
		case 11:
			//前三组选
			betInfo.setText(getString(R.string.cq_11_5_prize_pt_qs_zux));
			batchcode.setText(getString(R.string.cq_11_5_prize2_pt_qs_zux));
			break;
		default:
			break;
		}
	}
	/**
	 * 胆拖投注提示
	 */
	private void setDtBetPrompt(int type) {
		switch (type) {
			//胆拖
		case 0:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_2));
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_2));
			break;
		case 1:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_3));
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_3));
			break;
		case 2:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_4));
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_4));
			break;
		case 3:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_5));
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_5));
			break;
		case 4:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_6));
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_6));
			break;
		case 5:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_7));
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_7));
			break;
		case 6:
			betInfo.setText(getString(R.string.cq_11_5_prize_rx_8));
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_8));
			break;
			//胆拖前二组选
		case 7:
			betInfo.setText("");
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_qe_zux));
			break;
			//胆拖前三组选
		case 8:
			betInfo.setText("");
			batchcode.setText(getString(R.string.cq_11_5_prize2_dt_qs_zux));
			break;

		default:
			break;
		}
	}
	
	//...miqingqiang start
	private ChoosePopuAdapter showMenuAdapterFirst,showMenuAdapterSecond;
	public void createMenuDialog(){
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = (LinearLayout) inflate.inflate(R.layout.eleven_choose_five_list, null);
			
		mGridViewFirst = (GridView) popupView.findViewById(R.id.chooseviewfirst);
		String[] str1=getResources().getStringArray(R.array.dlc_type);
		List<String> stoogesFirst = Arrays.asList(str1);
		showMenuAdapterFirst = new ChoosePopuAdapter(this,new popFirstOnItemChick(),stoogesFirst,PT);
		mGridViewFirst.setAdapter(showMenuAdapterFirst);
			
		mGridViewSecond=(GridView)popupView.findViewById(R.id.chooseviewsecond);
		String[] str2=getResources().getStringArray(R.array.choose_type);
		List<String> stoogesSecond=Arrays.asList(str2);
		showMenuAdapterSecond = new ChoosePopuAdapter(this,new popSecondOnItemChick(),stoogesSecond,DT);
		mGridViewSecond.setAdapter(showMenuAdapterSecond);
			
		popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(reBtn);
	}
		
	public class popFirstOnItemChick implements OnChickItem{

		@Override
		public void onChickItem(View view, int position, String text) {
			// TODO Auto-generated method stub
				
			showMenuAdapterFirst.setItemSelect(position);
			showMenuAdapterFirst.notifyDataSetInvalidated();
			popupWindow.dismiss();
			setPtBetPrompt(position);
		}
			
	}
		
	public class popSecondOnItemChick implements OnChickItem{

		@Override
		public void onChickItem(View view, int position, String text) {
				// TODO Auto-generated method stub
				
			showMenuAdapterSecond.setItemSelect(position);
			showMenuAdapterSecond.notifyDataSetInvalidated();
			popupWindow.dismiss();
			setDtBetPrompt(position);
		}
	}
}
