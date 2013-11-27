package com.ruyicai.activity.buy.qxc;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.qxc.QXCZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

public class QXCZhiXuan extends ZixuanActivity {
	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[7];// 七星彩个选区
	private QXCZiZhiXuanCode qxcCode = new QXCZiZhiXuanCode();
	BallTable firstlineBallTable;
	BallTable secondlineBallTable;
	BallTable thirdlineBallTable;
	BallTable fourthlineBallTable;
	BallTable fifthlineBallTable;
	BallTable sixthlineBallTable;
	BallTable seventhlineBallTable;

	public void onCreate(Bundle savedInstanceState) {
		setAddView(((QXC)getParent()).addView);
		super.onCreate(savedInstanceState);
		setCode(qxcCode);
		initViewItem();
		// LinearLayout sevenLinear =
		// (LinearLayout)findViewById(R.id.buy_zixuan_linear_seven);
		// LinearLayout sixLinear =
		// (LinearLayout)findViewById(R.id.buy_zixuan_linear_six);
		// sixLinear.setVisibility(LinearLayout.VISIBLE);
		// sevenLinear.setVisibility(LinearLayout.VISIBLE);
		// LinearLayout fiveLinear =
		// (LinearLayout)findViewById(R.id.buy_zixuan_linear_five);
		// fiveLinear.setVisibility(LinearLayout.VISIBLE);
		// LinearLayout fourLinear =
		// (LinearLayout)findViewById(R.id.buy_zixuan_linear_four);
		// fourLinear.setVisibility(LinearLayout.VISIBLE);
		// LinearLayout thirdLinear =
		// (LinearLayout)findViewById(R.id.buy_zixuan_linear_third);
		// thirdLinear.setVisibility(LinearLayout.VISIBLE);

		firstlineBallTable = itemViewArray.get(0).areaNums[0].table;
		secondlineBallTable = itemViewArray.get(0).areaNums[1].table;
		thirdlineBallTable = itemViewArray.get(0).areaNums[2].table;
		fourthlineBallTable = itemViewArray.get(0).areaNums[3].table;
		fifthlineBallTable = itemViewArray.get(0).areaNums[4].table;
		sixthlineBallTable = itemViewArray.get(0).areaNums[5].table;
		seventhlineBallTable = itemViewArray.get(0).areaNums[6].table;
	}

