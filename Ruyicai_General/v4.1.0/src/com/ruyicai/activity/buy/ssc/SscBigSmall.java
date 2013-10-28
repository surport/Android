package com.ruyicai.activity.buy.ssc;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.ssc.BigSamllCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.SscMissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicMethod;

public class SscBigSmall extends ZixuanAndJiXuan {
	public boolean isjixuan = false;
	public static final int SSC_TYPE = 5;// 大小双单
	public static SscBigSmall self;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setAddView(((Ssc) getParent()).addView);
		super.onCreate(savedInstanceState);
		lotno = Constants.LOTNO_SSC;
		lotnoStr = Constants.LOTNO_SSC;
		isbigsmall = true;
		childtype = new String[] { "直选" };
		setContentView(R.layout.sscbuyview);
		sscCode = new BigSamllCode();
		self = this;
		highttype = "SSC";
		theMethodYouWantToCall();
	}

	public void theMethodYouWantToCall() {
		// do what ever you want here
		init();
		childtypes.setVisibility(View.GONE);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		onCheckAction(checkedId);
		isMissNet(new SscMissJson(), sellWay, false);// 获取遗漏值
	}

	public void onCheckAction(int checkedId) {
		switch (checkedId) {
		case 0:
			radioId = 0;
			sellWay = MissConstant.SSC_MV_DD;
			isjixuan = false;
			BallTable getable;
			iProgressBeishu = 1;
			iProgressQishu = 1;
			String shititle = "十位区：";
			String getitle = "个位区：";
			areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(4, 10, 1, 1, BallResId, 0, 0, Color.RED,
					shititle, false, true);
			areaNums[1] = new AreaNum(4, 10, 1, 1, BallResId, 0, 0, Color.RED,
					getitle, false, true);
			createView(areaNums, sscCode, BIG_SMALL, true, checkedId, true);
			BallTable = areaNums[0].table;
			getable = areaNums[1].table;
			break;
		// case 1:
		// radioId = 1;
		// isjixuan=true;
		// iProgressBeishu = 1;iProgressQishu = 1;
		// SscBalls sscb = new SscBalls();
		// createviewmechine(sscb,checkedId);
		// break;
		}
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		lotnoStr = Constants.LOTNO_SSC;
	}

	public String setTemp(int aBallViewText, int iBallViewNo, int col) {
		String iStrTemp = "";
		switch (col) {
		case 0:
			iStrTemp = "大";
			break;
		case 1:
			iStrTemp = "小";
			break;
		case 2:
			iStrTemp = "单";
			break;
		case 3:
			iStrTemp = "双";
			break;
		}
		return iStrTemp;
	}

	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		String zhumas = "";
		int num = 0;// 高亮小球数
		int length = 0;
		for (int j = 0; j < areaNums.length; j++) {
			BallTable ballTable = areaNums[j].table;
			int[] zhuMa = ballTable.getHighlightBallNOsbigsmall();
			for (int i = 0; i < ballTable.getHighlightBallNOsbigsmall().length; i++) {
				zhumas += PublicMethod.getbigsmalZhumastr(zhuMa[i]);
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
			showEditTitle(BIG_SMALL);
		} else {
			builder.append(zhumas);
			String zhuma[] = zhumas.split("\\,");
			for (int i = 0; i < zhuma.length; i++) {
				if (i != 0) {
					length += zhuma[i].length() + 1;
				} else {
					length += zhuma[i].length();
				}
				if (i != zhuma.length - 1) {
					builder.setSpan(new ForegroundColorSpan(Color.BLACK),
							length, length + 1, Spanned.SPAN_COMPOSING);
				}
				builder.setSpan(new ForegroundColorSpan(areaNums[i].textColor),
						length - zhuma[i].length(), length,
						Spanned.SPAN_COMPOSING);

			}
			editZhuma.setText(builder, BufferType.EDITABLE);
			showEditTitle(NULL);
		}
	}

	public String getZhuma() {
		String zhuma = "";
		zhuma = sscCode.zhuma(areaNums, iProgressBeishu, 0);
		return zhuma;
	}

	@Override
	public String getZhuma(Balls ball) {
		String zhuma = "";
		zhuma = ball.getZhuma(null, SSC_TYPE);
		return zhuma;
	}

	public int getZhuShu() {
		int iReturnValue = 0;
		if (isjixuan) {
			int beishu = iProgressBeishu;
			iReturnValue = balls.size() * beishu;
		} else {
			int shi = areaNums[0].table.getHighlightBallNums();
			int ge = areaNums[1].table.getHighlightBallNums();
			int beishu = iProgressBeishu;
			iReturnValue = shi * ge * beishu;
		}
		return iReturnValue;
	}

	public String getZxAlertZhuma() {
		int iZhuShu = getZhuShu();

		int[] shi = areaNums[0].table.getHighlightBallNOsbigsmall();
		int[] ge = areaNums[1].table.getHighlightBallNOsbigsmall();

		return "注码：" + "\n" + "十位：" + getStrZhuMa(shi) + "\n" + "个位："
				+ getStrZhuMa(ge);

	}

	public String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			switch (balls[i]) {
			case 1:
				str += "大";// 大
				break;
			case 2:
				str += "小";// 小
				break;
			case 3:
				str += "单";// 单
				break;
			case 4:
				str += "双";// 双
				break;
			}
		}
		return str;
	}

	public String getStrZhuMajixuan(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			switch (balls[i] + 1) {
			case 1:
				str += "大";// 大
				break;
			case 2:
				str += "小";// 小
				break;
			case 3:
				str += "单";// 单
				break;
			case 4:
				str += "双";// 双
				break;
			}
		}
		return str;

	}

	public String isTouzhu() {
		String isTouzhu = "";
		int iZhuShu = getZhuShu();
		int shi = areaNums[0].table.getHighlightBallNums();
		int ge = areaNums[1].table.getHighlightBallNums();
		if (shi == 0 | ge == 0) {
			isTouzhu = "您还没有进行选择！";
		} else if (iZhuShu > MAX_ZHU) {
			// dialogExcessive();
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String iTempString;
		int iZhuShu = getZhuShu();
		if (iZhuShu != 0) {
			iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		} else {
			iTempString = getResources().getString(
					R.string.please_choose_number);
		}
		return iTempString;
	}

	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub
		int zhuShu = getZhuShu();
		if (isjixuan) {
			betAndGift.setSellway("1");
		} else {
			betAndGift.setSellway("0");
		}// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_SSC);
		betAndGift.setBet_code(getZhuma());
		betAndGift.setAmount("" + zhuShu * 200);
		betAndGift.setBatchcode(Ssc.batchCode);

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
				areaNums[i].table.changeBallState(areaNums[i].chosenBallSum,
						nBallId);
				break;
			}

		}
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_SSC);
		codeInfo.setTouZhuType("bigsmall");
	}

}
