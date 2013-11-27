package com.ruyicai.activity.buy.gdeleven;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.HighFrequencyNoticeHistroyActivity;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.eleven.Eleven;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.notice.NoticeActivityGroup;
import com.ruyicai.code.Gdeleven.GdelevenCode;
import com.ruyicai.code.Gdeleven.GdelevenDanTuoCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.GdelevenQxBalls;
import com.ruyicai.jixuan.GdelevenRxBalls;
import com.ruyicai.json.miss.DlcMissJson;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.SscZMissJson;
import com.umeng.analytics.MobclickAgent;

public class GdEleven extends Dlc {
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setLotnoX(Constants.LOTNO_GD115);
		setTitleOne(getString(R.string.gdeleven));
		highttype = "DLC";
		setLotno();
		initSpinner();
		initGroup();
		imgRetrun.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createDialog(NoticeActivityGroup.ID_SUB_GD115_LISTVIEW);
			}
		});
		MobclickAgent.onEvent(this, "guangdong11xuan5"); // BY贺思明
															// 点击首页的“广东11选5”图标
		MobclickAgent.onEvent(this, "gaopingoucaijiemian ");// BY贺思明 高频购彩页面
	}

	public void updatePage() {
		Intent intent = new Intent(GdEleven.this, Eleven.class);
		startActivity(intent);
		finish();
	}

	public void turnHosity() {
		Intent intent = new Intent(GdEleven.this,
				HighFrequencyNoticeHistroyActivity.class);
		intent.putExtra("lotno", Constants.LOTNO_GD_11_5);
		startActivity(intent);
	}

	/**
	 * 初始化group
	 */
	public void initGroup() {
		if (state.equals("R1") || state.equals("R8") || state.equals("Q2")
				|| state.equals("Q3")) {
			childtype = new String[] { "自选" };
			init();
			childtypes.setVisibility(View.GONE);
		} else if (state.equals("Z2") || state.equals("Z3")) {
			childtype = new String[] { "组选", "胆拖" };
			init();
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
			if (!sellWay.equals(MissConstant.gdELV_MV_Q3)) {
				sellWay = MissConstant.gdELV_MV_Q3;
			}
		} else if (state.equals("Z2")) {
			if (!sellWay.equals(MissConstant.gdELV_MV_Q2Z)) {
				sellWay = MissConstant.gdELV_MV_Q2Z;
			}
		} else if (state.equals("Z3")) {
			if (!sellWay.equals(MissConstant.gdELV_MV_Q3Z)) {
				sellWay = MissConstant.gdELV_MV_Q3Z;
			}
		} else if (state.equals("R5")) {
			isMissNet(new SscZMissJson(), MissConstant.gdELV_MV_ZH_R5, true);// 获取遗漏值
			sellWay = MissConstant.gdELV_MV_RX;
		} else if (state.equals("R7")) {
			isMissNet(new SscZMissJson(), MissConstant.gdELV_MV_ZH_R7, true);// 获取遗漏值
			sellWay = MissConstant.gdELV_MV_RX;
		} else if (state.equals("R8")) {
			isMissNet(new SscZMissJson(), MissConstant.gdELV_ZH_R8, true);// 获取遗漏值
			sellWay = MissConstant.gdELV_MV_RX;
		} else if (state.equals("Q3")) {
			sellWay = MissConstant.gdELV_MV_Q3;
			isMissNet(new SscZMissJson(), MissConstant.gdELV_MV_Q3_ZH, true);// 获取遗漏值
		} else {
			sellWay = MissConstant.gdELV_MV_RX;
		}
		isMissNet(new DlcMissJson(), sellWay, false);// 获取遗漏值
	}

	/**
	 * 设置彩种编号
	 * 
	 * @param lotno
	 */
	public void setLotno() {
		this.lotno = Constants.LOTNO_GD_11_5;
		lotnoStr = lotno;
	}

	/**
	 * 初始化自选选区
	 */
	public void createViewZx(int id) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		sscCode = new GdelevenCode();
		initArea();
		if (state.equals("R5")) {
			lineNum = 2;
			textSize = 2;
			createViewNew(areaNums, sscCode, ZixuanAndJiXuan.NULL, true, id);
		} else if (state.equals("R7")) {
			lineNum = 2;
			textSize = 2;
			createViewNew(areaNums, sscCode, ZixuanAndJiXuan.NULL, true, id);
		} else if (state.equals("R8")) {
			lineNum = 2;
			textSize = 2;
			createViewNew(areaNums, sscCode, ZixuanAndJiXuan.NULL, true, id);
		} else if (state.equals("Q3")) {
			lineNum = 2;
			textSize = 2;
			createViewNew(areaNums, sscCode, ZixuanAndJiXuan.NULL, true, id);
		} else {
			createView(areaNums, sscCode, ZixuanAndJiXuan.NULL, true, id, true);
		}
	}

	/**
	 * 初始化机选选区
	 */
	public void createViewJx(int id) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if (state.equals("Q2") || state.equals("Q3")) {
			GdelevenQxBalls dlcb = new GdelevenQxBalls(num);
			createviewmechine(dlcb, id);
		} else {
			GdelevenRxBalls dlcb = new GdelevenRxBalls(num);
			createviewmechine(dlcb, id);
		}
	}

	/**
	 * 初始化胆拖选区
	 */
	public void createViewDT(int id) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		initDTArea();
		sscCode = new GdelevenDanTuoCode();
		createViewDanTuo(areaNums, sscCode, ZixuanAndJiXuan.NULL, true, id,
				true);

	}

	/**
	 * 投注注码
	 * 
	 * @return
	 */
	public String getZhuma() {
		String zhuma = "";
		if (is11_5DanTuo) {
			zhuma = GdelevenDanTuoCode.zhuma(areaNums, state);
		} else {
			zhuma = GdelevenCode.zhuma(areaNums, state);
		}
		return zhuma;
	}

	/**
	 * 机选投注注码
	 */
	public String getZhuma(Balls ball) {
		String zhuma = "";
		zhuma = GdelevenRxBalls.getZhuma(ball, state);
		return zhuma;
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_GD_11_5);
		if (radioId == 1) {
			codeInfo.setTouZhuType("dantuo");
		} else {
			codeInfo.setTouZhuType("zhixuan");
		}
	}

}