	/**
	 * 初始化选区界面
	 */
	public void initViewItem() {
		// TODO Auto-generated method stub
		BuyViewItem buyView = new BuyViewItem(this, initArea());
		itemViewArray.add(buyView);
		layoutView.addView(buyView.createView());
	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		AreaNum areaNums[] = new AreaNum[7];
		String firstTitle = getResources().getString(R.string.qxc_first)
				.toString();
		String secondTitle = getResources().getString(R.string.qxc_second)
				.toString();
		String thirdTitle = getResources().getString(R.string.qxc_third)
				.toString();
		String fourthTitle = getResources().getString(R.string.qxc_fourth)
				.toString();
		String fifthTitle = getResources().getString(R.string.qxc_fifth)
				.toString();
		String sixthTitle = getResources().getString(R.string.qxc_sixth)
				.toString();
		String seventhTitle = getResources().getString(R.string.qxc_seventh)
				.toString();
		areaNums[0] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				firstTitle, false, true);
		areaNums[1] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				secondTitle, false, true);
		areaNums[2] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				thirdTitle, false, true);
		areaNums[3] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				fourthTitle, false, true);
		areaNums[4] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				fifthTitle, false, true);
		areaNums[5] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				sixthTitle, false, true);
		areaNums[6] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				seventhTitle, false, true);
		return areaNums;
	}

	@Override
	public String isTouzhu() {
		String isTouzhu = "";
		int firstweiNums = firstlineBallTable.getHighlightBallNums();
		int secondweiNums = secondlineBallTable.getHighlightBallNums();
		int thirdweiNums = thirdlineBallTable.getHighlightBallNums();
		int fourthweiNums = fourthlineBallTable.getHighlightBallNums();
		int fifthweiNums = fifthlineBallTable.getHighlightBallNums();
		int sixthweiNums = sixthlineBallTable.getHighlightBallNums();
		int seventhweiNums = seventhlineBallTable.getHighlightBallNums();

		int[] firstweis = firstlineBallTable.getHighlightBallNOs();
		int[] secondweis = secondlineBallTable.getHighlightBallNOs();
		int[] thirdweis = thirdlineBallTable.getHighlightBallNOs();
		int[] fourthweis = fourthlineBallTable.getHighlightBallNOs();
		int[] fifthweis = fifthlineBallTable.getHighlightBallNOs();
		int[] sixthweis = sixthlineBallTable.getHighlightBallNOs();
		int[] seventhweis = seventhlineBallTable.getHighlightBallNOs();

		String firstweistr = "";
		String secondweistr = "";
		String thirdweistr = "";
		String fourthweistr = "";
		String fifthweistr = "";
		String sixthweistr = "";
		String seventhweistr = "";

		for (int i = 0; i < firstweiNums; i++) {
			firstweistr += (firstweis[i]) + ",";
			if (i == firstweiNums - 1) {
				firstweistr = firstweistr
						.substring(0, firstweistr.length() - 1);
			}
		}
		for (int i = 0; i < secondweiNums; i++) {
			secondweistr += (secondweis[i]) + ",";
			if (i == secondweiNums - 1) {
				secondweistr = secondweistr.substring(0,
						secondweistr.length() - 1);
			}
		}
		for (int i = 0; i < thirdweiNums; i++) {
			thirdweistr += (thirdweis[i]) + ",";
			if (i == thirdweiNums - 1) {
				thirdweistr = thirdweistr
						.substring(0, thirdweistr.length() - 1);
			}
		}
		for (int i = 0; i < fourthweiNums; i++) {
			fourthweistr += (fourthweis[i]) + ",";
			if (i == fourthweiNums - 1) {
				fourthweistr = fourthweistr.substring(0,
						fourthweistr.length() - 1);
			}
		}
		for (int i = 0; i < fifthweiNums; i++) {
			fifthweistr += (fifthweis[i]) + ",";
			if (i == fifthweiNums - 1) {
				fifthweistr = fifthweistr
						.substring(0, fifthweistr.length() - 1);
			}
		}
		for (int i = 0; i < sixthweiNums; i++) {
			sixthweistr += (sixthweis[i]) + ",";
			if (i == sixthweiNums - 1) {
				sixthweistr = sixthweistr
						.substring(0, sixthweistr.length() - 1);
			}
		}
		for (int i = 0; i < seventhweiNums; i++) {
			seventhweistr += (seventhweis[i]) + ",";
			if (i == seventhweiNums - 1) {
				seventhweistr = seventhweistr.substring(0,
						seventhweistr.length() - 1);
			}
		}
		if (firstweiNums < 1 || secondweiNums < 1 || thirdweiNums < 1
				|| fourthweiNums < 1 || fifthweiNums < 1 || sixthweiNums < 1
				|| seventhweiNums < 1) {
			isTouzhu = "每位至少要选择一个小球，检查一下吧 ";
		} else {
			int iZhuShu = getZhuShu();
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	/**
	 * 点击小球提示金额
	 */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String mTextSumMoney = "";
		if (firstlineBallTable.getHighlightBallNums() > 0
				&& secondlineBallTable.getHighlightBallNums() > 0
				&& thirdlineBallTable.getHighlightBallNums() > 0
				&& fourthlineBallTable.getHighlightBallNums() > 0
				&& fifthlineBallTable.getHighlightBallNums() > 0
				&& sixthlineBallTable.getHighlightBallNums() > 0
				&& seventhlineBallTable.getHighlightBallNums() > 0) {
			int iZhuShu = getZhuShu();
			mTextSumMoney = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		} else if (firstlineBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "第一位的小球为空噢";
		} else if (secondlineBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "第二位的小球为空噢";
		} else if (thirdlineBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "第三位的小球为空噢";
		} else if (fourthlineBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "第四位的小球为空噢";
		} else if (fifthlineBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "第五位的小球为空噢";
		} else if (sixthlineBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "第六位的小球为空噢";
		} else if (seventhlineBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "第七位的小球为空噢";
		}

		return mTextSumMoney;
	}

	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_QXC);
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
		for (int j = 0; j < itemViewArray.get(0).areaNums.length; j++) {
			BallTable ballTable = itemViewArray.get(0).areaNums[j].table;
			int[] zhuMa = ballTable.getHighlightBallNOs();
			if (j != 0) {
				zhumas += " , ";
			}
			for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
				if (isTen) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
				} else {
					zhumas += zhuMa[i] + "";
				}
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
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
				builder.setSpan(new ForegroundColorSpan(
						itemViewArray.get(0).areaNums[i].textColor), length
						- zhuma[i].length(), length, Spanned.SPAN_COMPOSING);

			}
			editZhuma.setText(builder, BufferType.EDITABLE);
		}
	}

	/**
	 * 获取注数
	 */
	public int getZhuShu() {
		int iReturnValue = 0;
		iReturnValue = firstlineBallTable.getHighlightBallNums()
				* secondlineBallTable.getHighlightBallNums()
				* thirdlineBallTable.getHighlightBallNums()
				* fourthlineBallTable.getHighlightBallNums()
				* fifthlineBallTable.getHighlightBallNums()
				* sixthlineBallTable.getHighlightBallNums()
				* seventhlineBallTable.getHighlightBallNums();
		return iReturnValue * iProgressBeishu;

	}

	@Override
	public String getZhuma() {
		// TODO Auto-generated method stub
		return null;
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_QXC);
		codeInfo.setTouZhuType("zhixuan");
	}
}
