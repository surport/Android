/**
 * 
 */
package com.ruyicai.activity.buy.qlc;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zixuan.BuyViewItem;
import com.ruyicai.activity.buy.zixuan.ZixuanActivity;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.qlc.QlcZiDanTuoCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

/**
 * 七乐彩自选胆拖
 * 
 * @author Administrator
 * 
 */
public class QlcZiDanTuo extends ZixuanActivity {
	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[2];// 2个选区
	private QlcZiDanTuoCode qlcCode = new QlcZiDanTuoCode();
	BallTable danBallTable;
	BallTable tuoBallTable;

	public void onCreate(Bundle savedInstanceState) {
		setAddView(((Qlc)getParent()).addView);
		super.onCreate(savedInstanceState);
		setCode(qlcCode);
		setIsTen(true);
		initViewItem();
		danBallTable = itemViewArray.get(0).areaNums[0].table;
		tuoBallTable = itemViewArray.get(0).areaNums[1].table;
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
		AreaNum areaNums[] = new AreaNum[2];
		String danma = getResources().getString(
				R.string.ssq_dantuo_text_red_danma_title).toString();
		String tuoma = getResources().getString(
				R.string.ssq_dantuo_text_red_tuoma_title).toString();
		areaNums[0] = new AreaNum(30, 8, 6, ballResId, 0, 1, Color.RED, danma);
		areaNums[1] = new AreaNum(30, 8, 20, ballResId, 0, 1, Color.RED, tuoma);
		return areaNums;
	}

	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId) {
		AreaNum[] areaNums = itemViewArray.get(0).areaNums;
		int nBallId = 0;
		for (int i = 0; i < areaNums.length; i++) {
			nBallId = iBallId;
			iBallId = iBallId - areaNums[i].areaNum;
			if (iBallId < 0) {
				if (i == 0) {
					int isHighLight = areaNums[0].table.changeBallState(
							areaNums[0].chosenBallSum, nBallId);
					if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT
							&& areaNums[1].table.getOneBallStatue(nBallId) != 0) {
						areaNums[1].table.clearOnBallHighlight(nBallId);

						toast.setText(getResources().getString(
								R.string.ssq_toast_danma_title));
						toast.show();
					}

				} else if (i == 1) {
					int isHighLight = areaNums[1].table.changeBallState(
							areaNums[1].chosenBallSum, nBallId);
					if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT
							&& areaNums[0].table.getOneBallStatue(nBallId) != 0) {
						areaNums[0].table.clearOnBallHighlight(nBallId);

						toast.setText(getResources().getString(
								R.string.ssq_toast_tuoma_title));
						toast.show();
					}
				}

				break;
			}

		}

	}

	public int getZhuShu() {
		int iRedHighlights = danBallTable.getHighlightBallNums();
		int iRedTuoHighlights = tuoBallTable.getHighlightBallNums();
		int iReturnValue = (int) getQLCDTZhuShu(iRedHighlights,
				iRedTuoHighlights);
		return iReturnValue;
	}

	/**
	 * 投注提示框中的信息(注码)
	 * 
	 * @return
	 */
	public String getZhuma() {
		String red_zhuma_string = " ";
		int[] redZhuMa = danBallTable.getHighlightBallNOs();
		for (int i = 0; i < danBallTable.getHighlightBallNOs().length; i++) {
			red_zhuma_string += PublicMethod.getZhuMa(redZhuMa[i]);
			if (i != danBallTable.getHighlightBallNOs().length - 1)
				red_zhuma_string += ",";
		}
		String red_tuo_zhuma_string = " ";
		int[] redTuoZhuMa = tuoBallTable.getHighlightBallNOs();
		for (int i = 0; i < tuoBallTable.getHighlightBallNOs().length; i++) {
			red_tuo_zhuma_string += PublicMethod.getZhuMa(redTuoZhuMa[i]);
			if (i != tuoBallTable.getHighlightBallNOs().length - 1)
				red_tuo_zhuma_string += ",";
		}
		return "注码：\n" + "胆码：" + red_zhuma_string + "\n" + "拖码："
				+ red_tuo_zhuma_string;

	}

	/**
	 * 判断是否满足投注条件
	 */
	public String isTouzhu() {
		String isTouzhu = "";
		int iZhuShu = getZhuShu();
		int redNumber = danBallTable.getHighlightBallNums();
		int redTuoNumber = tuoBallTable.getHighlightBallNums();
		// int blueNumber = blueBallTable.getHighlightBallNums();
		if ((redNumber < 1 || redNumber > 6)
				&& (redTuoNumber < 1 || redTuoNumber > 29)) {
			isTouzhu = "请选择1~6个胆码，1~29个拖码！";
		} else if (redNumber + redTuoNumber < 8) {
			isTouzhu = "胆码和拖码之和至少为8个！";
		} else if (iZhuShu <= 0) {
			isTouzhu = "胆码和拖码之和至少为8个！";
		} else if (iZhuShu * 2 > 100000) {
			isTouzhu = "false";
		} else {
			isTouzhu = "true";
		}
		return isTouzhu;
	}

	/**
	 * 点击小球提示金额
	 * 
	 * @param areaNum
	 * @param iProgressBeishu
	 * @return
	 */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String iTempString = "";
		int iRedDanHighlights = areaNum[0].table.getHighlightBallNums();
		int iRedTuoHighlights = areaNum[1].table.getHighlightBallNums();

		if (iRedDanHighlights + iRedTuoHighlights < 8) {
			int num = 8 - iRedDanHighlights - iRedTuoHighlights;
			if (iRedDanHighlights == 0) {
				iTempString = "选择1个胆码";
			} else {
				iTempString = "至少还需要" + num + "个拖码";
			}
		} else {
			int iZhuShu = (int) getQLCDTZhuShu(iRedDanHighlights,
					iRedTuoHighlights);
			iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		}

		return iTempString;
	}

	/*
	 * 胆拖玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @param int aRedTuoBalls 红球拖码个数
	 */
	private long getQLCDTZhuShu(int aRedBalls, int aRedTuoBalls) {
		long qlcZhuShu = 0L;
		if (aRedBalls > 0 && aRedTuoBalls > 0) {
			qlcZhuShu += (PublicMethod.zuhe(7 - aRedBalls, aRedTuoBalls) * iProgressBeishu);
		}
		return qlcZhuShu;
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
				zhumas += " # ";
			}
			for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
				if (isTen) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
				} else {
					zhumas += zhuMa[i] + "";
				}

				if (i != ballTable.getHighlightBallNOs().length - 1) {
					zhumas += ",";
				}
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
		} else {
			builder.append(zhumas);
			String zhuma[] = zhumas.split("\\#");
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
	 * 投注联网
	 */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");// 1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_QLC);

	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	void setLotoNoAndType(CodeInfo codeInfo) {
		codeInfo.setLotoNo(Constants.LOTNO_QLC);
		codeInfo.setTouZhuType("dantuo");
	}
}
