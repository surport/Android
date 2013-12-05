package com.ruyicai.activity.buy.ten;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.notice.NoticeActivityGroup;
import com.ruyicai.code.ten.TenCode;
import com.ruyicai.code.ten.TenDanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.ElevenBalls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;

public class TenActivity extends Dlc {
	protected String tenType[] = { "S1", "H1", "R2", "R3", "R4", "R5", "Q2",
			"Q3", "Z2", "Z3" };// 类型
	protected int tenNums[] = { 1, 1, 2, 3, 4, 5, 1, 1, 2, 3 };
	private ArrayAdapter<CharSequence> adapter = null;// 要使用的Adapter

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "onCreate()");
		}
		setLotnoX(Constants.LOTNO_ten);
		setTitleOne(getString(R.string.tenTitle));
		highttype = "DLC";
		setLotno();
		initSpinner();
		initGroup();
		setTitleOne(getString(R.string.tenTitle));
		imgRetrun.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createDialog(NoticeActivityGroup.ID_SUB_GD10_LISTVIEW);
			}
		});
		MobclickAgent.onEvent(this, "guangdongkuaileshifen"); // BY贺思明
																// 点击首页的“广东快乐十分”图标
		MobclickAgent.onEvent(this, "gaopingoucaijiemian ");// BY贺思明 高频购彩页面

	}

	/**
	 * 初始化胆拖选区
	 */
	public void initDTArea() {
		areaNums = new AreaNum[2];
		areaNums[0] = new AreaNum(20, 10, num - 1, BallResId, 0, 1, Color.RED,
				"胆码");
		areaNums[1] = new AreaNum(20, 10, 20, BallResId, 0, 1, Color.RED, "拖码");
	}

	@Override
	public boolean getIsLuck() {
		return true;
	}

	/**
	 * spinner处理事件
	 */
	public void action(int position) {
		state = tenType[position];
		num = tenNums[position];
		max = maxs[position];
		missView.clear();
		initGroup();
		setSellWay();
	}

	public void initSpinner() {
		typeSpinner = (Spinner) findViewById(R.id.buy_dlc_spinner);
		adapter = ArrayAdapter.createFromResource(this, R.array.ten_type,
				android.R.layout.simple_spinner_item);// 实例化ArrayAdapter
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		typeSpinner.setAdapter(adapter);
		childtypes = (LinearLayout) findViewById(R.id.buy_dlc_top);
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int position = typeSpinner.getSelectedItemPosition();
				action(position);
				startSensor();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		typeSpinner.setSelection(5);
	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		String wantitle = getString(R.string.qxc_first);
		String qiantitle = getString(R.string.qxc_second);
		String baititle = getString(R.string.qxc_third);
		if (state.equals("S1")) {
			String title = "请选择投注号码";
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(18, 10, 1, 18, BallResId, 0, 1,
					Color.RED, title, false, true);
		} else if (state.equals("H1")) {
			String title = "请选择投注号码";
			areaNums = new AreaNum[1];
			areaNums[0] = new AreaNum(2, 10, 1, 2, BallResId, 0, 19, Color.RED,
					title, false, true);
		} else if (state.equals("Q2")) {
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(20, 10, 1, 20, BallResId, 0, 1,
					Color.RED, wantitle, false, true, true);
			areaNums[1] = new AreaNum(20, 10, 1, 20, BallResId, 0, 1,
					Color.RED, qiantitle, false, true, true);
		} else if (state.equals("Q3")) {
			areaNums = new AreaNum[3];
			areaNums[0] = new AreaNum(20, 10, 1, 20, BallResId, 0, 1,
					Color.RED, wantitle, false, true, true);
			areaNums[1] = new AreaNum(20, 10, 1, 20, BallResId, 0, 1,
					Color.RED, qiantitle, false, true, true);
			areaNums[2] = new AreaNum(20, 10, 1, 20, BallResId, 0, 1,
					Color.RED, baititle, false, true, true);
		} else if (state.equals("Z2")) {
			areaNums = new AreaNum[1];
			String title = "请选择投注号码";
			areaNums[0] = new AreaNum(20, 10, 2, 20, BallResId, 0, 1,
					Color.RED, title, false, true);
		} else if (state.equals("Z3")) {
			areaNums = new AreaNum[1];
			String title = "请选择投注号码";
			areaNums[0] = new AreaNum(20, 10, 3, 20, BallResId, 0, 1,
					Color.RED, title, false, true);
		} else {
			areaNums = new AreaNum[1];
			String title = "请选择投注号码";
			int isChoseNum = typeSpinner.getSelectedItemPosition();
			areaNums[0] = new AreaNum(20, 10, isChoseNum, 20, BallResId, 0, 1,
					Color.RED, title, false, true);
		}
		return areaNums;
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
				if (is11_5DanTuo) {
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
				} else if (state.equals("Q2")) {
					if (i == 0) {
						int isHighLight = areaNums[0].table.changeBallState(
								areaNums[0].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
							areaNums[1].table.clearOnBallHighlight(nBallId);
						}
					} else {
						int isHighLight = areaNums[1].table.changeBallState(
								areaNums[1].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
							areaNums[0].table.clearOnBallHighlight(nBallId);
						}
					}
				} else if (state.equals("Q3")) {
					if (i == 0) {
						int isHighLight = areaNums[0].table.changeBallState(
								areaNums[0].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
							areaNums[1].table.clearOnBallHighlight(nBallId);
							areaNums[2].table.clearOnBallHighlight(nBallId);
						}
					} else if (i == 1) {
						int isHighLight = areaNums[1].table.changeBallState(
								areaNums[1].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
							areaNums[0].table.clearOnBallHighlight(nBallId);
							areaNums[2].table.clearOnBallHighlight(nBallId);
						}
					} else {
						int isHighLight = areaNums[2].table.changeBallState(
								areaNums[2].chosenBallSum, nBallId);
						if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
							areaNums[0].table.clearOnBallHighlight(nBallId);
							areaNums[1].table.clearOnBallHighlight(nBallId);
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

	public void updatePage() {
		Intent intent = new Intent(this, TenActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 中奖提示
	 */
	public void setTextPrize(int type) {
		textPrize.setTextSize(11);
		if (state.equals("S1")) {
			textPrize.setText(getString(R.string.ten_prize_rx_s_1));
		} else if (state.equals("H1")) {
			textPrize.setText(getString(R.string.ten_prize_rx_h_1));
		} else if (state.equals("Q2")) {
			textPrize.setText(getString(R.string.ten_prize_xq_2));
		} else if (state.equals("Q3")) {
			textPrize.setText(getString(R.string.ten_prize_xq_3));
		} else if (state.equals("Z3")) {
			textPrize.setText(getString(R.string.ten_prize_zx_3));
		} else if (state.equals("Z2")) {
			textPrize.setText(getString(R.string.ten_prize_zx_2));
		} else if (state.equals("R2")) {
			if (is11_5DanTuo) {
				textPrize.setText(getString(R.string.ten_prize_dan_rx_2));
			} else {
				textPrize.setText(getString(R.string.ten_prize_rx_2));
			}
		} else if (state.equals("R3")) {
			if (is11_5DanTuo) {
				textPrize.setText(getString(R.string.ten_prize_dan_rx_3));
			} else {
				textPrize.setText(getString(R.string.ten_prize_rx_3));
			}
		} else if (state.equals("R4")) {
			if (is11_5DanTuo) {
				textPrize.setText(getString(R.string.ten_prize_dan_rx_4));
			} else {
				textPrize.setText(getString(R.string.ten_prize_rx_4));
			}
		} else if (state.equals("R5")) {
			if (is11_5DanTuo) {
				textPrize.setText(getString(R.string.ten_prize_dan_rx_5));
			} else {
				textPrize.setText(getString(R.string.ten_prize_rx_5));
			}
		}
	}

	/**
	 * 初始化group
	 */
	public void initGroup() {
		if (state.equals("Z2") || state.equals("Z3") || state.equals("Q2")
				|| state.equals("Q3") || state.equals("S1")
				|| state.equals("H1")) {
			childtype = new String[] { "自选" };
			init();
			childtypes.setVisibility(View.GONE);
		} else {
			childtype = new String[] { "自选", "胆拖" };
			init();
		}
		group.setOnCheckedChangeListener(this);
		group.check(0);
	}

	/**
	 * 设置遗漏值类别
	 */
	public void setSellWay() {
		if (state.equals("Q2") || state.equals("R1")) {
			if (!sellWay.equals(MissConstant.ELV_MV_Q3)) {
				sellWay = MissConstant.ELV_MV_Q3;
			}
		} else if (state.equals("Z2")) {
			if (!sellWay.equals(MissConstant.ELV_MV_Q2Z)) {
				sellWay = MissConstant.ELV_MV_Q2Z;
			}
		} else if (state.equals("Z3")) {
			if (!sellWay.equals(MissConstant.ELV_MV_Q3Z)) {
				sellWay = MissConstant.ELV_MV_Q3Z;
			}
		} else if (state.equals("R5")) {
			// isMissNet(new
			// SscZMissJson(),MissConstant.ELV_MV_ZH_R5,true);//获取遗漏值
		} else if (state.equals("R7")) {
			// isMissNet(new
			// SscZMissJson(),MissConstant.ELV_MV_ZH_R7,true);//获取遗漏值
		} else if (state.equals("R8")) {
			// isMissNet(new SscZMissJson(),MissConstant.ELV_ZH_R8,true);//获取遗漏值
		} else if (state.equals("Q3")) {
			sellWay = MissConstant.ELV_MV_Q3;
			// isMissNet(new
			// SscZMissJson(),MissConstant.ELV_MV_Q3_ZH,true);//获取遗漏值
		} else {
			if (!sellWay.equals(MissConstant.ELV_MV_RX)) {
				sellWay = MissConstant.ELV_MV_RX;
			}
		}
		// isMissNet(new DlcMissJson(),sellWay,false);//获取遗漏值
	}

	/**
	 * 获得总注数
	 * 
	 * @return
	 */
	public int getZhuShu() {
		int zhushu = 0;
		if (isJiXuan) {
			zhushu = balls.size() * iProgressBeishu;
		} else if (is11_5DanTuo) {
			int dan = areaNums[0].table.getHighlightBallNums();
			int tuo = areaNums[1].table.getHighlightBallNums();
			zhushu = (int) getDTZhuShu(dan, tuo, iProgressBeishu);
		} else {
			if (state.equals("Q2")) {
				int oneNum = areaNums[0].table.getHighlightBallNums();
				int twoNum = areaNums[1].table.getHighlightBallNums();
				areaNums[0].table.getHighlightStr();
				zhushu = oneNum * twoNum * iProgressBeishu;
			} else if (state.equals("Q3")) {
				int oneNum = areaNums[0].table.getHighlightBallNums();
				int twoNum = areaNums[1].table.getHighlightBallNums();
				int thirdNum = areaNums[2].table.getHighlightBallNums();
				zhushu = oneNum * twoNum * thirdNum * iProgressBeishu;
			} else {
				int ballNums = areaNums[0].table.getHighlightBallNums();
				zhushu = (int) PublicMethod.zuhe(num, ballNums)
						* iProgressBeishu;
			}
		}
		return zhushu;
	}

	/**
	 * 设置彩种编号
	 * 
	 * @param lotno
	 */
	public void setLotno() {
		this.lotno = Constants.LOTNO_ten;
		lotnoStr = lotno;
	}

	/**
	 * 初始化自选选区
	 */
	public void createViewZx(int id) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		sscCode = new TenCode();
		initArea();
		createView(areaNums, sscCode, ZixuanAndJiXuan.NULL, true, id, false);
	}

	/**
	 * 初始化机选选区
	 */
	public void createViewJx(int id) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		ElevenBalls dlcb = new ElevenBalls(num);
		createviewmechine(dlcb, id);
	}

	/**
	 * 初始化胆拖选区
	 */
	public void createViewDT(int id) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		initDTArea();
		sscCode = new TenDanCode();
		createViewDanTuo(areaNums, sscCode, ZixuanAndJiXuan.NULL, true, id,
				false);

	}

	/**
	 * 投注注码
	 * 
	 * @return
	 */
	public String getZhuma() {
		String zhuma = "";
		if (is11_5DanTuo) {
			zhuma = TenDanCode.zhuma(areaNums, state);
		} else {
			zhuma = TenCode.zhuma(areaNums, state);
		}
		return zhuma;
	}

	/**
	 * 机选投注注码
	 */
	public String getZhuma(Balls ball) {
		String zhuma = "";
		zhuma = ElevenBalls.getZhuma(ball, state);
		return zhuma;
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_ten);
		if (radioId == 1) {
			codeInfo.setTouZhuType("dantuo");
		} else {
			codeInfo.setTouZhuType("zhixuan");
		}
	}

}
