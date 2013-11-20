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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.cq11xuan5.Cq11xuan5Code;
import com.ruyicai.code.cq11xuan5.Cq11xuan5DanTuoCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.DlcRxBalls;
import com.ruyicai.json.miss.CQ11X5MissJson;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class Cq11Xuan5 extends ZixuanAndJiXuan {
	/** 标题栏*/
	private RelativeLayout titleBarRelativeLayout;
	/** 标题文本 */
	private TextView titleBarTextView;
	/**玩法提示*/
	private TextView playMethodTextView;
	/**遗漏值开关*/
	private CheckBox missCheckBox;
	/**截止时间*/
	private TextView endTimeTextView;
	/**机选按钮*/
	private Button jixuanButton;
	/**彩票信息栏*/
	private RelativeLayout lotteryinfoRelativeLayout;
	/**选号按钮图片*/
	protected int BallResId[] = { R.drawable.cq_11_5_ball_normal, R.drawable.cq_11_5_ball_select };
	/**玩法标识:1普通，2胆拖*/
	private int playMethodTag=1;
	
	private static final String TITLE="重庆11选5";
	protected String pt_types[] = { "PT_R2", "PT_R3", "PT_R4", "PT_R5", "PT_R6", "PT_R7","PT_R8",
			"PT_QZ1", "PT_QZ2", "PT_QZ3", "PT_ZU2", "PT_ZU3" };// 普通类型
	protected String dt_types[] = { "DT_R2", "DT_R3", "DT_R4", "DT_R5", "DT_R6", "DT_R7", "DT_R8",
			"DT_ZU2", "DT_ZU3" };// 胆拖类型
	protected int nums[] = { 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 2, 3 };// 单式机选个数
	protected int dannums[] = { 2, 3, 4, 5, 6, 7, 8, 2, 3 };// 单式机选个数
	public static String state;// 当前类型
	int lesstime;// 剩余时间
	public static String batchCode;// 期号
	private boolean isRun = true;
	private MyGridView mGridViewFirst,mGridViewSecond;
	private PopupWindow popupWindow;
	private Button returnBtn;
	private int lottypeIndex = 0;
	private static final String PT="pt";//普通
	private static final String DT="dt";//胆拖
	private String showMessage = "";
	private int itemId=3;
	private int checkedId;
	public AddView addView = new AddView(this);

	
	private HistoryNumberActivity test;
	public boolean isJiXuan = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAddView(addView);
		lotno = Constants.LOTNO_CQ_ELVEN_FIVE;
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutMain = inflater.inflate(R.layout.buy_cq_eleven_five_main, null);
		setContentView(layoutMain);
		highttype = "CQ_ELEVEN_FIVE";
		state = "PT_R5";
		initShow();
		setIssue(lotno);
		lotnoStr = lotno;
		action();
		setTitle("任选五");
		setDtBetPrompt(3);
		latestLotteryList.setVisibility(View.GONE);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		this.checkedId=checkedId;
		onCheckAction(checkedId);
	}
	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int zhuShu = getZhuShu();
		return "您以选择了" + zhuShu + "注，共" + zhuShu * 2 + "元";
	}
	
	/**
	 * 设置投注金额提示
	 */
	public void showEditText(){
		editZhuma.setText(textSumMoney(areaNums, iProgressBeishu));
		showEditTitle(NULL);
	}
	
	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId) {
		int nBallId = 0;
		for (int i = 0; i < areaNums.length; i++) {
			nBallId = iBallId;
			iBallId = iBallId - areaNums[i].areaNum;

			if (iBallId < 0) {
				if (playMethodTag == 2) {
					if (i == 0) {
						int isHighLight = areaNums[0].table.changeBallState(
								areaNums[0].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT
								&& areaNums[1].table.getOneBallStatue(nBallId) != 0) {
							areaNums[1].table.clearOnBallHighlight(nBallId);
							showBetInfo(getResources().getString(
									R.string.ssq_toast_danma_title));
						}

					} else if (i == 1) {
						int isHighLight = areaNums[1].table.changeBallState(
								areaNums[1].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT
								&& areaNums[0].table.getOneBallStatue(nBallId) != 0) {
							areaNums[0].table.clearOnBallHighlight(nBallId);
							showBetInfo(getResources().getString(
									R.string.ssq_toast_tuoma_title));
						}
					}
				} else {
					areaNums[i].table.changeBallState(
							areaNums[i].chosenBallSum, nBallId);
				}
				break;
			}

		}

	}

	@Override
	public String isTouzhu() {
		String isTouzhu = "";
		int iZhuShu = getZhuShu();
		if (playMethodTag == 2) {
			isTouzhu = getIsTouzhuStatus(iZhuShu);
		}
		//普通投注：前2直选
		else if (state.equals("PT_QZ2")) {

			if (iZhuShu == 0) {
				isTouzhu = "请在第万位和第千位至少选择一个球，再进行投注！";
			} else if (iZhuShu > MAX_ZHU) {
				isTouzhu = "false";
			} else {
				isTouzhu = "true";
			}
		}
		//前3直选
		else if (state.equals("PT_QZ3")) {

			if (iZhuShu == 0) {
				isTouzhu = "请在第一位、第二位和第三位至少选择一个球，再进行投注！";
			} else if (iZhuShu > MAX_ZHU) {
				isTouzhu = "false";
			} else {
				isTouzhu = "true";
			}
		} else {
			// 任选二，任选三，任选四，任选五，任选六，任选七，任选八，前2组选，前3组选
			int ballNums = areaNums[0].table.getHighlightBallNums();
			int oneNum = nums[itemId] - ballNums;
			if (oneNum > 0) {
				isTouzhu = "请再选择" + oneNum + "球，再进行投注！";
			} else if (iZhuShu > MAX_ZHU) {
				isTouzhu = "false";
			} else {
				isTouzhu = "true";
			}
		}
		return isTouzhu;
	}
	
	/**
	 * 获得胆拖投注的投注状态
	 * 
	 * @param iZhuShu
	 * @return
	 */
	private String getIsTouzhuStatus(int iZhuShu) {
		String isTouzhu = "";
		int tuoNum = 10;
		int dan = areaNums[0].table.getHighlightBallNums();
		int tuo = areaNums[1].table.getHighlightBallNums();
		if (dan + tuo < dannums[itemId] + 1 || dan < 1 || dan > dannums[itemId] || tuo < 2
				|| tuo > tuoNum) {
			if (state.equals("DT_R2") || state.equals("DT_ZU2")) {
				isTouzhu = "请选择:\"1个胆码；\n" + " 2~" + tuoNum + "个拖码；\n"
						+ " 胆码与拖码个数之和不小于" + (dannums[itemId] + 1) + "个";
			} else if(state.equals("DT_ZU3")){
				isTouzhu = "请选择:\n1~2个胆码；\n" + " 2~" + tuoNum
						+ "个拖码；\n" + " 胆码与拖码个数之和不小于" + (dannums[itemId] + 1) + "个";
			}else{
				isTouzhu = "请选择:\n1~" + (dannums[itemId] - 1) + "个胆码；\n" + " 2~" + tuoNum
						+ "个拖码；\n" + " 胆码与拖码个数之和不小于" + (dannums[itemId] + 1) + "个";
			}
		} else if (iZhuShu > MAX_ZHU) {
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}
	
	/**
	 * 判断球数
	 * 
	 * @param ballNums
	 * @param num
	 * @return
	 */
	private boolean checkBallNum(int ballNums, int num) {
		if ("PT_R8".equals(state) && Constants.LOTNO_GD_11_5.equals(lotno)) {
			if (ballNums > num) {
				showMessage = "只能选择" + num + "个球进行投注！";
				return false;
			}
		}
		return true;
	}


	@Override
	public int getZhuShu() {
		int zhushu = 0;
		if (isJiXuan) {
			zhushu = balls.size() * iProgressBeishu;
		}
		//如果是胆拖玩法
		else if (playMethodTag==2) {
			int dan = areaNums[0].table.getHighlightBallNums();
			int tuo = areaNums[1].table.getHighlightBallNums();
			zhushu = (int) getDTZhuShu(dan, tuo, iProgressBeishu);
		}
		//如果是普通投注
		else {
			if (state.equals("PT_QZ2")) {//普通前二组选
				zhushu = getzhushuQ2(areaNums[0].table.getHighlightStr(),
						areaNums[1].table.getHighlightStr()) * iProgressBeishu;
			} else if (state.equals("PT_QZ3")) {//普通前三组选
				zhushu = getzhushuQ3(areaNums[0].table.getHighlightStr(),
						areaNums[1].table.getHighlightStr(),
						areaNums[2].table.getHighlightStr())
						* iProgressBeishu;
			} else {
				int ballNums = areaNums[0].table.getHighlightBallNums();
				zhushu = (int) PublicMethod.zuhe(nums[itemId], ballNums)* iProgressBeishu;
			}
		}
		return zhushu;
	}
	/**
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @return long 注数
	 */
	protected long getDTZhuShu(int dan, int tuo, int iProgressBeishu) {
		long ssqZhuShu = 0L;
		if (dan > 0 && tuo > 0) {
			ssqZhuShu += (PublicMethod.zuhe(dannums[itemId] - dan, tuo) * iProgressBeishu);
		}
		return ssqZhuShu;
	}
	/**
	 * 前二直选玩法注数计算方法
	 * @param wan
	 * @param qian
	 * @return
	 */
	public int getzhushuQ2(String[] wan, String[] qian) {
		int zhushu = 0;
		for (int i = 0; i < wan.length; i++) {
			for (int j = 0; j < qian.length; j++) {
				if (!wan[i].equals(qian[j])) {
					zhushu++;
				}
			}
		}
		return zhushu;
	}
	/**
	 * 前三直选玩法注数计算方法
	 * @param wan
	 * @param qian
	 * @param bai
	 * @return
	 */
	public int getzhushuQ3(String[] wan, String[] qian, String[] bai) {
		int zhushu = 0;
		for (int i = 0; i < wan.length; i++) {
			for (int j = 0; j < qian.length; j++) {
				if (!wan[i].equals(qian[j])) {
					for (int k = 0; k < bai.length; k++) {
						if (!bai[k].equals(qian[j]) && !bai[k].equals(wan[i])) {
							zhushu++;

						}
					}
				}
			}
		}
		return zhushu;
	}

	@Override
	public String getZhuma() {
		String zhuma = "";
		if (playMethodTag == 2) {
			zhuma = Cq11xuan5DanTuoCode.zhuma(areaNums, state);
		} else {
			zhuma = Cq11xuan5Code.zhuma(areaNums, state);
		}
		return zhuma;
	}

	@Override
	public String getZhuma(Balls ball) {
		String zhuma = "";
		zhuma = DlcRxBalls.getZhuma(ball, state);
		return zhuma;
	}

	@Override
	public void touzhuNet() {
		int zhuShu = getZhuShu();
		if (isJiXuan) {
			betAndGift.setSellway("1");
		} else {
			betAndGift.setSellway("0");
		}// 1代表机选 0代表自选
		betAndGift.setLotno(lotno);
		betAndGift.setBet_code(getZhuma());
		betAndGift.setAmount("" + zhuShu * 200);
		betAndGift.setBatchcode(batchCode);
	}

	@Override
	public void onCheckAction(int checkedId) {
		switch (checkedId) {
		case 0:
			if(playMethodTag==1){
				createViewPT(checkedId);
			}else if (playMethodTag==2) {
				createViewDT(checkedId);
			}

			if (!state.equals("PT_QZ2") && !state.equals("PT_QZ3")
					&& !state.equals("PT_QZ1") && !state.equals("PT_ZU2")
					&& !state.equals("PT_ZU2") && !state.equals("PT_ZU3")) {
				test = new HistoryNumberActivity(this);
			}
			showEditText();
			break;
		default:
			break;
		}
	}
	
	private void initShow() {
		lotteryinfoRelativeLayout = (RelativeLayout) findViewById(R.id.cq11xuan5_lotteryinfo_relativelayout);
		titleBarTextView = (TextView) findViewById(R.id.cq11xuan5_title_textview);
		playMethodTextView = (TextView) findViewById(R.id.cq11xuan5_palymethod_textview);
		endTimeTextView = (TextView) findViewById(R.id.cq11xuan5_endtime_textview);
		jixuanButton = (Button) findViewById(R.id.cq11xuan5_jisuan_button);
		jixuanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				baseSensor.action();
			}
		});
		missCheckBox = (CheckBox) findViewById(R.id.cq11xuan5_miss_checkbox);
		missCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				controlShowMiss(isChecked);
			}
		});
		titleBarRelativeLayout = (RelativeLayout) findViewById(R.id.cq11xuan5_title_relativelayout);
		titleBarRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPlayMethodMenuDialog();
			}
		});
	}
	
	private void controlShowMiss(boolean isChecked) {
		if (!isChecked) {
			for (int area_i = 0; area_i < areaNums.length; area_i++) {
				int rowNum = areaNums[area_i].tableLayout
						.getChildCount();
				for (int row_j = 0; row_j < rowNum; row_j++) {
					if(row_j % 2 != 0){
						areaNums[area_i].tableLayout
						.getChildAt(row_j)
						.setVisibility(View.GONE);
					}
				}
			}

		} else {
			for (int area_i = 0; area_i < areaNums.length; area_i++) {
				int rowNum = areaNums[area_i].tableLayout
						.getChildCount();
				for (int row_j = 0; row_j < rowNum; row_j++) {
					if(row_j % 2 != 0){
						areaNums[area_i].tableLayout
						.getChildAt(row_j)
						.setVisibility(View.VISIBLE);
					}
				}
			}
		}
	}

	/**
	 * 赋值给当前期
	 * 
	 * @param type彩种编号
	 */
	public void setIssue(final String lotno) {
		final Handler sscHandler = new Handler();
		endTimeTextView.setText("获取中...");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
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
										endTimeTextView.setText("距" + batchCode.substring(8) + "期截止:"
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
										endTimeTextView.setText("剩余时间:00:00");
										nextIssue();
									}
								});
								break;
							}
						}
					} catch (Exception e) {
						sscHandler.post(new Runnable() {
							public void run() {
								endTimeTextView.setText("获取失败");
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
		new AlertDialog.Builder(Cq11Xuan5.this)
				.setTitle("提示")
				.setMessage(
						titleBarTextView.getText().toString() + "第" + batchCode
								+ "期已经结束,是否转入下一期")
				.setNegativeButton("转入下一期", new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						setIssue(lotno);
					}

				})
				.setNeutralButton("返回主页面",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Cq11Xuan5.this.finish();
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
		if (state.equals("PT_QZ2")) {
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "万位","", false, true, false);
			areaNums[1] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "千位","", false, true, false);
			createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.CQ_QE,id, true);
		}else if (state.equals("PT_QZ3")) {
			areaNums = new AreaNum[3];
			areaNums[0] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "万位","", false, true, false);
			areaNums[1] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "千位","", false, true, false);
			areaNums[2] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "百位","", false, true, false);
			createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.CQ_QS,id, true);
		}else if(state.equals("PT_QZ1")){
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 1, 11, BallResId, 0, 1,Color.RED, "","", false, true, false);
			createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.CQ_QY,id, true);
		}else if (state.equals("PT_ZU2")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 2, 11, BallResId, 0, 1,Color.RED, "","", false, true, false);
			createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.NULL,id, true);
		}else if (state.equals("PT_ZU3")) {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, 3, 11, BallResId, 0, 1,Color.RED, "","", false, true, false);
			createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.NULL,id, true);
		} else {
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(cqArea, nums[itemId], 11, BallResId, 0, 1,Color.RED, "","", false, true, false);
			createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.NULL,id, true);
		}
		setBottomView();
	}
	/**
	 * 创建胆拖界面
	 *
	 */
	int[] dtNum={1,2,3,4,5,6,7,1,2};// 胆拖选区最大小球数
	private static final String dtTPrompt="我认为可能出的号码  选2-10个";//拖码投注提示
	//胆码投注提示
	private String dtDPrompt(int a){
		String str="";
		if(state.equals("DT_R2")
				||state.equals("DT_ZU2")){
			str="我认为必出的号码   选1个";
		}else if (state.equals("DT_ZU3")) {
			str="我认为必出的号码   至少选1个，最多2个";
		}else {
			str="我认为必出的号码   至少选1个，最多"+(itemId+1)+"个";
		}
		return str;
	}
	private void createViewDT(int id){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		areaNums = new AreaNum[2];
		areaNums[0] = new AreaNum(cqArea, 1, dtNum[itemId], BallResId, 0, 1,Color.RED, "胆码",dtDPrompt(itemId), false, true, true);
		areaNums[1] = new AreaNum(cqArea, 10, 10, BallResId, 0, 1,Color.RED, "拖码",dtTPrompt, false, true, true);
		baseSensor.stopAction();
		createViewCQ(areaNums, sscCode, ZixuanAndJiXuan.NULL,id, true);
		setBottomView();
	}
	/**
	 * 设置底部显示
	 */
	private void setBottomView(){
		if(state.equals("PT_QZ1")
				||state.equals("PT_QZ2")
				||state.equals("PT_QZ3")
				||state.equals("PT_ZU2")
				||state.equals("PT_ZU3")
				||state.equals("DT_ZU2")
				||state.equals("DT_ZU3")){
			latestLotteryList.setVisibility(View.VISIBLE);
			simulateSelectNumberView.setVisibility(View.GONE);
		}else {
			latestLotteryList.setVisibility(View.GONE);
			simulateSelectNumberView.setVisibility(View.VISIBLE);
		}
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
		setSellWay();
	}
	
	public void setSellWay() {
		if(state.equals("PT_QZ1")){
			sellWay = MissConstant.CQ11X5_PT_Q1_Z;
			highttype = "CQ11X5_PT_QZ1";
		}else if(state.equals("PT_QZ2")){
			sellWay = MissConstant.CQ11X5_PT_Q2_Z;
			highttype = "CQ11X5_PT_QZ2";
		}else if(state.equals("PT_QZ3")){
			sellWay = MissConstant.CQ11X5_PT_Q3_Z;
			highttype = "CQ11X5_PT_QZ3";
		} else if (state.equals("PT_ZU2") || state.equals("DT_ZU2")) {
			sellWay = MissConstant.CQ11X5_PT_Q2_ZU;
		} else if (state.equals("PT_ZU3") || state.equals("DT_ZU3")) {
			sellWay = MissConstant.CQ11X5_PT_Q3_ZU;
		} else {
			sellWay = MissConstant.CQ11X5_PT_RX;
		}
		
		isMissNet(new CQ11X5MissJson(), sellWay, false);// 获取遗漏值
	}
	/**
	 * 普通投注提示
	 */
	private void setPtBetPrompt(int type) {
		switch (type) {
		//普通
		case 0:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_rx_2));
			break;
		case 1:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_rx_3));
			break;
		case 2:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_rx_4));
			break;
		case 3:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_rx_5));
			break;
		case 4:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_rx_6));
			break;
		case 5:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_rx_7));
			break;
		case 6:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_rx_8));
			break;
			//前一
		case 7:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_pt_qy));
			break;
			//前二直选
		case 8:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_pt_qe_zhix));
			break;
			//前二组选
		case 9:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_pt_qs_zhix));
			break;
			//前三直选
		case 10:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_pt_qe_zux));
			break;
		case 11:
			//前三组选
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_pt_qs_zux));
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
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_2));
			break;
		case 1:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_3));
			break;
		case 2:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_4));
			break;
		case 3:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_5));
			break;
		case 4:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_6));
			break;
		case 5:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_7));
			break;
		case 6:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_8));
			break;
			//胆拖前二组选
		case 7:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_qe_zux));
			break;
			//胆拖前三组选
		case 8:
			playMethodTextView.setText(getString(R.string.cq_11_5_prize2_dt_qs_zux));
			break;

		default:
			break;
		}
	}
	/**
	 * 设置标题
	 * @param titleType玩法名称
	 */
	private void setTitle(String titleType){
		if(playMethodTag==1){
			titleBarTextView.setText(TITLE+"--"+titleType+"--普通");
		}else if (playMethodTag==2) {
			titleBarTextView.setText(TITLE+"--"+titleType+"--胆拖");
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRun = false;
	}
	
	private ChoosePTPopuAdapter showMenuAdapterFirst;
	private ChooseDTPopuAdapter showMenuAdapterSecond;
	/**
	 * 显示玩法菜单
	 */
	public void showPlayMethodMenuDialog(){
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
		popupWindow.showAsDropDown(titleBarRelativeLayout);
		
		if(playMethodTag==1){
			showMenuAdapterFirst.setItemSelect(itemId);
			showMenuAdapterFirst.notifyDataSetInvalidated();
			showMenuAdapterSecond.setItemSelect(-1);
			showMenuAdapterSecond.notifyDataSetInvalidated();
		}else if(playMethodTag==2){
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
			playMethodTag=1;
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
			playMethodTag=2;
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
	
	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_CQ_ELVEN_FIVE);
		if (playMethodTag == 1) {
			codeInfo.setTouZhuType("zhixuan");
		} else {
			codeInfo.setTouZhuType("dantuo");
		}
	}
}
