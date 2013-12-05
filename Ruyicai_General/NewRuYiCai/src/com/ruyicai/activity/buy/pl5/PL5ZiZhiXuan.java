package com.ruyicai.activity.buy.pl5;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.pl5.PL5ZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

public class PL5ZiZhiXuan extends ZixuanActivity {

	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[5];// 排列5具有5个选区
	private PL5ZiZhiXuanCode pl5Code = new PL5ZiZhiXuanCode();
	BallTable wanBallTable;
	BallTable qianBallTable;
	BallTable baiBallTable;
	BallTable shiBallTable;
	BallTable geBallTable;

	public void onCreate(Bundle savedInstanceState) {
		setAddView(((PL5)getParent()).addView);
		super.onCreate(savedInstanceState);
		setCode(pl5Code);
		initViewItem();
		wanBallTable = itemViewArray.get(0).areaNums[0].table;
		qianBallTable = itemViewArray.get(0).areaNums[1].table;
		baiBallTable = itemViewArray.get(0).areaNums[2].table;
		shiBallTable = itemViewArray.get(0).areaNums[3].table;
		geBallTable = itemViewArray.get(0).areaNums[4].table;
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
		AreaNum areaNums[] = new AreaNum[5];
		String wanTitle = getResources().getString(R.string.ssc_table_wan)
				.toString();
		String qianTitle = getResources().getString(R.string.ssc_table_qian)
				.toString();
		String baiTitle = getResources().getString(R.string.ssc_table_bai)
				.toString();
		String shiTitle = getResources().getString(R.string.ssc_table_shi)
				.toString();
		String geTitle = getResources().getString(R.string.ssc_table_ge)
				.toString();

		areaNums[0] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				wanTitle, false, true);
		areaNums[1] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				qianTitle, false, true);
		areaNums[2] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				baiTitle, false, true);
		areaNums[3] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				shiTitle, false, true);
		areaNums[4] = new AreaNum(10, 10, 1, 10, ballResId, 0, 0, Color.RED,
				geTitle, false, true);
		return areaNums;
	}

	@Override
	public String isTouzhu() {
		String isTouzhu = "";
		int wanweiNums = wanBallTable.getHighlightBallNums();
		int qianweiNums = qianBallTable.getHighlightBallNums();
		int baiweiNums = baiBallTable.getHighlightBallNums();
		int shiweiNums = shiBallTable.getHighlightBallNums();
		int geweiNums = geBallTable.getHighlightBallNums();

		int[] wanweis = wanBallTable.getHighlightBallNOs();
		int[] qianweis = qianBallTable.getHighlightBallNOs();
		int[] baiweis = baiBallTable.getHighlightBallNOs();
		int[] shiweis = shiBallTable.getHighlightBallNOs();
		int[] geweis = geBallTable.getHighlightBallNOs();
		String wanweistr = "";
		String qianweistr = "";
		String baiweistr = "";
		String shiweistr = "";
		String geweistr = "";
		for (int i = 0; i < wanweiNums; i++) {
			wanweistr += (wanweis[i]) + ",";
			if (i == wanweiNums - 1) {
				wanweistr = wanweistr.substring(0, wanweistr.length() - 1);
			}
		}
		for (int i = 0; i < qianweiNums; i++) {
			qianweistr += (qianweis[i]) + ",";
			if (i == qianweiNums - 1) {
				qianweistr = qianweistr.substring(0, qianweistr.length() - 1);
			}
		}
		for (int i = 0; i < baiweiNums; i++) {
			baiweistr += (baiweis[i]) + ",";
			if (i == baiweiNums - 1) {
				baiweistr = baiweistr.substring(0, baiweistr.length() - 1);
			}
		}
		for (int i = 0; i < shiweiNums; i++) {
			shiweistr += (shiweis[i]) + ",";
			if (i == shiweiNums - 1) {
				shiweistr = shiweistr.substring(0, shiweistr.length() - 1);
			}
		}
		for (int i = 0; i < geweiNums; i++) {
			geweistr += (geweis[i]) + ",";
			if (i == geweiNums - 1) {
				geweistr = geweistr.substring(0, geweistr.length() - 1);
			}
		}
		if (wanweiNums < 1 || qianweiNums < 1 || baiweiNums < 1
				|| shiweiNums < 1 || geweiNums < 1) {
			isTouzhu = "每位至少要选择一个小球，检查一下吧";
		} else {
			int iZhuShu = getZhuShu();
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		String mTextSumMoney = "";
		if (wanBallTable.getHighlightBallNums() > 0
				&& qianBallTable.getHighlightBallNums() > 0
				&& baiBallTable.getHighlightBallNums() > 0
				&& shiBallTable.getHighlightBallNums() > 0
				&& geBallTable.getHighlightBallNums() > 0) {
			int iZhuShu = getZhuShu();
			mTextSumMoney = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		} else if (wanBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "万位的小球为空噢";
		} else if (qianBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "千位的小球为空噢";
		} else if (baiBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "百位的小球为空噢";
		} else if (shiBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "十位的小球为空噢";
		} else if (geBallTable.getHighlightBallNums() == 0) {
			mTextSumMoney = "个位的小球为空噢";
		}

		return mTextSumMoney;
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

	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_PL5);
	}

	/**
	 * 获取注数
	 */
	public int getZhuShu() {
		int iReturnValue = 0;
		iReturnValue = wanBallTable.getHighlightBallNums()
				* qianBallTable.getHighlightBallNums()
				* baiBallTable.getHighlightBallNums()
				* shiBallTable.getHighlightBallNums()
				* geBallTable.getHighlightBallNums();
		return iReturnValue * iProgressBeishu;

	}

	@Override
	public String getZhuma() {
		// TODO Auto-generated method stub
		return null;
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_PL5);
		codeInfo.setTouZhuType("zhixuan");
	}
}
