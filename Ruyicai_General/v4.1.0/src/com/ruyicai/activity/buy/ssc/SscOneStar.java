package com.ruyicai.activity.buy.ssc;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.json.miss.SscMissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicMethod;

public class SscOneStar extends ZixuanAndJiXuan {
	public static SscOneStar self;
	private boolean isjixuan = false;
	public static final int SSC_TYPE = 1;// 一星

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setAddView(((Ssc)getParent()).addView);
		lotno = Constants.LOTNO_SSC;
		super.onCreate(savedInstanceState);
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "onCreate()");
		}
		lotnoStr = Constants.LOTNO_SSC;
		childtype = new String[] { "直选" };
		setContentView(R.layout.sscbuyview);
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "onDestroy()");
		}
	}

	// 单选框切换直选，机选
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
			isjixuan = false;
			iProgressBeishu = 1;
			iProgressQishu = 1;
			initArea();
			createView(areaNums, sscCode, ZixuanAndJiXuan.ONE, true, checkedId,
					true);
			BallTable = areaNums[0].table;
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		lotnoStr = Constants.LOTNO_SSC;

	}

	public String isTouzhu() {
		String isTouzhu = "";
		int ge = BallTable.getHighlightBallNums();
		int iZhuShu = getZhuShu();
		if (ge == 0) {
			isTouzhu = "请至少选择一注！";
		} else if (iZhuShu > MAX_ZHU) {
			isTouzhu = "false";
		}
		else {
			isTouzhu = "true";
		}

		return isTouzhu;
	}

	public String getZxAlertZhuma() {
		int iZhuShu = getZhuShu();
		String zhuma_string = "";
		int[] ZhuMa = BallTable.getHighlightBallNOs();
		for (int i = 0; i < BallTable.getHighlightBallNOs().length; i++) {
			zhuma_string = zhuma_string + String.valueOf(ZhuMa[i]);
			if (i != BallTable.getHighlightBallNOs().length - 1) {
				zhuma_string = zhuma_string + ",";
			}
		}
		return "注码：" + "\n" + "个位：" + zhuma_string;

	}

	public int getZhuShu() {
		int iReturnValue = 0;
		if (isjixuan) {
			iReturnValue = balls.size() * iProgressBeishu;
		} else {
			int ge = areaNums[0].table.getHighlightBallNums();
			iReturnValue = ge * iProgressBeishu;
		}

		return iReturnValue;
	}

	public String getZhuma() {
		String zhuma = "";
		if (isjixuan) {
			zhuma = ballOne.getZhuma(balls, SSC_TYPE);
		} else {
			zhuma = sscCode.zhuma(areaNums, iProgressBeishu, 0);
		}
		return zhuma;
	}

	@Override
	public String getZhuma(Balls ball) {
		String zhuma = "";
		zhuma = ball.getZhuma(null, SSC_TYPE);
		return zhuma;
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
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

	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		String zhumas = "-,-,-,-,";
		int num = 0;// 高亮小球数
		int length = 0;
		for (int j = 0; j < areaNums.length; j++) {
			BallTable ballTable = areaNums[j].table;
			int[] zhuMa = ballTable.getHighlightBallNOs();
			for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
				if (highttype.equals("SSC")) {
					zhumas += (zhuMa[i]) + "";
				} else if (highttype.equals("DLC")) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
				}
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
			showEditTitle(this.type);
		} else {
			builder.append(zhumas);
			String zhuma[] = zhumas.split("\\|");
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

	@Override
	public void touzhuNet() {
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

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_SSC);
		codeInfo.setTouZhuType("1start");
	}

}
