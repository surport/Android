package com.ruyicai.activity.buy.cq11x5;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.cq11x5.ChooseDTPopuAdapter.OnDtChickItem;
import com.ruyicai.activity.buy.cq11x5.ChoosePTPopuAdapter.OnChickItem;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class CqElevenFive extends ZixuanAndJiXuan {
	protected int BallResId[] = { R.drawable.cq_11_5_ball_normal, R.drawable.cq_11_5_ball_select };
	private RelativeLayout relativeLayout;
	private TextView titleOne;// 标题
	private TextView betInfo;// 投注提示1
	private TextView batchcode;// 投注提示2
	private TextView time;// 距离开奖时间
	private CheckBox missCheck;// 遗漏值开关
	private Button mYaoYao;// 摇一摇机选
	private Button imgRetrun;// Top右边按钮
	private Button refreshBtn;// Top刷新按钮
	
	private static final String TITLE="重庆11选5";
	protected String pt_types[] = { "PT_R2", "PT_R3", "PT_R4", "PT_R5", "PT_R6", "PT_R7","PT_R8",
			"PT_QZ1", "PT_QZ2", "PT_QZ3", "PT_ZU2", "PT_ZU3" };// 普通类型
	protected String dt_types[] = { "DT_R2", "DT_R3", "DT_R4", "DT_R5", "DT_R6", "DT_R7", "DT_R8",
			"DT_ZU2", "DT_ZU3" };// 胆拖类型
	public static String state = "PT_R2";// 当前类型
	int lesstime;// 剩余时间
	public static String batchCode;// 期号
	private boolean isRun = true;
	//...miqingqiang start
	private TextView viewButton;
	private RelativeLayout reBtn;
	private MyGridView mGridViewFirst,mGridViewSecond;
	private PopupWindow popupWindow;
	private Button returnBtn;
	private int lottypeIndex = 0;
	RWSharedPreferences shellRW;
	private int position=0;
	private static final String PT="pt";//普通
	private static final String DT="dt";//胆拖
	private int tag=1;
	private int itemId=0;
	private int checkedId;
	public AddView addView = new AddView(this);
	//...end
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setAddView(addView);
		lotno = Constants.LOTNO_CQ_ELVEN_FIVE;
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutMain = inflater.inflate(R.layout.buy_cq_eleven_five_main, null);
		setContentView(layoutMain);
		highttype = "CQ_ELEVEN_FIVE";
		initView();
		setIssue(lotno);
		action();
		setTitle("任选二");
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		this.checkedId=checkedId;
		onCheckAction(checkedId);
//		showBetInfo(textSumMoney(areaNums, iProgressBeishu));
	}
	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		return "zzz";
	}

	@Override
	public String isTouzhu() {
		// TODO Auto-generated method stub
		return "zzz";
	}

	@Override
	public int getZhuShu() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getZhuma() {
		// TODO Auto-generated method stub
		return "zzz";
	}

	@Override
	public String getZhuma(Balls ball) {
		// TODO Auto-generated method stub
		return "zzz";
	}

	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckAction(int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case 0:
			if(tag==1){
				createViewPT(checkedId);
			}else if (tag==2) {
				createViewDT(checkedId);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 初始化组件
	 */
	private void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.last_batchcode);
		titleOne = (TextView) findViewById(R.id.layout_main_text_title_one);// 标题
		betInfo = (TextView) findViewById(R.id.last_batchcode_textlable_red);// 投注提示1
		batchcode = (TextView) findViewById(R.id.bet_info);// 投注提示2
		time = (TextView) findViewById(R.id.layout_main_text_time);// 距离开奖时间
		imgRetrun = (Button) findViewById(R.id.layout_main_img_return);// Top右边按钮
		refreshBtn = (Button) findViewById(R.id.refresh_code);// Top刷新按钮
		mYaoYao = (Button) findViewById(R.id.yaoyao_jisuan);// 摇一摇机选
		missCheck = (CheckBox) findViewById(R.id.missCheck);// 遗漏值开关

//		titleOne.setText(getString(R.string.cq_11_5));
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
	int[] cqArea={5,6};
	
	/**
	 * 创建普通界面
	 */
	private void createViewPT(int id){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if(state.equals("PT_R2")){
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 2, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_R3")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 3, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_R4")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 4, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_R5")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 5, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_R6")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 6, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_R7")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 7, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_R8")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 8, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_QZ1")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_QZ2")) {
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "万位","", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "千位","", false, true, true);
		}else if (state.equals("PT_QZ3")) {
			areaNums = new AreaNum[3];
			areaNums[0] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "万位","", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "千位","", false, true, true);
			areaNums[2] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "百位","", false, true, true);
		}else if (state.equals("PT_ZU2")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 2, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}else if (state.equals("PT_ZU3")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 3, 11, BallResId, 0, 1,Color.RED, "","", false, true, true);
		}
		createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.NULL,id, true);
	}
	/**
	 * 创建胆拖界面
	 */
	private void createViewDT(int id){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if(state.equals("DT_R2")){
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 1, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码   选1个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 2, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码  选2-10个", false, true, true);
		}else if(state.equals("DT_R3")){
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 2, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码   至少选1个，最多2个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 2, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码  选2-10个", false, true, true);
		}else if(state.equals("DT_R4")){
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 3, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码   至少选1个，最多3个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 3, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码  选2-10个", false, true, true);
		}else if(state.equals("DT_R5")){
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 4, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码   至少选1个，最多4个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 2, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码  选2-10个", false, true, true);
		}else if(state.equals("DT_R6")){
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 5, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码   至少选1个，最多5个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 2, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码  选2-10个", false, true, true);
		}else if(state.equals("DT_R7")){
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 6, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码   至少选1个，最多6个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 2, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码  选2-10个", false, true, true);
		}else if(state.equals("DT_R8")){
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 7, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码   至少选1个，最多7个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 2, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码  选2-10个", false, true, true);
		}else if (state.equals("DT_ZU2")) {
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码    选1个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 2, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码    选2-10个", false, true, true);
		}else if (state.equals("DT_ZU3")) {
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 2, BallResId, 0, 1,Color.RED, "胆码","我认为必出的号码   至少选1个，最多2个", false, true, true);
			areaNums[1] = new AreaNum(cqArea, 2, 10, BallResId, 0, 1,Color.RED, "拖码","我认为可能出的号码 选2-10个", false, true, true);
		}
		createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.NULL,id, true);
	}
	/**
	 * 事件处理
	 */
	public void action() {
		missView.clear();
		childtype = new String[] { "自选" };
		init();
		childtypes.setVisibility(View.GONE);
		group.setOnCheckedChangeListener(this);
		group.check(0);
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
	
	private void setTitle(String titleType){
		if(tag==1){
			titleOne.setText(TITLE+"--"+titleType+"--普通");
		}else if (tag==2) {
			titleOne.setText(TITLE+"--"+titleType+"--胆拖");
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRun = false;
	}
	//...miqingqiang start
	private ChoosePTPopuAdapter showMenuAdapterFirst;
	private ChooseDTPopuAdapter showMenuAdapterSecond;
	public void createMenuDialog(){
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = (LinearLayout) inflate.inflate(R.layout.eleven_choose_five_list, null);
			
		mGridViewFirst = (MyGridView) popupView.findViewById(R.id.chooseviewfirst);
		String[] str1=getResources().getStringArray(R.array.dlc_type);
		List<String> stoogesFirst = Arrays.asList(str1);
		showMenuAdapterFirst = new ChoosePTPopuAdapter(this,new popFirstOnItemChick(),stoogesFirst);
		mGridViewFirst.setAdapter(showMenuAdapterFirst);
			
		mGridViewSecond=(MyGridView)popupView.findViewById(R.id.chooseviewsecond);
		String[] str2=getResources().getStringArray(R.array.choose_type);
		List<String> stoogesSecond=Arrays.asList(str2);
		showMenuAdapterSecond = new ChooseDTPopuAdapter(this,new popSecondOnItemChick(),stoogesSecond);
		mGridViewSecond.setAdapter(showMenuAdapterSecond);
			
		popupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(reBtn);
		
		if(tag==1){
			showMenuAdapterFirst.setItemSelect(itemId);
			showMenuAdapterFirst.notifyDataSetInvalidated();
			showMenuAdapterSecond.setItemSelect(-1);
			showMenuAdapterSecond.notifyDataSetInvalidated();
		}else if(tag==2){
			showMenuAdapterSecond.setItemSelect(itemId);
			showMenuAdapterSecond.notifyDataSetInvalidated();
			showMenuAdapterFirst.setItemSelect(-1);
			showMenuAdapterFirst.notifyDataSetInvalidated();
		}
		
	}
	
	/**
	 * 普通点击事件
	 * @author 
	 *
	 */
	public class popFirstOnItemChick implements OnChickItem{

		@Override
		public void onChickItem(View view, int position, String text) {
			// TODO Auto-generated method stub
			tag=1;
			state = pt_types[position];
			itemId=position;
			setTitle(text);
			showMenuAdapterSecond.setItemSelect(-1);
			showMenuAdapterSecond.notifyDataSetInvalidated();
			
			showMenuAdapterFirst.setItemSelect(position);
			showMenuAdapterFirst.notifyDataSetInvalidated();
			popupWindow.dismiss();
			setPtBetPrompt(position);
			action();
		}
			
	}
	/**
	 * 胆拖点击事件
	 * @author 
	 *
	 */
	public class popSecondOnItemChick implements OnDtChickItem{
		@Override
		public void onChickItem(View view, int position, String text) {
			tag=2;
			state = dt_types[position];
			itemId=position;
			setTitle(text);
			showMenuAdapterFirst.setItemSelect(-1);
			showMenuAdapterFirst.notifyDataSetInvalidated();
			
			showMenuAdapterSecond.setItemSelect(position);
			showMenuAdapterSecond.notifyDataSetInvalidated();
			
			popupWindow.dismiss();
			setDtBetPrompt(position);
			action();
		}
	}
}
